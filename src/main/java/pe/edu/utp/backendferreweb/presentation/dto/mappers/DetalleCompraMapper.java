package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.DetalleCompra;
import pe.edu.utp.backendferreweb.persistence.model.Producto;
import pe.edu.utp.backendferreweb.persistence.model.Unidad;
import pe.edu.utp.backendferreweb.persistence.model.UnidadesPorProducto;
import pe.edu.utp.backendferreweb.presentation.dto.request.DetalleCompraRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.DetalleCompraResponse;
import pe.edu.utp.backendferreweb.service.ProductoService;

@Component
@RequiredArgsConstructor
public class DetalleCompraMapper {

    private final ProductoService productoService;

    public DetalleCompra toEntity(DetalleCompraRequest request) {
        if (request == null) return null;

        Producto producto = productoService.obtenerEntidadPorId(request.getIdProducto());

        UnidadesPorProducto unidadPermitida = producto.getUnidadesPermitidas().stream()
                .filter(unidadesPorProducto ->
                        unidadesPorProducto.getUnidad().getIdUnidad().equals(request.getIdUnidad()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("La unidad con id: " +
                        request.getIdUnidad() +
                        " no es permitida en el producto: " + producto));

        Unidad unidad = unidadPermitida.getUnidad();
        Double precioUnitario = request.getPrecio();

        return DetalleCompra.builder()
                .producto(producto)
                .unidad(unidad)
                .cantidad(request.getCantidad())
                .precioUnitario(precioUnitario)
                .build();
    }

    public DetalleCompraResponse toResponse(DetalleCompra detalleCompra) {
        if (detalleCompra == null) return null;

        return DetalleCompraResponse.builder()
                .idDetalle(detalleCompra.getIdDetalleCompra())
                .idProducto(detalleCompra.getProducto().getIdProducto())
                .producto(detalleCompra.getProducto().getNombre())
                .cantidad(detalleCompra.getCantidad())
                .unidad(detalleCompra.getUnidad().getNombre())
                .precioUnitario(detalleCompra.getPrecioUnitario())
                .subtotal(detalleCompra.getSubtotal())
                .build();
    }
}
