package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.Rol;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.presentation.dto.request.RolRequest;
import pe.edu.utp.backendferreweb.presentation.dto.request.UsuarioRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.UsuarioResponse;

import java.util.Set;
import java.util.stream.Collectors;

import static pe.edu.utp.backendferreweb.util.conversion.BlobConverter.utf8ToBlob;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {
    private final RolMapper rolMapper;
    private final PasswordEncoder passwordEncoder;

    public Usuario toEntity(UsuarioRequest request) {
        if (request == null) return null;
        Set<Rol> roles = null;
        if (request.getRoles() != null) {
            roles = request.getRoles().stream()
                    .map(rol -> rolMapper.toEntity(new RolRequest(rol)))
                    .collect(Collectors.toSet());
        }

        return Usuario.builder()
                .username(request.getUser())
                .contrasena(utf8ToBlob(passwordEncoder.encode(request.getContrasena())))
                .dni(request.getDni())
                .nombre(request.getNombre())
                .apellidoPaterno(request.getApellidoPaterno())
                .apellidoMaterno(request.getApellidoMaterno())
                .direccion(request.getDireccion())
                .roles(roles)
                .build();
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) return null;

        return UsuarioResponse.builder()
                .id(usuario.getIdUsuario())
                .nombreCompleto(String.format("%s %s %s",
                        usuario.getNombre(),
                        usuario.getApellidoPaterno(),
                        usuario.getApellidoMaterno()))
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
