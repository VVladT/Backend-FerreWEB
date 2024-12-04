package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.backendferreweb.persistence.model.Proveedor;

import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    @Query("SELECT p FROM Proveedor p WHERE p.fechaEliminado IS NULL")
    List<Proveedor> findAllActive();

    @Query("SELECT p FROM Proveedor p WHERE p.idProveedor = :id AND p.fechaEliminado IS NULL")
    Proveedor findActiveById(@Param("id") Integer id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Proveedor p WHERE p.ruc = :ruc AND p.fechaEliminado IS NULL")
    boolean existsActiveByRuc(@Param("ruc") String ruc);

    @Query("SELECT p FROM Proveedor p WHERE p.ruc = :ruc AND p.fechaEliminado IS NULL")
    Proveedor findActiveByRuc(@Param("ruc") String ruc);
}
