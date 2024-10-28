package pe.edu.utp.backendferreweb.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.auth.dto.AuthResponse;
import pe.edu.utp.backendferreweb.auth.dto.LoginRequest;
import pe.edu.utp.backendferreweb.auth.dto.RegisterRequest;
import pe.edu.utp.backendferreweb.model.Usuario;
import pe.edu.utp.backendferreweb.service.UsuarioService;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import static pe.edu.utp.backendferreweb.util.BlobConverter.utf8ToBlob;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUser(), request.getContrasena()));
        UserDetails usuario = usuarioService.loadUserByUsername(request.getUser());
        String token = jwtService.getToken(usuario);
        return AuthResponse.builder()
                .token(token)
                .build();

    }

    public AuthResponse register(RegisterRequest request) {
        Usuario usuario = Usuario.builder()
                .dni(request.getDni())
                .email(isEmail(request.getUser()) ? request.getUser() : null)
                .telefono(isTelefono(request.getUser()) ? request.getUser() : null)
                .nombre(request.getNombre())
                .apellidoPaterno(request.getApellidoPaterno())
                .apellidoMaterno(request.getApellidoMaterno())
                .contrasena(utf8ToBlob(passwordEncoder.encode(request.getContrasena())))
                .roles(new HashSet<>())
                .build();

        usuarioService.register(usuario);
        String token = jwtService.getToken(usuario);
        return AuthResponse.builder()
                .token(token)
                .build();

    }

    public List<Usuario> list() {return usuarioService.listar();}

    private boolean isTelefono(String username) {
        String regex = "^9\\d{8}$";
        return Pattern.matches(regex, username);
    }

    private boolean isEmail(String username) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, username);
    }
}
