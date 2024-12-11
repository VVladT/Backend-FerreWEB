package pe.edu.utp.backendferreweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.utp.backendferreweb.persistence.model.Almacen;

import java.util.List;
import java.util.Optional;

public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {
    @Query("""
           SELECT a FROM Almacen a
           WHERE a.fechaEliminado IS NULL""")
    List<Almacen> findAllActive();

    @Query("""
           SELECT a FROM Almacen a
           WHERE a.idAlmacen = :id
           AND a.fechaEliminado IS NULL""")
    Almacen findActiveById(Integer id);

    @Query("""
           SELECT
            CASE
                WHEN COUNT(a) > 0
                THEN true
                ELSE false
            END
           FROM Almacen a
           WHERE a.nombre = :nombre
           AND a.fechaEliminado IS NULL""")
    boolean existsByNombre(String nombre);

    @Query("""
           SELECT a FROM Almacen a
           WHERE a.nombre = :nombre
           AND a.fechaEliminado IS NULL""")
    Optional<Almacen> findByNombre(String nombre);

    @Query("""
           SELECT
            CASE
                WHEN EXISTS
                    (SELECT 1
                    FROM ProductosPorAlmacen pa
                    JOIN pa.producto p
                    WHERE pa.almacen.idAlmacen = :id
                        AND p.fechaEliminado IS NULL)
                    THEN TRUE
                    ELSE FALSE
            END""")
    boolean isAssociatedWithProduct(Integer id);
}
