package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Almacen;
import pe.edu.utp.backendferreweb.persistence.repository.AlmacenRepository;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.AlmacenMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.AlmacenRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.AlmacenResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlmacenService {
    private final AlmacenRepository almacenRepository;
    private final AlmacenMapper almacenMapper;

    @Transactional
    public List<AlmacenResponse> obtenerTodos() {
        return almacenRepository.findAllActive().stream()
                .map(almacenMapper::toResponse)
                .toList();
    }

    public AlmacenResponse obtenerPorId(Integer id) {
        Almacen almacen = almacenRepository.findActiveById(id);

        if (almacen == null) throw new EntityNotFoundException("No existe almacen con id: " + id);

        return almacenMapper.toResponse(almacen);
    }

    public AlmacenResponse crearAlmacen(AlmacenRequest request) {
        String nombre = request.getNombre();
        String direccion = request.getDireccion();

        if (nombre == null) throw new IllegalArgumentException("El nombre no puede ser nulo");
        if (nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío");
        if (direccion == null) throw new IllegalArgumentException("La dirección no puede ser nula");
        if (direccion.isBlank()) throw new IllegalArgumentException("La dirección no puede estar vacía");

        if (almacenRepository.existsByNombre(nombre)) {
            throw new EntityExistsException("Ya existe un almacen con nombre: " + nombre);
        }

        Almacen almacen = almacenRepository.save(almacenMapper.toEntity(request));

        return almacenMapper.toResponse(almacen);
    }

    public AlmacenResponse editarAlmacen(Integer id, AlmacenRequest request) {
        String nuevoNombre = request.getNombre();
        String nuevaDireccion = request.getDireccion();

        Almacen almacenParaActualizar = almacenRepository.findActiveById(id);

        if (almacenParaActualizar == null) throw new EntityNotFoundException("El almacen no existe.");

        if (!almacenParaActualizar.getNombre().equals(nuevoNombre) &&
                almacenRepository.existsByNombre(nuevoNombre)) {
            throw new EntityExistsException("El almacen con el nombre: "
                    + nuevoNombre + " ya existe.");
        }

        if (nuevoNombre != null) {
            if (nuevoNombre.isBlank())
                throw new IllegalArgumentException("El nuevo nombre no puede estar vacío");

            almacenParaActualizar.setNombre(nuevoNombre);
        }

        if (nuevaDireccion != null) {
            if (nuevaDireccion.isBlank())
                throw new IllegalArgumentException("La nueva dirección no puede estar vacía");

            almacenParaActualizar.setDireccion(nuevaDireccion);
        }

        Almacen almacenActualizado = almacenRepository.save(almacenParaActualizar);

        return almacenMapper.toResponse(almacenActualizado);
    }

    public void eliminarAlmacen(Integer id) {
        if (almacenRepository.isAssociatedWithProduct(id))
            throw new DataIntegrityViolationException("No se pudo eliminar el almacen debido a que está asociado con un producto");

        Almacen almacenParaEliminar = almacenRepository.findActiveById(id);

        if (almacenParaEliminar == null) throw new EntityNotFoundException("No existe el almacén con el id: " + id);

        almacenParaEliminar.setFechaEliminado(LocalDateTime.now());
        almacenRepository.save(almacenParaEliminar);
    }

    public Almacen obtenerPorNombre(String nombre) {
        return almacenRepository.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException("No existe el almacén con el nombre: " + nombre));
    }

    public Almacen obtenerEntidadPorId(Integer id) {
        return almacenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe almacen con id: " + id));
    }
}
