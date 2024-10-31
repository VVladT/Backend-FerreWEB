package pe.edu.utp.backendferreweb.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.service.UsuarioService;

import java.util.HashSet;
import java.util.regex.Pattern;

import static pe.edu.utp.backendferreweb.util.conversion.BlobConverter.utf8ToBlob;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public String login(String user, String contrasena) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user, contrasena));
        UserDetails usuario = usuarioService.loadUserByUsername(user);
        return jwtService.getToken(usuario);
    }

    public String register(String user, String contrasena, String dni,
                           String nombre, String apellidoPat, String apellidoMat) {
        Usuario usuario = Usuario.builder()
                .dni(dni)
                .email(isEmail(user) ? user : null)
                .telefono(isTelefono(user) ? user : null)
                .nombre(nombre)
                .apellidoPaterno(apellidoPat)
                .apellidoMaterno(apellidoMat)
                .contrasena(utf8ToBlob(passwordEncoder.encode(contrasena)))
                .roles(new HashSet<>())
                .build();

        usuarioService.register(usuario);
        return jwtService.getToken(usuario);
    }

    private boolean isTelefono(String username) {
        String regex = "^9\\d{8}$";
        return Pattern.matches(regex, username);
    }

    private boolean isEmail(String username) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, username);
    }
}
