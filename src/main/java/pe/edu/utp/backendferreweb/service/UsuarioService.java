package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Rol;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.persistence.model.enums.ERol;
import pe.edu.utp.backendferreweb.persistence.repository.UsuarioRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final RolService rolService;

    @Transactional
    @Override
    public Usuario loadUserByUsername(String username) throws UsernameNotFoundException {
        if (isEmail(username)) {
            return usuarioRepository.findByEmail(username).orElseThrow(
                    () -> new EntityNotFoundException("El email no ha sido encontrado.")
            );
        }

        if (isTelefono(username)) {
            return usuarioRepository.findByTelefono(username).orElseThrow(
                    () -> new EntityNotFoundException("El telefono no ha sido encontrado.")
            );
        }

        throw new EntityNotFoundException("El nombre de usuario no ha sido encontrado.");
    }

    @Transactional
    public synchronized Usuario register(@NonNull Usuario usuario) {
        if (!existsByUsername(usuario.getUsername())) {
            Rol rol = rolService.obtenerPorTipo(ERol.USUARIO);

            if (rol == null) {
                rol = Rol.builder().tipo(ERol.USUARIO.name()).build();
            }

            usuario.addRol(rol);
            return usuarioRepository.save(usuario);
        } else {
            throw new EntityExistsException("El usuario ya está registrado.");
        }
    }

    private boolean existsByUsername(String username) {
        return usuarioRepository.existsByEmail(username) || usuarioRepository.existsByTelefono(username);
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
