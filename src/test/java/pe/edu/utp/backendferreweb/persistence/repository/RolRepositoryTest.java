package pe.edu.utp.backendferreweb.persistence.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.utp.backendferreweb.persistence.model.Rol;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
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
