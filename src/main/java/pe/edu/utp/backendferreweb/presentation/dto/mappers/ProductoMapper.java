package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.Producto;
import pe.edu.utp.backendferreweb.presentation.dto.request.ProductoRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.AlmacenProductoResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.ProductoResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.UnidadProductoResponse;
import pe.edu.utp.backendferreweb.service.CategoriaService;
import pe.edu.utp.backendferreweb.service.UnidadService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductoMapper {

    private final UnidadMapper unidadMapper;
    private final CategoriaMapper categoriaMapper;
    private final UnidadService unidadService;
    private final AlmacenMapper almacenMapper;
    private final CategoriaService categoriaService;

    public Producto toEntity(ProductoRequest request) {
        if (request == null) return null;

        return Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .categoria(categoriaService.obtenerPorNombre(request.getCategoria()))
                .unidadPorDefecto(unidadService.obtenerPorNombre(request.getUnidadPorDefecto()))
                .build();
    }

    public ProductoResponse toResponse(Producto producto) {
        if (producto == null) return null;

        List<AlmacenProductoResponse> almacenes = null;
        if (producto.getAlmacenes() != null) {
            almacenes = producto.getAlmacenes().stream()
                    .map(productosPorAlmacen -> AlmacenProductoResponse.builder()
                            .idProducto(producto.getIdProducto())
                            .almacen(almacenMapper.toResponse(productosPorAlmacen.getAlmacen()))
                            .cantidad(productosPorAlmacen.getCantidad())
                            .build())
                    .toList();
        }

        List<UnidadProductoResponse> unidadesPermitidas = null;
        if (producto.getUnidadesPermitidas() != null) {
            unidadesPermitidas = producto.getUnidadesPermitidas().stream()
                    .map(unidadesPorProducto -> UnidadProductoResponse.builder()
                            .idProducto(producto.getIdProducto())
                            .unidad(unidadMapper.toResponse(unidadesPorProducto.getUnidad()))
                            .precio(unidadesPorProducto.getPrecio())
                            .equivalencia(unidadesPorProducto.getEquivalencia())
                            .build())
                    .toList();
        }

        return ProductoResponse.builder()
                .idProducto(producto.getIdProducto())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .rutaImagen(producto.getRutaImagen())
                .stock((int) Math.round(producto.getStock()))
                .categoria(categoriaMapper.toResponse(producto.getCategoria()))
                .unidadPorDefecto(unidadMapper.toResponse(producto.getUnidadPorDefecto()))
                .almacenes(almacenes)
                .unidadesPermitidas(unidadesPermitidas)
                .build();
    }

}
