package pe.edu.utp.backendferreweb.presentation.controller;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.presentation.dto.request.UsuarioRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.RolResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.UsuarioResponse;
import pe.edu.utp.backendferreweb.service.UsuarioService;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @SneakyThrows
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> obtenerUsuarioAutenticado(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) throw new AuthException("No hay usuario autenticado.");

        UsuarioResponse response = UsuarioResponse.builder()
                .id(usuario.getIdUsuario())
                .dni(usuario.getDni())
                .nombre(usuario.getNombre())
                .apellidoPat(usuario.getApellidoPaterno())
                .apellidoMat(usuario.getApellidoMaterno())
                .username(usuario.getUsername())
                .fechaEliminacion(usuario.getFechaEliminado() != null ? usuario.getFechaEliminado().format(formatter) : "")
                .roles(usuario.getRoles().stream().map(
                        rol -> RolResponse.builder()
                                    .idRol(rol.getIdRol())
                                    .tipo(rol.getTipo())
                                    .rutaImagen(rol.getRutaImagen())
                                    .build()
                ).toList())
                .rutaImagen(usuario.getRutaImagen())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> obtenerUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrarUsuario(@RequestPart UsuarioRequest request,
                                                            @RequestPart(required = false) MultipartFile imagen) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(request, imagen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@PathVariable Integer id,
                                                             @RequestPart(required = false) MultipartFile imagen,
                                                             @RequestPart UsuarioRequest request) {
        UsuarioResponse response = usuarioService.editarUsuario(id, request, imagen);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
