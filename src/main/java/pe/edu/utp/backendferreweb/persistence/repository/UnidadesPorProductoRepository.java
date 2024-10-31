package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.backendferreweb.persistence.model.UnidadesPorProducto;
import pe.edu.utp.backendferreweb.persistence.model.UnidadesPorProductoPK;

public interface UnidadesPorProductoRepository extends JpaRepository<UnidadesPorProducto, UnidadesPorProductoPK> {
}
