package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.persistence.model.Rol;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.persistence.model.enums.ERol;
import pe.edu.utp.backendferreweb.persistence.repository.UsuarioRepository;
import pe.edu.utp.backendferreweb.presentation.dto.request.UsuarioRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.RolResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.UsuarioResponse;
import pe.edu.utp.backendferreweb.util.encoder.Argon2PasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static pe.edu.utp.backendferreweb.util.conversion.BlobConverter.utf8ToBlob;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final RolService rolService;
    private final StorageService storageService;
    private final Argon2PasswordEncoder passwordEncoder;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public List<UsuarioResponse> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuario -> UsuarioResponse.builder()
                        .id(usuario.getIdUsuario())
                        .dni(usuario.getDni())
                        .nombre(usuario.getNombre())
                        .apellidoPat(usuario.getApellidoPaterno())
                        .apellidoMat(usuario.getApellidoMaterno())
                        .username(usuario.getUsername())
                        .roles(usuario.getRoles().stream().map(
                                rol -> RolResponse.builder()
                                        .idRol(rol.getIdRol())
                                        .tipo(rol.getTipo())
                                        .rutaImagen(rol.getRutaImagen())
                                        .build()
                        ).toList())
                        .rutaImagen(usuario.getRutaImagen())
                        .fechaEliminacion(usuario.getFechaEliminado() != null ? usuario.getFechaEliminado().format(formatter) : "")
                        .build())
                .toList();
    }

    @Transactional
    public UsuarioResponse registrarUsuario(UsuarioRequest request, MultipartFile imagen) {
        Usuario usuarioGuardado = registrarUsuario(request);

        if (imagen != null && !imagen.isEmpty()) {
            String rutaImagen = storageService.uploadFile(imagen, "usuario/" + usuarioGuardado.getIdUsuario());
            usuarioGuardado.setRutaImagen(rutaImagen);
            usuarioRepository.save(usuarioGuardado);
        }

        return UsuarioResponse.builder()
                .id(usuarioGuardado.getIdUsuario())
                .dni(usuarioGuardado.getDni())
                .nombre(usuarioGuardado.getNombre())
                .apellidoMat(usuarioGuardado.getApellidoMaterno())
                .apellidoPat(usuarioGuardado.getApellidoPaterno())
                .username(usuarioGuardado.getUsername())
                .direccion(usuarioGuardado.getDireccion())
                .rutaImagen(usuarioGuardado.getRutaImagen())
                .fechaEliminacion(usuarioGuardado.getFechaEliminado() != null ? usuarioGuardado.getFechaEliminado().format(formatter) : "")
                .roles(usuarioGuardado.getRoles().stream().map(
                        rol -> RolResponse.builder()
                                .idRol(rol.getIdRol())
                                .tipo(rol.getTipo())
                                .rutaImagen(rol.getRutaImagen())
                                .build()
                ).toList())
                .build();

    }

    @Transactional
    public UsuarioResponse editarUsuario(Integer id, UsuarioRequest request, MultipartFile imagen) {
        Usuario usuarioParaActualizar = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El usuario no existe."));

        Set<Rol> roles = new HashSet<>();
        for (String tipoRol : request.getRoles()) {
            Rol rol = rolService.obtenerPorTipo(tipoRol);
            roles.add(rol);
        }

        if (imagen != null && !imagen.isEmpty()) {
            String rutaImagen = storageService.uploadImage(imagen, "usuario/" + usuarioParaActualizar.getIdUsuario());
            usuarioParaActualizar.setRutaImagen(rutaImagen);
        }

        usuarioParaActualizar.setContrasena(
                utf8ToBlob(passwordEncoder.encode(request.getContrasena()))
        );
        usuarioParaActualizar.setDireccion(request.getDireccion());
        usuarioParaActualizar.setRoles(roles);

        Usuario usuarioActualizado = usuarioRepository.save(usuarioParaActualizar);

        return UsuarioResponse.builder()
                .id(usuarioActualizado.getIdUsuario())
                .username(usuarioActualizado.getUsername())
                .dni(usuarioActualizado.getDni())
                .nombre(usuarioActualizado.getNombre())
                .apellidoPat(usuarioActualizado.getApellidoPaterno())
                .apellidoMat(usuarioActualizado.getApellidoMaterno())
                .rutaImagen(usuarioActualizado.getRutaImagen())
                .fechaEliminacion(usuarioActualizado.getFechaEliminado() != null ? usuarioActualizado.getFechaEliminado().format(formatter) : "")
                .roles(usuarioActualizado.getRoles().stream().map(
                        rol -> RolResponse.builder()
                                .idRol(rol.getIdRol())
                                .tipo(rol.getTipo())
                                .rutaImagen(rol.getRutaImagen())
                                .build()
                ).toList())
                .build();
    }

    public void eliminarUsuario(Integer id) {
        Usuario usuarioParaEliminar = usuarioRepository.findActiveById(id);

        if (usuarioParaEliminar == null) throw new EntityNotFoundException("El usuario no está activo.");

        usuarioParaEliminar.setFechaEliminado(LocalDateTime.now());

        usuarioRepository.save(usuarioParaEliminar);
    }

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
    public synchronized Usuario registrarUsuario(@NonNull UsuarioRequest request) {
        if (!existsByUsername(request.getUser())) {
            String user = request.getUser();

            Usuario usuario = Usuario.builder()
                    .dni(request.getDni())
                    .email(isEmail(user) ? user : null)
                    .telefono(isTelefono(user) ? user : null)
                    .nombre(request.getNombre())
                    .apellidoPaterno(request.getApellidoPaterno())
                    .apellidoMaterno(request.getApellidoMaterno())
                    .contrasena(utf8ToBlob(passwordEncoder.encode(request.getContrasena())))
                    .roles(new HashSet<>())
                    .build();

            if (request.getRoles() != null && !request.getRoles().isEmpty()) {
                Set<Rol> roles = new HashSet<>();
                for (String tipoRol : request.getRoles()) {
                    Rol rol = rolService.obtenerPorTipo(tipoRol);
                    roles.add(rol);
                }

                usuario.setRoles(roles);
            } else {
                Rol rolPorDefecto = rolService.obtenerPorTipo(ERol.USUARIO.name());

                if (rolPorDefecto == null) {
                    rolPorDefecto = Rol.builder().tipo(ERol.USUARIO.name()).build();
                }

                usuario.addRol(rolPorDefecto);
            }

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
