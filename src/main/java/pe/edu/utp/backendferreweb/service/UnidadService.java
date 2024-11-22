package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Unidad;
import pe.edu.utp.backendferreweb.persistence.repository.UnidadRepository;
import pe.edu.utp.backendferreweb.presentation.dto.request.UnidadRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadService {
    private final UnidadRepository unidadRepository;

    public List<Unidad> obtenerTodas() {
        return unidadRepository.findAllActive();
    }

    public Unidad obtenerPorId(int id) {
        Unidad unidad = unidadRepository.findActiveById(id);

        if (unidad == null) throw new EntityNotFoundException("No existe unidad con id: " + id);

        return unidad;
    }

    public Unidad crearUnidad(UnidadRequest request) {
        String nombre = request.getUnidad();

        if (unidadRepository.existsByNombre(nombre)) {
            throw new EntityExistsException("La unidad \"" + nombre + "\" ya existe.");
        } else {
            Unidad nuevaUnidad = Unidad.builder().nombre(nombre).build();
            return unidadRepository.save(nuevaUnidad);
        }
    }

    public Unidad editarUnidad(int id, UnidadRequest request) {
        String nuevoNombre = request.getUnidad();
        Unidad unidadParaActualizar = unidadRepository.findActiveById(id);

        if (unidadParaActualizar == null) throw new EntityNotFoundException("La unidad no existe.");

        if (unidadRepository.existsByNombre(nuevoNombre)) {
            throw new EntityExistsException("La unidad con el nombre \""
                    + nuevoNombre + "\" ya existe.");
        } else {
            unidadParaActualizar.setNombre(nuevoNombre);

            return unidadRepository.save(unidadParaActualizar);
        }
    }

    public void eliminarUnidad(int id) {
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
