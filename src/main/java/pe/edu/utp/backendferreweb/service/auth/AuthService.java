package pe.edu.utp.backendferreweb.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.presentation.dto.request.UsuarioRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.AuthResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.RolResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.UsuarioResponse;
import pe.edu.utp.backendferreweb.service.UsuarioService;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public AuthResponse login(UsuarioRequest request) {
        String user = request.getUser();
        String contrasena = request.getContrasena();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user, contrasena));
        Usuario usuario = usuarioService.loadUserByUsername(user);

        return buildAuthResponse(usuario);
    }


    public AuthResponse register(UsuarioRequest request) {
        Usuario usuario = usuarioService.registrarUsuario(request);

        return buildAuthResponse(usuario);
    }

    private AuthResponse buildAuthResponse(Usuario usuario) {
        return AuthResponse.builder()
                .token(jwtService.getToken(usuario))
                .usuario(UsuarioResponse.builder()
                        .id(usuario.getIdUsuario())
                        .username(usuario.getUsername())
                        .dni(usuario.getDni())
                        .nombre(usuario.getNombre())
                        .apellidoPat(usuario.getApellidoPaterno())
                        .apellidoMat(usuario.getApellidoMaterno())
                        .rutaImagen(usuario.getRutaImagen())
                        .fechaEliminacion(usuario.getFechaEliminado() != null ? usuario.getFechaEliminado().format(formatter) : "")
                        .roles(usuario.getRoles().stream().map(
                                rol -> RolResponse.builder()
                                        .idRol(rol.getIdRol())
                                        .tipo(rol.getTipo())
                                        .rutaImagen(rol.getRutaImagen())
                                        .build()
                        ).toList())
                        .build())
                .build();
    }
}
