package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Unidad;
import pe.edu.utp.backendferreweb.persistence.repository.UnidadRepository;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.UnidadMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.UnidadRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.UnidadResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadService {
    private final UnidadRepository unidadRepository;
    private final UnidadMapper unidadMapper;

    public List<UnidadResponse> obtenerTodas() {
        return unidadRepository.findAllActive().stream()
                .map(unidadMapper::toResponse)
                .toList();
    }

    public UnidadResponse obtenerPorId(int id) {
        Unidad unidad = unidadRepository.findActiveById(id);

        if (unidad == null) throw new EntityNotFoundException("No existe unidad con id: " + id);

        return unidadMapper.toResponse(unidad);
    }

    public UnidadResponse crearUnidad(UnidadRequest request) {
        String nombre = request.getNombre();

        if (nombre == null) throw new IllegalArgumentException("El nombre de la unidad no puede ser nulo");
        if (nombre.isBlank()) throw new IllegalArgumentException("El nombre de la unidad no puede estar vacío");

        if (unidadRepository.existsByNombre(nombre)) {
            throw new EntityExistsException("Ya existe la unidad con el nombre: " + nombre);
        }

        Unidad nuevaUnidad = unidadMapper.toEntity(request);
        nuevaUnidad = unidadRepository.save(nuevaUnidad);

        return unidadMapper.toResponse(nuevaUnidad);
    }

    public UnidadResponse editarUnidad(Integer id, UnidadRequest request) {
        String nuevoNombre = request.getNombre();
        Unidad unidadParaActualizar = unidadRepository.findActiveById(id);

        if (unidadParaActualizar == null)
            throw new EntityNotFoundException("No existe unidad con el id: " + id);

        if (!unidadParaActualizar.getNombre().equals(nuevoNombre) &&
                unidadRepository.existsByNombre(nuevoNombre)) {
            throw new EntityExistsException("Ya existe unidad con el nombre: " + nuevoNombre);
        }

        if (nuevoNombre != null) {
            if (nuevoNombre.isBlank())
                throw new IllegalArgumentException("El nuevo nombre no puede estar vacío");

            unidadParaActualizar.setNombre(nuevoNombre);
        }

        Unidad unidadActualizada = unidadRepository.save(unidadParaActualizar);

        return unidadMapper.toResponse(unidadActualizada);
    }

    public void eliminarUnidad(Integer id) {
        Unidad unidadParaEliminar = unidadRepository.findActiveById(id);

        if (unidadParaEliminar == null) throw new EntityNotFoundException("No existe unidad con id: " + id);

        unidadParaEliminar.setFechaEliminado(LocalDateTime.now());

        unidadRepository.save(unidadParaEliminar);
    }

    public Unidad obtenerPorNombre(String nombre) {
        return unidadRepository.findByNombre(nombre)
                .orElseGet(() -> unidadRepository.save(Unidad.builder()
                                .nombre(nombre)
                                .build()));
    }
}
