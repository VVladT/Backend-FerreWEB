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
import pe.edu.utp.backendferreweb.presentation.dto.request.RolRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final StorageService storageService;

    public List<Rol> obtenerTodos() {
        return rolRepository.findAll();
    }

    public Rol obtenerPorId(Integer id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El rol no existe."));
    }

    public List<Rol> obtenerRolesPorIdUsuario(Integer id) {
        return usuarioRepository.findRolesByIdUsuario(id);
    }

    public Rol obtenerPorTipo(String rol) {
        return rolRepository.getRolByTipo(rol)
                .orElseGet(() -> rolRepository.save(Rol.builder()
                        .tipo(rol)
                        .build()));
    }

    public Rol crearRol(RolRequest request, MultipartFile imagen) {
        String tipo = request.getTipo();

        if (rolRepository.existsByTipo(tipo)) {
            throw new EntityExistsException("El rol: " + tipo + " ya existe.");
        } else {
            Rol rol = Rol.builder()
                    .tipo(tipo)
                    .build();

            Rol rolGuardado = rolRepository.save(rol);

            if (imagen != null && !imagen.isEmpty()) {
                String rutaImagen = storageService.uploadImage(imagen, "rol/" + rolGuardado.getIdRol() + ".webp");
                rolGuardado.setRutaImagen(rutaImagen);
            }

            return rolRepository.save(rolGuardado);
        }
    }

    public Rol editarRol(Integer id, RolRequest request, MultipartFile imagen) {
        String nuevoTipo = request.getTipo();
        Rol rolParaActualizar = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El rol no existe."));

        if (!rolParaActualizar.getTipo().equals(nuevoTipo) && rolRepository.existsByTipo(nuevoTipo)) {
            throw new EntityExistsException("El rol con el tipo: " + nuevoTipo + " ya existe.");
        } else {
            rolParaActualizar.setTipo(nuevoTipo);

            if (imagen != null && !imagen.isEmpty()) {
                String rutaImagen = storageService.uploadImage(imagen, "rol/" + rolParaActualizar.getIdRol() + ".webp");
                rolParaActualizar.setRutaImagen(rutaImagen);
            }

            return rolRepository.save(rolParaActualizar);
        }
    }

    public void eliminarRol(Integer id) {
        if (rolRepository.isAssociated(id)) {
            throw new IllegalOperationException("No se puede eliminar el rol debido a que está asociado a un usuario.");
        }

        rolRepository.deleteById(id);
    }
}
