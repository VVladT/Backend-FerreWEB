package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.presentation.dto.request.RolRequest;
import pe.edu.utp.backendferreweb.presentation.dto.request.UsuarioRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.AuthResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.UsuarioResponse;
import pe.edu.utp.backendferreweb.service.auth.JwtService;

import java.util.stream.Collectors;

import static pe.edu.utp.backendferreweb.util.conversion.BlobConverter.utf8ToBlob;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {
    private final RolMapper rolMapper;
    private final PasswordEncoder passwordEncoder;

    public Usuario toEntity(UsuarioRequest request) {
        if (request == null) return null;

        return Usuario.builder()
                .username(request.getUser())
                .contrasena(utf8ToBlob(passwordEncoder.encode(request.getContrasena())))
                .dni(request.getDni())
                .nombre(request.getNombre())
                .apellidoPaterno(request.getApellidoPaterno())
                .apellidoMaterno(request.getApellidoMaterno())
                .direccion(request.getDireccion())
                .roles(request.getRoles().stream()
                        .map(rol -> rolMapper.toEntity(new RolRequest(rol)))
                        .collect(Collectors.toSet()))
                .build();
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) return null;

        return UsuarioResponse.builder()
                .id(usuario.getIdUsuario())
                .username(usuario.getUsername())
                .dni(usuario.getDni())
                .nombre(usuario.getNombre())
                .apellidoPat(usuario.getApellidoPaterno())
                .apellidoMat(usuario.getApellidoMaterno())
                .direccion(usuario.getDireccion())
                .rutaImagen(usuario.getRutaImagen())
                .roles(usuario.getRoles().stream().map(rolMapper::toResponse).toList())
                .fechaEliminacion(String.valueOf(usuario.getFechaEliminado()))
                .build();
    }
}