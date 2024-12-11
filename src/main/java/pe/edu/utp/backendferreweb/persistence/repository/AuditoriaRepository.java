package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.backendferreweb.persistence.model.AuditoriaLog;

public interface AuditoriaRepository extends JpaRepository<AuditoriaLog, Integer> {
}
