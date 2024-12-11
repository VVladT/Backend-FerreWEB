package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.backendferreweb.presentation.dto.request.UsuarioRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.AuthResponse;
import pe.edu.utp.backendferreweb.service.auth.AuthService;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidLogin;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody
                                              @Validated(ValidLogin.class)
                                              UsuarioRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody
                                                 @Validated(ValidCreacion.class)
                                                 UsuarioRequest request) {
        return ResponseEntity.ok(authService.registrarUsuario(request));
    }
}