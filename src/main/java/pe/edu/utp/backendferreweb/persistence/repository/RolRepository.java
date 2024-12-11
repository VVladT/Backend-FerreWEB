package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.utp.backendferreweb.persistence.model.Rol;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByTipo(String name);

    boolean existsByTipo(String tipo);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u JOIN u.roles r WHERE r.idRol = :id")
    boolean isAssociated(Integer id);
}
