package pe.edu.utp.backendferreweb.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.utp.backendferreweb.presentation.dto.request.UsuarioRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                .roles(new ArrayList<>())
                .build();

        usuarioService.registrarUsuario(request);

        UserDetails usuarioFound = usuarioService.loadUserByUsername("vlad.tunoque@example.com");
        assertEquals(request.getUser(), usuarioFound.getUsername());
    }
}