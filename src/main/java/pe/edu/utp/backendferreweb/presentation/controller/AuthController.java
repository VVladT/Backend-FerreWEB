package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.backendferreweb.service.UsuarioService;
import pe.edu.utp.backendferreweb.service.auth.AuthService;
import pe.edu.utp.backendferreweb.presentation.dto.response.AuthResponse;
import pe.edu.utp.backendferreweb.presentation.dto.request.UsuarioRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UsuarioRequest request) {

        return ResponseEntity.ok(authService
                .login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(authService
                .register(request));
    }
}