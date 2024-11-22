package pe.edu.utp.backendferreweb.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.utp.backendferreweb.persistence.model.Rol;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.presentation.dto.request.UsuarioRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static pe.edu.utp.backendferreweb.util.conversion.BlobConverter.blobToUtf8;
import static pe.edu.utp.backendferreweb.util.conversion.BlobConverter.utf8ToBlob;

@ActiveProfiles("test")
@SpringBootTest
class UsuarioServiceTest {
    @Autowired
    UsuarioService usuarioService;

    @Test
    void loadUserByUsername() {
        UsuarioRequest request = UsuarioRequest.builder()
                .dni("12345678")
                .nombre("Vladimir")
                .user("vlad.tunoque@example.com")
                .contrasena("pokiTrop123")
                .apellidoPaterno("Tuñoque")
                .apellidoMaterno("Morante")
                .roles(List.of("ADMIN"))
                .build();

        usuarioService.registrarUsuario(request);

        UserDetails usuarioFound = usuarioService.loadUserByUsername("vlad.tunoque@example.com");
        assertEquals(request.getUser(), usuarioFound.getUsername());
        assertEquals(request.getRoles().getFirst(), usuarioFound.getAuthorities().iterator().next().getAuthority());
    }
}