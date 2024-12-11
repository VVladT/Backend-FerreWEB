package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.*;
import pe.edu.utp.backendferreweb.persistence.model.enums.EEstadoVenta;
import pe.edu.utp.backendferreweb.persistence.repository.ProductoRepository;
import pe.edu.utp.backendferreweb.persistence.repository.VentaPresencialRepository;
import pe.edu.utp.backendferreweb.persistence.repository.VentaRepository;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.VentaMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.DetalleVentaRequest;
import pe.edu.utp.backendferreweb.presentation.dto.request.VentaPresencialRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.VentaResponse;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;
    private final EstadoVentaService estadoVentaService;
    private final DetalleVentaService detalleVentaService;
    private final VentaPresencialRepository ventaPresencialRepository;
    private final ProductosPorAlmacenService productosPorAlmacenService;
    private final AlmacenService almacenService;
    private final ProductoRepository productoRepository;

    public List<VentaResponse> obtenerTodas() {
        return ventaRepository.findAll().stream()
                .map(ventaMapper::toResponse)
                .toList();
    }

    public VentaResponse obtenerPorId(Integer id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe venta con id: " + id));

        return ventaMapper.toResponse(venta);
    }

    public VentaResponse registrarVentaPresencial(VentaPresencialRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario cajero = (Usuario) authentication.getPrincipal();

        VentaPresencial venta = new VentaPresencial();
        venta.setDniCliente(request.getDniCliente());
        venta.setFecha(LocalDateTime.now());
        venta.setAlmacen(almacenService.obtenerEntidadPorId(request.getIdAlmacen()));
        venta.setResponsable(cajero);
        venta.setEstadoVenta(estadoVentaService.obtenerPorNombre(EEstadoVenta.FINALIZADA.name()));
        venta.setDetalles(new HashSet<>());

        for (DetalleVentaRequest detalle : request.getDetalles()) {
            venta.addDetalle(detalleVentaService.registrarDetalle(detalle));
        }

        VentaPresencial ventaGuardada = ventaPresencialRepository.save(venta);
        actualizarStockDeProductos(ventaGuardada);

        return ventaMapper.toResponse(ventaGuardada);
    }

    private void actualizarStockDeProductos(VentaPresencial venta) {
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();

            UnidadesPorProducto unidad = producto.getUnidadesPermitidas().stream()
                    .filter(u -> u.getUnidad().equals(detalle.getUnidad()))
                    .findFirst().orElse(null);

            if (unidad == null)
                throw new IllegalStateException("La unidad del producto está en un estado no válido.");

            ProductosPorAlmacenPK almacenesPK = new ProductosPorAlmacenPK();
            almacenesPK.setIdProducto(producto.getIdProducto());
            almacenesPK.setIdAlmacen(venta.getAlmacen().getIdAlmacen());

            ProductosPorAlmacen almacen = productosPorAlmacenService.obtenerEntidadPorId(almacenesPK);

            Double cantidadADisminuir = detalle.getCantidad() * unidad.getEquivalencia();

            if (almacen == null) {
                throw new IllegalStateException("Los productos deben salir de un almacen");
            }

            Double stockActualizado = almacen.getCantidad() - cantidadADisminuir;

            if (stockActualizado < 0)
                throw new IllegalStateException("La cantidad de productos a comprar es mamyor que el stock");

            almacen.setCantidad(stockActualizado);

            if (!producto.getAlmacenes().contains(almacen)) {
                producto.addAlmacen(almacen);
            }

            productosPorAlmacenService.actualizarAlmacenPorProducto(almacen);
            producto.calcularStock();
            productoRepository.save(producto);
        }
    }
}
