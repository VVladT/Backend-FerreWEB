package pe.edu.utp.backendferreweb.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Almacen;
import pe.edu.utp.backendferreweb.persistence.model.Producto;
import pe.edu.utp.backendferreweb.persistence.model.ProductosPorAlmacen;
import pe.edu.utp.backendferreweb.persistence.model.ProductosPorAlmacenPK;
import pe.edu.utp.backendferreweb.persistence.repository.ProductosPorAlmacenRepository;
import pe.edu.utp.backendferreweb.presentation.dto.request.AlmacenCantidadRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductosPorAlmacenService {
    private final ProductosPorAlmacenRepository productosPorAlmacenRepository;
    private final AlmacenService almacenService;

    public ProductosPorAlmacen registrarProductosPorAlmacen(Producto producto, AlmacenCantidadRequest request) {
        String nombreAlmacen = request.getNombreAlmacen();
        Double cantidad = request.getCantidadProductos();

        if (nombreAlmacen == null) throw new IllegalArgumentException("El nombre del almacén no puede ser nulo");
        if (nombreAlmacen.isBlank()) throw new IllegalArgumentException("El nombre del almacén no puede estar vacío");
        if (cantidad == null) throw new IllegalArgumentException("La cantidad no puede ser nula");
        if (cantidad < 0) throw new IllegalArgumentException("No puede haber productos negativos en almacén");

        if (producto.getIdProducto() == null)
            throw new IllegalArgumentException("Se intentó asociar un almacén con un producto no existente");

        Almacen almacen = almacenService.obtenerPorNombre(nombreAlmacen);

        ProductosPorAlmacenPK productosAlmacenPK = new ProductosPorAlmacenPK();
        productosAlmacenPK.setIdAlmacen(almacen.getIdAlmacen());
        productosAlmacenPK.setIdProducto(producto.getIdProducto());

        ProductosPorAlmacen productoAlmacen = ProductosPorAlmacen.builder()
                .primaryKey(productosAlmacenPK)
                .producto(producto)
                .almacen(almacen)
                .cantidad(cantidad)
                .build();

        return productosPorAlmacenRepository.save(productoAlmacen);
    }

    public void actualizarAlmacenPorProducto(ProductosPorAlmacen almacen) {
        productosPorAlmacenRepository.save(almacen);
    }

    public ProductosPorAlmacen obtenerEntidadPorId(ProductosPorAlmacenPK almacenesPK) {
        return productosPorAlmacenRepository.findById(almacenesPK)
                .orElse(null);
    }
}
