package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.backendferreweb.persistence.model.MetodoPago;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {
    boolean existsByNombre(String nombre);
}
