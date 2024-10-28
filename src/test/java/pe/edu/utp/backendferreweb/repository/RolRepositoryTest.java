package pe.edu.utp.backendferreweb.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.utp.backendferreweb.model.Rol;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class RolRepositoryTest {

    @Autowired
    private RolRepository rolRepository;

    @Test
    void testSaveAndFind() {
        Rol rol = Rol.builder().tipo("ADMIN").build();
        Rol savedRol = rolRepository.save(rol);
        Rol foundRol = rolRepository.findById(savedRol.getIdRol()).orElse(null);

        assert foundRol != null;
        assertEquals(savedRol.getIdRol(), foundRol.getIdRol());
        assertEquals(rol.getIdRol(), foundRol.getIdRol());
        assertEquals("ADMIN", foundRol.getTipo());
    }
}
