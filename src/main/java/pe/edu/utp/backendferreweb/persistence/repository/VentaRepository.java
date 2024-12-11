package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.backendferreweb.persistence.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
}
