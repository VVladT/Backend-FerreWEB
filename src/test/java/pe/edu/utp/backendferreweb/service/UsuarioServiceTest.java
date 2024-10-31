package pe.edu.utp.backendferreweb.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.utp.backendferreweb.persistence.model.Rol;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static pe.edu.utp.backendferreweb.util.conversion.BlobConverter.blobToUtf8;
import static pe.edu.utp.backendferreweb.util.conversion.BlobConverter.utf8ToBlob;

@ActiveProfiles("test")
@SpringBootTest
class UsuarioServiceTest {
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void loadUserByUsername() {
        Usuario usuario = Usuario.builder()
                .dni("12345678")
                .nombre("Vladimir")
                .email("vlad.tunoque@example.com")
                .contrasena(utf8ToBlob(passwordEncoder.encode("pokiTrop123")))
                .apellidoPaterno("Tuñoque")
                .apellidoMaterno("Morante")
                .roles(Set.of(Rol.builder().tipo("ADMIN").build()))
                .build();

        usuarioService.register(usuario);

        UserDetails usuarioFound = usuarioService.loadUserByUsername("vlad.tunoque@example.com");
        assertEquals(usuario.getEmail(), usuarioFound.getUsername());
        assertEquals(blobToUtf8(usuario.getContrasena()), usuarioFound.getPassword());
        assertEquals(usuario.getRoles().iterator().next().getTipo(), usuarioFound.getAuthorities().iterator().next().getAuthority());
    }
}