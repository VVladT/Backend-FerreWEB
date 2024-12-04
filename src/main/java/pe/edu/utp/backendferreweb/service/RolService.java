package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.exceptions.IllegalOperationException;
import pe.edu.utp.backendferreweb.persistence.model.Rol;
import pe.edu.utp.backendferreweb.persistence.repository.RolRepository;
import pe.edu.utp.backendferreweb.persistence.repository.UsuarioRepository;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.RolMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.RolRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.RolResponse;
import pe.edu.utp.backendferreweb.service.external.StorageService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final StorageService storageService;
    private final RolMapper rolMapper;

    public List<RolResponse> obtenerTodos() {
        return rolRepository.findAll().stream()
                .map(rolMapper::toResponse)
                .toList();
    }

    public RolResponse obtenerPorId(Integer id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El rol no existe."));

        return rolMapper.toResponse(rol);
    }

    public List<RolResponse> obtenerRolesPorIdUsuario(Integer id) {
        return usuarioRepository.findRolesByIdUsuario(id).stream()
                .map(rolMapper::toResponse)
                .toList();
    }

    public RolResponse obtenerPorTipo(String tipoRol) {
        Rol rol = rolRepository.getRolByTipo(tipoRol)
                .orElseThrow(() -> new EntityNotFoundException("No existe rol con el tipo: " + tipoRol));

        return rolMapper.toResponse(rol);
    }

    public RolResponse crearRol(RolRequest request, MultipartFile imagen) {
        String tipo = request.getTipo();

        if (tipo == null) throw new IllegalArgumentException("El tipo del rol no puede ser nulo");
        if (tipo.isBlank()) throw new IllegalArgumentException("El tipo del rol no puede estar vacío");

        if (rolRepository.existsByTipo(tipo)) {
            throw new EntityExistsException("Ya existe rol con el tipo: " + tipo);
        }

        Rol rol = rolMapper.toEntity(request);

        Rol rolGuardado = rolRepository.save(rol);

        if (imagen != null && !imagen.isEmpty()) {
            String rutaImagen = storageService.uploadImage(imagen, "rol/" + rolGuardado.getIdRol() + ".webp");
            rolGuardado.setRutaImagen(rutaImagen);
            rolGuardado = rolRepository.save(rolGuardado);
        }

        return rolMapper.toResponse(rolGuardado);
    }

    public RolResponse editarRol(Integer id, RolRequest request, MultipartFile imagen) {
        String nuevoTipo = request.getTipo();

        Rol rolParaActualizar = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe rol con id: " + id));

        if (!rolParaActualizar.getTipo().equals(nuevoTipo) && rolRepository.existsByTipo(nuevoTipo)) {
            throw new EntityExistsException("Ya existe rol con el tipo: " + nuevoTipo);
        }

        if (nuevoTipo != null) {
            if (nuevoTipo.isBlank())
                throw new IllegalArgumentException("El nuevo tipo no puede estar vacío");

            rolParaActualizar.setTipo(nuevoTipo);
        }

        if (imagen != null && !imagen.isEmpty()) {
            String rutaImagen = storageService.uploadImage(imagen, "rol/" + rolParaActualizar.getIdRol() + ".webp");
            rolParaActualizar.setRutaImagen(rutaImagen);
        }

        Rol rolActualizado = rolRepository.save(rolParaActualizar);

        return rolMapper.toResponse(rolActualizado);
    }

    public void eliminarRol(Integer id) {
        if (rolRepository.isAssociated(id)) {
            throw new IllegalOperationException("No se puede eliminar el rol debido a que está asociado a un usuario.");
        }

        rolRepository.deleteById(id);
    }

    public Rol obtenerEntidadPorTipo(String tipoRol) {
        return rolRepository.getRolByTipo(tipoRol)
                .orElseGet(() -> rolRepository.save(Rol.builder()
                        .tipo(tipoRol)
                        .build()));
    }
}
