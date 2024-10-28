package pe.edu.utp.backendferreweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.backendferreweb.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByEmail(String email);
    boolean existsByTelefono(String telefono);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTelefono(String telefono);
}
