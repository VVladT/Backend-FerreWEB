package pe.edu.utp.backendferreweb.presentation.controller;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.UsuarioMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.UsuarioRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.UsuarioResponse;
import pe.edu.utp.backendferreweb.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @SneakyThrows
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> obtenerUsuarioAutenticado(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) throw new AuthException("No hay usuario autenticado.");

        return ResponseEntity.ok(usuarioMapper.toResponse(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> obtenerUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrarUsuario(@RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@PathVariable Integer id,
                                                             @RequestBody UsuarioRequest request) {
        UsuarioResponse response = usuarioService.editarUsuario(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id, @RequestBody String motivo) {
        usuarioService.eliminarUsuario(id, motivo);
        return ResponseEntity.noContent().build();
    }
}
