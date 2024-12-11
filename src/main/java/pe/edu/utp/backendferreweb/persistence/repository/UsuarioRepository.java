package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.backendferreweb.persistence.model.Rol;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT u.roles FROM Usuario u WHERE u.idUsuario = :id")
    List<Rol> findRolesByIdUsuario(@Param("id") Integer id);

    @Query("SELECT u FROM Usuario u WHERE u.fechaEliminado IS NULL")
    List<Usuario> findAllActive();

    @Query("SELECT u FROM Usuario u WHERE u.idUsuario = :id AND u.fechaEliminado IS NULL")
    Usuario findActiveById(@Param("id") Integer id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.username = :username AND u.fechaEliminado IS NULL")
    boolean existsByUsername(@Param("username") String username);

    @Query("SELECT u FROM Usuario u WHERE u.username = :username AND u.fechaEliminado IS NULL")
    Optional<Usuario> findByUsername(@Param("username") String username);
}
