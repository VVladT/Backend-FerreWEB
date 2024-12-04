package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Proveedor;
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
        String ruc = request.getRuc();
        String nombre = request.getNombre();
        String email = request.getEmail();
        String telefono = request.getTelefono();
        String direccion = request.getDireccion();

        if (ruc == null) throw new IllegalArgumentException("El RUC no puede ser nulo");
        if (ruc.isBlank()) throw new IllegalArgumentException("El RUC no puede estar vacío");
        if (ruc.length() != 11) throw new IllegalArgumentException("El RUC debe tener 11 dígitos");
        if (nombre == null) throw new IllegalArgumentException("El nombre no puede ser nulo");
        if (nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío");
        if (email == null) throw new IllegalArgumentException("El email no puede ser nulo");
        if (email.isBlank()) throw new IllegalArgumentException("El email no puede estar vacío");
        if (telefono == null) throw new IllegalArgumentException("El telefono no puede ser nulo");
        if (telefono.isBlank()) throw new IllegalArgumentException("El telefono no puede estar vacío");
        if (direccion == null) throw new IllegalArgumentException("La dirección no puede ser nula");
        if (direccion.isBlank()) throw new IllegalArgumentException("La dirección no puede estar vacía");

        if (proveedorRepository.existsActiveByRuc(ruc))
            throw new EntityExistsException("Ya existe proveedor con ruc: " + ruc);

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

        if (nuevoNombre != null) {
            if (nuevoNombre.isBlank())
                throw new IllegalArgumentException("El nuevo nombre no puede estar vacío");

            proveedorExistente.setNombre(nuevoNombre);
        }

        if (nuevoNombreComercial != null) {
            if (nuevoNombreComercial.isBlank())
                throw new IllegalArgumentException("El nuevo nombre comercial no puede estar vacío");

            proveedorExistente.setNombreComercial(nuevoNombreComercial);
        }

        if (nuevoEmail != null) {
            if (nuevoEmail.isBlank())
                throw new IllegalArgumentException("El nuevo email no puede estar vacío");

            proveedorExistente.setEmail(nuevoEmail);
        }

        if (nuevoTelefono != null) {
            if (nuevoTelefono.isBlank())
                throw new IllegalArgumentException("El nuevo teléfono no puede estar vacío");

            proveedorExistente.setTelefono(nuevoTelefono);
        }

        if (nuevaDireccion != null) {
            if (nuevaDireccion.isBlank())
                throw new IllegalArgumentException("La nueva dirección no puede estar vacía");

            proveedorExistente.setDireccion(nuevaDireccion);
        }

        Proveedor proveedorActualizado = proveedorRepository.save(proveedorExistente);

        return proveedorMapper.toResponse(proveedorActualizado);
    }

    public void eliminarProveedor(Integer id) {
        Proveedor proveedorParaEliminar = proveedorRepository.findActiveById(id);

        if (proveedorParaEliminar == null)
            throw new EntityNotFoundException("No existe categoría con id: " + id);

        proveedorParaEliminar.setFechaEliminado(LocalDateTime.now());

        proveedorRepository.save(proveedorParaEliminar);
    }
}
