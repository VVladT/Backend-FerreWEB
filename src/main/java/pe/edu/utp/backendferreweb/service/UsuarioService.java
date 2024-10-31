package pe.edu.utp.backendferreweb.service;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.persistence.repository.UsuarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    @Transactional
    @Override
    public Usuario loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username).
                orElse(usuarioRepository.findByTelefono(username)
                        .orElse(null));
    }

    public Usuario register(@NonNull Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
