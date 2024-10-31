package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.backendferreweb.persistence.model.Almacen;
import pe.edu.utp.backendferreweb.persistence.model.Rol;

import java.util.List;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    @Query("SELECT r FROM Rol r WHERE r.fechaEliminado IS NULL")
    List<Almacen> findAllActive();

    @Query("SELECT r FROM Rol r WHERE r.idRol = :id AND r.fechaEliminado IS NULL")
    Almacen findActiveById(@Param("id") Integer id);
}
