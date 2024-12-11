package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.backendferreweb.persistence.model.DetalleCompra;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {
}
