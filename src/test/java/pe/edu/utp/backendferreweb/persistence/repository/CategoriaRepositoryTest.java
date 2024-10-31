package pe.edu.utp.backendferreweb.persistence.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.utp.backendferreweb.persistence.model.Categoria;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
public class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void testSaveAndFind() {
        Categoria categoria = Categoria.builder().nombre("Electrónica").descripcion("Productos electrónicos").build();
        Categoria savedCategoria = categoriaRepository.save(categoria);
        Categoria foundCategoria = categoriaRepository.findById(savedCategoria.getIdCategoria()).orElse(null);

        assert foundCategoria != null;
        assertEquals(savedCategoria.getIdCategoria(), foundCategoria.getIdCategoria());
        assertEquals(categoria.getIdCategoria(), foundCategoria.getIdCategoria());
        assertEquals("Electrónica", foundCategoria.getNombre());
        assertEquals("Productos electrónicos", foundCategoria.getDescripcion());
    }
}