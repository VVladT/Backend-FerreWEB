package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.exceptions.IllegalOperationException;
import pe.edu.utp.backendferreweb.persistence.model.Rol;
import pe.edu.utp.backendferreweb.persistence.model.enums.ERol;
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

    public List<Rol> obtenerRolesPorIdUsuario(Integer id) {
        return usuarioRepository.findRolesByIdUsuario(id);
    }

    public Rol obtenerPorTipo(ERol rol) {
        return rolRepository.getRolByTipo(rol.name())
                .orElseGet(() -> rolRepository.save(Rol.builder()
                        .tipo(rol.name())
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
                String rutaImagen = storageService.uploadImage(imagen, "rol/" + rolGuardado.getIdRol());
                rolGuardado.setRutaImagen(rutaImagen);
            }

            return rolRepository.save(rolGuardado);
        }
    }

    public void eliminarRol(Integer id) {
        if (rolRepository.isAssociated(id)) {
            throw new IllegalOperationException("No se puede eliminar el rol debido a que está asociado a un usuario.");
        }

        rolRepository.deleteById(id);
    }
}
