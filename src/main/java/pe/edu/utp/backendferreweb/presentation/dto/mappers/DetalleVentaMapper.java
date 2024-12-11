package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.*;
import pe.edu.utp.backendferreweb.presentation.dto.request.DetalleVentaRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.DetalleVentaResponse;
import pe.edu.utp.backendferreweb.service.ProductoService;

@Component
@RequiredArgsConstructor
public class DetalleVentaMapper {

    private final ProductoService productoService;

    public DetalleVenta toEntity(DetalleVentaRequest request) {
        if (request == null) return null;

        Producto producto = productoService.obtenerEntidadPorId(request.getIdProducto());

        UnidadesPorProducto unidadPermitida = producto.getUnidadesPermitidas().stream()
                .filter(unidadesPorProducto ->
                        unidadesPorProducto.getUnidad().getIdUnidad().equals(request.getIdUnidad()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("La unidad con id: " +
                        request.getIdUnidad() +
                        " no es permitida en el producto: " + producto));

        Unidad unidad = unidadPermitida.getUnidad();
        Double cantidad = request.getCantidad();
        Double precio = unidadPermitida.getPrecio();

        return DetalleVenta.builder()
                .producto(producto)
                .unidad(unidad)
                .precioUnitario(precio)
                .cantidad(cantidad)
                .build();
    }

    public DetalleVentaResponse toResponse(DetalleVenta detalleVenta) {
        if (detalleVenta == null) return null;

        return DetalleVentaResponse.builder()
                .idDetalle(detalleVenta.getIdDetalleCompra())
                .idProducto(detalleVenta.getProducto().getIdProducto())
                .producto(detalleVenta.getProducto().getNombre())
                .cantidad(detalleVenta.getCantidad())
                .unidad(detalleVenta.getUnidad().getNombre())
                .precioUnitario(detalleVenta.getPrecioUnitario())
                .subtotal(detalleVenta.getSubtotal())
                .build();
    }
}
