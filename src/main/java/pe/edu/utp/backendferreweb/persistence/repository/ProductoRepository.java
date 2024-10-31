package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.backendferreweb.persistence.model.Producto;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    @Query("SELECT p FROM Producto p WHERE p.fechaEliminado IS NULL")
    List<Producto> findAllActive();

    @Query("SELECT p FROM Producto p WHERE p.idProducto = :id AND p.fechaEliminado IS NULL")
    Producto findActiveById(@Param("id") Integer id);
}