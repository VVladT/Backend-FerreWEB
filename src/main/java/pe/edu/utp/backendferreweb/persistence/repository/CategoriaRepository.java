package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.backendferreweb.persistence.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    @Query("SELECT c FROM Categoria c WHERE c.fechaEliminado IS NULL")
    List<Categoria> findAllActive();

    @Query("SELECT c FROM Categoria c WHERE c.idCategoria = :id AND c.fechaEliminado IS NULL")
    Categoria findActiveById(@Param("id") Integer id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Categoria c WHERE c.nombre = :nombre AND c.fechaEliminado IS NULL")
    boolean existsByNombre(String nombre);

    @Query("SELECT c FROM Categoria c WHERE c.nombre = :nombre AND c.fechaEliminado IS NULL")
    Optional<Categoria> findByNombre(String nombre);
}
