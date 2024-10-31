package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.backendferreweb.persistence.model.ProductosPorAlmacen;
import pe.edu.utp.backendferreweb.persistence.model.ProductosPorAlmacenPK;

public interface ProductosPorAlmacenRepository extends JpaRepository<ProductosPorAlmacen, ProductosPorAlmacenPK> {
}
