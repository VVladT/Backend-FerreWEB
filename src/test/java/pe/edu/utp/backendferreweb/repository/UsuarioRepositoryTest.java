package pe.edu.utp.backendferreweb.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.utp.backendferreweb.model.Rol;
import pe.edu.utp.backendferreweb.model.Usuario;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static pe.edu.utp.backendferreweb.util.BlobConverter.utf8ToBlob;

@DataJpaTest
@RequiredArgsConstructor
public class UsuarioRepositoryTest {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Test
    void testSaveAndFindUsuario() {
        Rol rol = Rol.builder().tipo("ADMIN").build();
        rol = rolRepository.save(rol);

        Usuario usuario = Usuario.builder().dni("12345678").nombre("Juan")
                .apellidoPaterno("Pérez").apellidoMaterno("Gómez")
                .email("juan.perez@example.com").telefono("987654321")
                .direccion("Av. Siempre Viva 123")
                .contrasena(utf8ToBlob("contraseña"))
                .roles(new HashSet<>()).build();

        usuario.addRol(rol);

        Usuario savedUsuario = usuarioRepository.save(usuario);

        Usuario foundUsuario = usuarioRepository.findById(savedUsuario.getIdUsuario()).orElse(null);

        assert foundUsuario != null;
        assertEquals(foundUsuario.getDni(), "12345678", "The DNI do not match.");
        assertEquals(foundUsuario.getNombre(), "Juan", "The name do not match.");
        assertEquals(foundUsuario.getRoles().iterator().next().getIdRol(), rol.getIdRol(), "The rol do not match");
        assertEquals(foundUsuario.getRoles().iterator().next().getTipo(), ("ADMIN"), "The rol type do not match");
        assertTrue(usuarioRepository.existsByEmail("juan.perez@example.com"), "No exists an user with the email: " + "juan.perez@example.com");
        assertTrue(usuarioRepository.existsByTelefono("987654321"), "No exists an user with the telephone: " + "987654321");
    }

    @Test
    void updateCascadeTest() {
        Rol rol = Rol.builder().tipo("ADMIN").build();
        Rol savedRol = rolRepository.save(rol);
        savedRol.setTipo("NOADMIN");

        Usuario usuario = Usuario.builder().dni("12345678").nombre("Juan")
                .apellidoPaterno("Pérez").apellidoMaterno("Gómez")
                .email("juan.perez@example.com").telefono("987654321")
                .direccion("Av. Siempre Viva 123")
                .contrasena(utf8ToBlob("contraseña"))
                .roles(new HashSet<>()).build();

        usuario.addRol(savedRol);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        Usuario foundUsuario = usuarioRepository.findById(savedUsuario.getIdUsuario()).orElse(null);

        assert foundUsuario != null;
        assertEquals(foundUsuario.getRoles().iterator().next().getTipo(), "NOADMIN", "The rol not updated correctly.");
    }

    @Test
    void findByUsernameTest() {
        Rol rol = Rol.builder().tipo("USER").build();

        Usuario usuario1 = Usuario.builder().dni("12345678").nombre("Juan")
                .apellidoPaterno("Pérez").apellidoMaterno("Gómez")
                .email("juan.perez@example.com")
                .direccion("Av. Siempre Viva 123")
                .contrasena(utf8ToBlob("contraseña1"))
                .roles(new HashSet<>()).build();
        assertTrue(usuario1.addRol(rol));

        Usuario usuario2 = Usuario.builder().dni("22345678").nombre("Cristian")
                .apellidoPaterno("Fernandez").apellidoMaterno("Torres")
                .telefono("987654321")
                .direccion("Calle Las Mercedes 123")
                .contrasena(utf8ToBlob("contraseña2"))
                .roles(new HashSet<>()).build();
        assertTrue(usuario2.addRol(rol));

        Usuario usuario3 = Usuario.builder().dni("32345678").nombre("Julio")
                .apellidoPaterno("Pérez").apellidoMaterno("Aguilar")
                .email("julio.perez@example.com")
                .direccion("Mz. U, Lt. 12, Urb. La Victoria")
                .contrasena(utf8ToBlob("contraseña3"))
                .roles(new HashSet<>()).build();
        assertTrue(usuario3.addRol(rol));

        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);
        usuarioRepository.save(usuario3);

        Usuario usuario1found = usuarioRepository.findByEmail("juan.perez@example.com").orElse(null);
        assert usuario1found != null;
        assertEquals(usuario1found.getEmail(), "juan.perez@example.com");

        Usuario usuario2found = usuarioRepository.findByTelefono("987654321").orElse(null);
        assert usuario2found != null;
        assertEquals(usuario2found.getTelefono(), "987654321");

        Usuario usuario3found = usuarioRepository.findByEmail("julio.perez@example.com").orElse(null);
        assert usuario3found != null;
        assertEquals(usuario3found.getEmail(), "julio.perez@example.com");
    }
}
