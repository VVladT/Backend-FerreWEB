package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.utp.backendferreweb.persistence.model.Unidad;

import java.util.List;
import java.util.Optional;

public interface UnidadRepository extends JpaRepository<Unidad, Integer> {
    @Query("SELECT u FROM Unidad u WHERE u.fechaEliminado IS NULL")
    List<Unidad> findAllActive();

    @Query("SELECT u FROM Unidad u WHERE u.idUnidad = :id AND u.fechaEliminado IS NULL")
    Unidad findActiveById(Integer id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Unidad u WHERE u.nombre = :nombre AND u.fechaEliminado IS NULL")
    boolean existsByNombre(String nombre);

    @Query("SELECT u FROM Unidad u WHERE u.nombre = :nombre AND u.fechaEliminado IS NULL")
    Optional<Unidad> findByNombre(String nombre);

    @Query("""
           SELECT
            CASE
                WHEN EXISTS
                    (SELECT 1
                    FROM UnidadesPorProducto up
                    JOIN up.producto p
                    WHERE up.unidad.idUnidad = :id
                        AND p.fechaEliminado IS NULL)
                    THEN TRUE
                    ELSE FALSE
            END""")
    boolean isAssociatedWithProduct(Integer id);
}
