package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.backendferreweb.persistence.model.OrdenCompra;

import java.util.List;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {
}
