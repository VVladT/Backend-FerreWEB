package pe.edu.utp.backendferreweb.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.UsuarioMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.UsuarioRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.AuthResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.UsuarioResponse;
import pe.edu.utp.backendferreweb.service.UsuarioService;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final UsuarioMapper usuarioMapper;

    public AuthResponse login(UsuarioRequest request) {
        String user = request.getUser();
        String contrasena = request.getContrasena();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user, contrasena));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }
        Usuario usuario = usuarioService.loadUserByUsername(user);
        String jwtToken = jwtService.getToken(usuario);

        return new AuthResponse(jwtToken, usuarioMapper.toResponse(usuario));
    }

    public AuthResponse registrarUsuario(UsuarioRequest request) {
        UsuarioResponse usuarioResponse = usuarioService.registrarUsuario(request, null);
        String jwtToken = jwtService.getToken(request.getUser());

        return new AuthResponse(jwtToken, usuarioResponse);
    }
}
