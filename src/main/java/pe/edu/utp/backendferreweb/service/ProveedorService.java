package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Proveedor;
import pe.edu.utp.backendferreweb.persistence.model.enums.EAuditAction;
import pe.edu.utp.backendferreweb.persistence.repository.ProveedorRepository;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.ProveedorMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.ProveedorRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.ProveedorResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProveedorService {
    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;
    private final AuditoriaService auditoriaService;

    public List<ProveedorResponse> obtenerTodos() {
        return proveedorRepository.findAllActive().stream()
                .map(proveedorMapper::toResponse)
                .toList();
    }

    public ProveedorResponse obtenerPorId(Integer id) {
        Proveedor proveedor = proveedorRepository.findActiveById(id);

        if (proveedor == null) throw new EntityNotFoundException("No existe proveedor con id: " + id);

        return proveedorMapper.toResponse(proveedor);
    }

    public ProveedorResponse buscarPorRuc(String ruc) {
        Proveedor proveedor = proveedorRepository.findActiveByRuc(ruc);

        if (proveedor == null) throw new EntityNotFoundException("No existe proveedor con ruc: " + ruc);

        return proveedorMapper.toResponse(proveedor);
    }

    public ProveedorResponse crearProveedor(ProveedorRequest request) {
        if (proveedorRepository.existsActiveByRuc(request.getRuc()))
            throw new EntityExistsException("Ya existe proveedor con ruc: " + request.getRuc());

        Proveedor proveedor = proveedorMapper.toEntity(request);
        Proveedor nuevoProveedor = proveedorRepository.save(proveedor);

        return proveedorMapper.toResponse(nuevoProveedor);
    }

    public ProveedorResponse editarProveedor(Integer id, ProveedorRequest request) {
        String nuevoRuc = request.getRuc();
        String nuevoNombre = request.getNombre();
        String nuevoNombreComercial = request.getNombreComercial();
        String nuevoEmail = request.getEmail();
        String nuevoTelefono = request.getTelefono();
        String nuevaDireccion = request.getDireccion();

        Proveedor proveedorExistente = proveedorRepository.findActiveById(id);

        if (!proveedorExistente.getRuc().equals(nuevoRuc) &&
                proveedorRepository.existsActiveByRuc(nuevoRuc)) {
            throw new EntityExistsException("Ya existe proveedor con ruc: " + nuevoRuc);
        }

        if (nuevoNombre != null) proveedorExistente.setNombre(nuevoNombre);

        if (nuevoNombreComercial != null) proveedorExistente.setNombreComercial(nuevoNombreComercial);

        if (nuevoEmail != null) proveedorExistente.setEmail(nuevoEmail);

        if (nuevoTelefono != null) proveedorExistente.setTelefono(nuevoTelefono);

        if (nuevaDireccion != null) proveedorExistente.setDireccion(nuevaDireccion);

        Proveedor proveedorActualizado = proveedorRepository.save(proveedorExistente);

        return proveedorMapper.toResponse(proveedorActualizado);
    }

    public void eliminarProveedor(Integer id, String motivo) {
        Proveedor proveedorParaEliminar = proveedorRepository.findActiveById(id);

        if (proveedorParaEliminar == null)
            throw new EntityNotFoundException("No existe categoría con id: " + id);

        proveedorParaEliminar.setFechaEliminado(LocalDateTime.now());

        auditoriaService.registerAudit(id,"Proveedor", EAuditAction.DELETE.name(), motivo);

        proveedorRepository.save(proveedorParaEliminar);
    }

    public Proveedor obtenerEntidadPorId(Integer id) {
        return proveedorRepository.findActiveById(id);
    }
}
