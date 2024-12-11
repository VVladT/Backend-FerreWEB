package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.persistence.model.*;
import pe.edu.utp.backendferreweb.persistence.model.enums.EAuditAction;
import pe.edu.utp.backendferreweb.persistence.model.enums.EEstadoCompra;
import pe.edu.utp.backendferreweb.persistence.repository.OrdenCompraRepository;
import pe.edu.utp.backendferreweb.persistence.repository.ProductoRepository;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.OrdenCompraMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.DetalleCompraRequest;
import pe.edu.utp.backendferreweb.presentation.dto.request.DetalleTransferenciaRequest;
import pe.edu.utp.backendferreweb.presentation.dto.request.OrdenCompraRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.FileResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.OrdenCompraResponse;
import pe.edu.utp.backendferreweb.util.reports.OrdenCompraExcelGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenCompraMapper ordenCompraMapper;
    private final EstadoCompraService estadoCompraService;
    private final MailMessageService mailMessageService;
    private final ProductosPorAlmacenService productosPorAlmacenService;
    private final ProductoRepository productoRepository;
    private final AuditoriaService auditoriaService;
    private final DetalleCompraService detalleCompraService;
    private final OrdenCompraExcelGenerator ordenCompraExcelGenerator;

    public List<OrdenCompraResponse> obtenerTodas() {
        return ordenCompraRepository.findAll().stream()
                .map(ordenCompraMapper::toResponse)
                .toList();
    }

    public OrdenCompraResponse obtenerPorId(Integer id) {
        OrdenCompra ordenCompra = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe orden de compra con id: " + id));

        return ordenCompraMapper.toResponse(ordenCompra);
    }

    public OrdenCompraResponse crearOrdenCompra(OrdenCompraRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        OrdenCompra ordenCompra = ordenCompraMapper.toEntity(request);
        for (DetalleCompraRequest detalle : request.getDetalles()) {
            ordenCompra.addDetalle(detalleCompraService.registrarDetalle(detalle));
        }

        ordenCompra.setSolicitante(usuario);
        ordenCompra.setFechaEmision(LocalDateTime.now());
        ordenCompra.setEstadoCompra(estadoCompraService.obtenerPorNombre(EEstadoCompra.PENDIENTE.name()));

        OrdenCompra ordenGuardada = ordenCompraRepository.save(ordenCompra);

        return ordenCompraMapper.toResponse(ordenGuardada);
    }

    public FileResponse descargarOrdenCompra(Integer id) throws IOException {
        OrdenCompraResponse ordenCompra = obtenerPorId(id);

        File ordenXlsx = null;
        try {
            ordenXlsx = ordenCompraExcelGenerator.generateOrdenCompra(ordenCompra);
        } catch (IOException e) {
            throw new IOException("Error al generar la orden de compra");
        }

        return FileResponse.builder()
                .name(String.format("orden_compra_%08d", ordenCompra.getIdOrdenCompra()))
                .extension(".xlsx")
                .file(ordenXlsx)
                .mimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .build();
    }

    @SneakyThrows
    public OrdenCompraResponse aprobarOrdenCompra(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        OrdenCompra ordenCompra = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe orden de compra con id: " + id));

        if (ordenCompra == null)
            throw new EntityNotFoundException("No existe orden de compra con id: " + id);

        String estado = ordenCompra.getEstadoCompra().getEstado();

        if (!estado.equals(EEstadoCompra.PENDIENTE.name()))
            throw new IllegalStateException("Solo se puede aprobar ordenes de compra PENDIENTES");

        ordenCompra.setEstadoCompra(estadoCompraService.obtenerPorNombre(EEstadoCompra.APROBADO.name()));
        ordenCompra.setUsuarioAutorizacion(usuario);

        OrdenCompra ordenActualizada = ordenCompraRepository.save(ordenCompra);

        OrdenCompraResponse response = ordenCompraMapper.toResponse(ordenActualizada);
        File ordenXlsx = ordenCompraExcelGenerator.generateOrdenCompra(response);
        mailMessageService.enviarOrdenCompra(ordenXlsx, response);
        ordenXlsx.delete();

        return response;
    }

    public OrdenCompraResponse pagarOrdenCompra(Integer id,
                                                DetalleTransferenciaRequest request,
                                                MultipartFile file) {
        OrdenCompra ordenCompra = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe orden de compra con id: " + id));

        if (ordenCompra == null)
            throw new EntityNotFoundException("No existe orden de compra con id: " + id);

        String estado = ordenCompra.getEstadoCompra().getEstado();

        if (!estado.equals(EEstadoCompra.APROBADO.name()))
            throw new IllegalStateException("Solo se puede pagar ordenes de compra APROBADAS");

        ordenCompra.setEstadoCompra(estadoCompraService.obtenerPorNombre(EEstadoCompra.PAGADO.name()));
        OrdenCompra ordenActualizada = ordenCompraRepository.save(ordenCompra);

        OrdenCompraResponse response = ordenCompraMapper.toResponse(ordenActualizada);

        mailMessageService.enviarDetallesTransferencia(response, request, file);

        return response;
    }

    public OrdenCompraResponse procesarEntrega(Integer id) {
        OrdenCompra ordenCompra = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe orden de compra con id: " + id));

        if (ordenCompra == null)
            throw new EntityNotFoundException("No existe orden de compra con id: " + id);

        String estado = ordenCompra.getEstadoCompra().getEstado();

        if (!estado.equals(EEstadoCompra.PAGADO.name()))
            throw new IllegalStateException("Solo se puede procesar la entrega de ordenes de compra PAGADAS");

        ordenCompra.setEstadoCompra(estadoCompraService.obtenerPorNombre(EEstadoCompra.ENTREGADO.name()));
        actualizarStockDeProductos(ordenCompra);
        OrdenCompra ordenActualizada = ordenCompraRepository.save(ordenCompra);
        return ordenCompraMapper.toResponse(ordenActualizada);
    }

    public OrdenCompraResponse cancelarOrdenCompra(Integer id, String motivo) {
        OrdenCompra ordenCompra = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe orden de compra con id: " + id));

        if (ordenCompra == null)
            throw new EntityNotFoundException("No existe orden de compra con id: " + id);

        String estado = ordenCompra.getEstadoCompra().getEstado();

        if (!estado.equals(EEstadoCompra.PENDIENTE.name()))
            throw new IllegalStateException("Solo se pueden cancelar ordenes PENDIENTES");

        ordenCompra.setEstadoCompra(estadoCompraService.obtenerPorNombre(EEstadoCompra.CANCELADO.name()));

        auditoriaService.registerAudit(ordenCompra.getIdOrdenCompra(), "Orden_Compra", EAuditAction.UPDATE.name(), motivo);
        OrdenCompra ordenActualizada = ordenCompraRepository.save(ordenCompra);

        return ordenCompraMapper.toResponse(ordenActualizada);
    }

    private void actualizarStockDeProductos(OrdenCompra ordenCompra) {
        for (DetalleCompra detalle : ordenCompra.getDetalles()) {
            Producto producto = detalle.getProducto();

            UnidadesPorProducto unidad = producto.getUnidadesPermitidas().stream()
                    .filter(u -> u.getUnidad().equals(detalle.getUnidad()))
                    .findFirst().orElse(null);

            if (unidad == null)
                throw new IllegalStateException("La unidad del producto está en un estado no válido.");

            ProductosPorAlmacenPK almacenesPK = new ProductosPorAlmacenPK();
            almacenesPK.setIdProducto(producto.getIdProducto());
            almacenesPK.setIdAlmacen(ordenCompra.getDestino().getIdAlmacen());

            ProductosPorAlmacen almacen = productosPorAlmacenService.obtenerEntidadPorId(almacenesPK);

            Double cantidadParaAgregar = detalle.getCantidad() * unidad.getEquivalencia();

            if (almacen == null) {
                almacen = ProductosPorAlmacen.builder()
                        .primaryKey(almacenesPK)
                        .producto(producto)
                        .almacen(ordenCompra.getDestino())
                        .cantidad(cantidadParaAgregar)
                        .build();
            } else {
                Double stockActualizado = almacen.getCantidad() + cantidadParaAgregar;
                almacen.setCantidad(stockActualizado);
            }


            if (!producto.getAlmacenes().contains(almacen)) {
                producto.addAlmacen(almacen);
            }

            productosPorAlmacenService.actualizarAlmacenPorProducto(almacen);
            producto.calcularStock();
            productoRepository.save(producto);
        }
    }
}
