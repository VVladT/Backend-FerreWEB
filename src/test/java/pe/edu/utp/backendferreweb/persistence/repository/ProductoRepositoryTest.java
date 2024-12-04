package pe.edu.utp.backendferreweb.persistence.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.utp.backendferreweb.persistence.model.*;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class ProductoRepositoryTest {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private AlmacenRepository almacenRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void testProductos() {
        Categoria categoria = Categoria.builder()
                .nombre("Categoria1")
                .build();

        Almacen almacen = Almacen.builder()
                .nombre("Almacen1")
                .direccion("Calle siempre viva")
                .productosPorAlmacen(new HashSet<>())
                .build();

        Producto producto = Producto.builder()
                .nombre("Producto1")
                .categoria(categoria)
                .stock(10)
                .almacenes(new HashSet<>())
                .unidadesPermitidas(new HashSet<>())
                .build();

        Unidad unidad = Unidad.builder().nombre("Unidad").build();

        UnidadesPorProducto unidadesPorProducto = UnidadesPorProducto.builder()
                .primaryKey(new UnidadesPorProductoPK())
                .unidad(unidad)
                .producto(producto)
                .precio(10.50)
                .build();

        ProductosPorAlmacen productosPorAlmacen = ProductosPorAlmacen.builder()
                .primaryKey(new ProductosPorAlmacenPK())
                .almacen(almacen)
                .producto(producto)
                .cantidad(10)
                .build();

        producto.getUnidadesPermitidas().add(unidadesPorProducto);
        producto.getAlmacenes().add(productosPorAlmacen);


        productoRepository.save(producto);

        Producto productoFound = productoRepository.findById(producto.getIdProducto())
                .orElse(null);

        assertEquals(productoFound.getAlmacenes().iterator().next().getCantidad(), 10);
        assertEquals(productoFound.getUnidadesPermitidas().iterator().next().getPrecio(), 10.50);
    }
}