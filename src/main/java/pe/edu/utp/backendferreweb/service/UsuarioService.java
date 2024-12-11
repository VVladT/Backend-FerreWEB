package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.persistence.model.Rol;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.persistence.model.enums.EAuditAction;
import pe.edu.utp.backendferreweb.persistence.model.enums.ERol;
import pe.edu.utp.backendferreweb.persistence.repository.UsuarioRepository;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.UsuarioMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.UsuarioRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.UsuarioResponse;
import pe.edu.utp.backendferreweb.service.external.StorageService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static pe.edu.utp.backendferreweb.util.conversion.BlobConverter.utf8ToBlob;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final RolService rolService;
    private final StorageService storageService;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;

    public List<UsuarioResponse> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    public synchronized UsuarioResponse registrarUsuario(UsuarioRequest request) {
        String username = request.getUser();
        String dni = request.getDni();
        String nombre = request.getNombre();
        String apellidoPaterno = request.getApellidoPaterno();
        String apellidoMaterno = request.getApellidoMaterno();
        String contrasena = request.getContrasena();

        if (username == null) throw new IllegalArgumentException("El username no puede ser nulo");
        if (username.isBlank()) throw new IllegalArgumentException("El username no puede estar vacío");
        if (!isEmail(username)) throw new IllegalArgumentException("El username debe ser un email válido");
        if (dni == null) throw new IllegalArgumentException("El dni no puede ser nulo");
        if (dni.isBlank()) throw new IllegalArgumentException("El dni no puede estar vacío");
        if (nombre == null) throw new IllegalArgumentException("El nombre no puede ser nulo");
        if (nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío");
        if (apellidoPaterno == null) throw new IllegalArgumentException("El apellido paterno no puede ser nulo");
        if (apellidoPaterno.isBlank()) throw new IllegalArgumentException("El apellido paterno no puede estar vacío");
        if (apellidoMaterno == null) throw new IllegalArgumentException("El apellido materno no puede ser nulo");
        if (apellidoMaterno.isBlank()) throw new IllegalArgumentException("El apellido materno no puede estar vacío");
        if (contrasena == null) throw new IllegalArgumentException("La contraseña no puede ser nula");
        if (contrasena.isBlank()) throw new IllegalArgumentException("La contraseña no puede estar vacía");
        if (usuarioRepository.existsByUsername(username))
            throw new EntityExistsException("Ya existe un usuario registrado como: " + username);

        Usuario usuario = usuarioMapper.toEntity(request);

        if (usuario.getRoles() == null) usuario.setRoles(new HashSet<>());
        if (usuario.getRoles().isEmpty()) {
            Rol rolPorDefecto = rolService.obtenerEntidadPorTipo(ERol.USUARIO.name());

            usuario.addRol(rolPorDefecto);
        }

        usuario = usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(usuario);
    }

    public UsuarioResponse editarUsuario(Integer id, UsuarioRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        if (usuario.getIdUsuario().equals(id))
            throw new IllegalStateException("No puedes editar tu propio usuario.");

        Usuario usuarioParaActualizar = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El usuario no existe."));

        String nuevoUsername = request.getUser();
        String nuevoDni = request.getDni();
        String nuevoNombre = request.getNombre();
        String nuevoApellidoPaterno = request.getApellidoPaterno();
        String nuevoApellidoMaterno = request.getApellidoMaterno();
        String nuevaContrasena = request.getContrasena();
        String nuevaDireccion = request.getDireccion();
        List<String> nuevosRoles = request.getRoles();

        if (!usuarioParaActualizar.getUsername().equals(nuevoUsername) &&
                usuarioRepository.existsByUsername(nuevoUsername))
            throw new EntityExistsException("Ya existe un usuario registrado como: " + nuevoUsername);

        if (nuevoUsername != null) {
            if (nuevoUsername.isBlank())
                throw new IllegalArgumentException("El nuevo username no puede estar vacío");
            if (!isEmail(nuevoUsername))
                throw new IllegalArgumentException("El nuevo username debe ser un email válido");

            usuarioParaActualizar.setUsername(nuevoUsername);
        }

        if (nuevoDni != null) {
            if (nuevoDni.isBlank())
                throw new IllegalArgumentException("El nuevo dni no puede estar vacío");

            usuarioParaActualizar.setDni(nuevoDni);
        }

        if (nuevoNombre != null) {
            if (nuevoNombre.isBlank())
                throw new IllegalArgumentException("El nombre no puede estar vacío");

            usuarioParaActualizar.setNombre(nuevoNombre);
        }

        if (nuevoApellidoPaterno != null) {
            if (nuevoApellidoPaterno.isBlank())
                throw new IllegalArgumentException("El apellido paterno no puede estar vacío");

            usuarioParaActualizar.setApellidoPaterno(nuevoApellidoPaterno);
        }

        if (nuevoApellidoMaterno != null) {
            if (nuevoApellidoMaterno.isBlank())
                throw new IllegalArgumentException("El apellido materno no puede estar vacío");

            usuarioParaActualizar.setApellidoMaterno(nuevoApellidoMaterno);
        }

        if (nuevaContrasena != null) {
            if (nuevaContrasena.isBlank())
                throw new IllegalArgumentException("La nueva contraseña no puede estar vacía");

            usuarioParaActualizar.setContrasena(utf8ToBlob(passwordEncoder.encode(nuevaContrasena)));
        }

        if (nuevaDireccion != null) {
            if (nuevaDireccion.isBlank())
                throw new IllegalArgumentException("La nueva dirección no puede estar vacía");

            usuarioParaActualizar.setDireccion(nuevaDireccion);
        }

        if (nuevosRoles != null && !nuevosRoles.isEmpty()) {
            Set<Rol> roles = nuevosRoles.stream()
                    .map(rolService::obtenerEntidadPorTipo)
                    .collect(Collectors.toSet());

            usuarioParaActualizar.setRoles(roles);
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuarioParaActualizar);

        return usuarioMapper.toResponse(usuarioActualizado);
    }

    public void eliminarUsuario(Integer id, String motivo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        if (usuario.getIdUsuario().equals(id))
            throw new IllegalStateException("No puedes eliminar tu propio usuario desde la pantalla de administración.");

        Usuario usuarioParaEliminar = usuarioRepository.findActiveById(id);

        if (usuarioParaEliminar == null)
            throw new EntityNotFoundException("El usuario no está activo.");
        if (usuarioParaEliminar.getRoles().size() > 1)
            throw new IllegalStateException("Debe quitar los roles extra antes de eliminar a un empleado.");

        usuarioParaEliminar.setFechaEliminado(LocalDateTime.now());

        auditoriaService.registerAudit(id, "Usuario", EAuditAction.DELETE.name(), motivo);

        usuarioRepository.save(usuarioParaEliminar);
    }


    public UsuarioResponse obtenerPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("No existe usuario con id: " + id)
                );

        return usuarioMapper.toResponse(usuario);
    }

    @Override
    public Usuario loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException("No existe registrado ningún usuario con el username: " + username)
                );
    }

    private boolean isEmail(String username) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, username);
    }
}
