package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.backendferreweb.persistence.model.TipoEntrega;

public interface TipoEntregaRepository extends JpaRepository<TipoEntrega, Integer> {
    boolean existsByTipo(String tipo);
}
