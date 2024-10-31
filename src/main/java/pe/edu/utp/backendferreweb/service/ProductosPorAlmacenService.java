package pe.edu.utp.backendferreweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Almacen;
import pe.edu.utp.backendferreweb.persistence.model.Producto;
import pe.edu.utp.backendferreweb.persistence.model.ProductosPorAlmacen;
import pe.edu.utp.backendferreweb.persistence.model.ProductosPorAlmacenPK;
import pe.edu.utp.backendferreweb.persistence.repository.ProductosPorAlmacenRepository;
import pe.edu.utp.backendferreweb.presentation.dto.request.AlmacenCantidadRequest;

@Service
@RequiredArgsConstructor
public class ProductosPorAlmacenService {
    private final ProductosPorAlmacenRepository productosPorAlmacenRepository;
    private final AlmacenService almacenService;

    public ProductosPorAlmacen registrarProductosPorAlmacen(Producto producto, AlmacenCantidadRequest request) {
        Almacen almacen = almacenService.obtenerPorNombre(request.getNombreAlmacen());
        Integer cantidad = request.getCantidadProductos();

        ProductosPorAlmacenPK productosAlmacenPK = new ProductosPorAlmacenPK();
        productosAlmacenPK.setIdAlmacen(almacen.getIdAlmacen());
        productosAlmacenPK.setIdProducto(producto.getIdProducto());

        ProductosPorAlmacen productoAlmacen = productosPorAlmacenRepository.findById(productosAlmacenPK)
                .orElseGet(() -> productosPorAlmacenRepository.save(
                        ProductosPorAlmacen.builder()
                                .primaryKey(productosAlmacenPK)
                                .producto(producto)
                                .almacen(almacen)
                                .build()
                ));

        productoAlmacen.setCantidad(cantidad);

        return productosPorAlmacenRepository.save(productoAlmacen);
    }
}
