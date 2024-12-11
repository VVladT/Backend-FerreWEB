package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.backendferreweb.persistence.model.EstadoCompra;

import java.util.Optional;

public interface EstadoCompraRepository extends JpaRepository<EstadoCompra, Integer> {
    Optional<EstadoCompra> findByEstado(String nombre);
}
