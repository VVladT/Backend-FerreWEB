package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.backendferreweb.persistence.model.Almacen;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT u FROM Usuario u WHERE u.fechaEliminado IS NULL")
    List<Almacen> findAllActive();

    @Query("SELECT u FROM Usuario u WHERE u.idUsuario = :id AND u.fechaEliminado IS NULL")
    Almacen findActiveById(@Param("id") Integer id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.email = :email AND u.fechaEliminado IS NULL")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.telefono = :telefono AND u.fechaEliminado IS NULL")
    boolean existsByTelefono(@Param("telefono") String telefono);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.fechaEliminado IS NULL")
    Optional<Usuario> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.telefono = :telefono AND u.fechaEliminado IS NULL")
    Optional<Usuario> findByTelefono(@Param("telefono") String telefono);
}