package pe.edu.utp.backendferreweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.backendferreweb.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
