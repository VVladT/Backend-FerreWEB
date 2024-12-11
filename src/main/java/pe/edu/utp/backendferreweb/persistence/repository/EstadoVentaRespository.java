package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.backendferreweb.persistence.model.EstadoVenta;

import java.util.Optional;

public interface EstadoVentaRespository extends JpaRepository<EstadoVenta, Integer> {

    Optional<EstadoVenta> findByEstado(String nombre);
}
