package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.MetodoPago;
import pe.edu.utp.backendferreweb.persistence.repository.MetodoPagoRepository;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.MetodoPagoMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.MetodoPagoRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.MetodoPagoResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;
    private final MetodoPagoMapper metodoPagoMapper;

    public List<MetodoPagoResponse> obtenerTodos() {
        return metodoPagoRepository.findAll().stream()
                .map(metodoPagoMapper::toResponse)
                .toList();
    }

    public MetodoPagoResponse obtenerPorId(Integer id) {
        MetodoPago metodoPago = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe método de pago con el id: " + id));

        return metodoPagoMapper.toResponse(metodoPago);
    }

    public MetodoPagoResponse crearMetodoPago(MetodoPagoRequest request) {
        String nombre = request.getNombre();

        if (nombre == null) throw new IllegalArgumentException("El nombre del método no puede ser nulo");
        if (nombre.isBlank()) throw new IllegalArgumentException("El nombre del método no puede estar vacío");

        MetodoPago metodoPago = metodoPagoMapper.toEntity(request);

        if (metodoPagoRepository.existsByNombre(nombre))
            throw new EntityExistsException("Ya existe un método de pago con el nombre: " + nombre);

        MetodoPago metodoCreado = metodoPagoRepository.save(metodoPago);

        return metodoPagoMapper.toResponse(metodoCreado);
    }

    public MetodoPagoResponse editarMetodoPago(Integer id, MetodoPagoRequest request) {
        String nuevoNombre = request.getNombre();

        MetodoPago metodoPago = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe método de pago con el id: " + id));

        if (nuevoNombre != null) {
            if (nuevoNombre.isBlank())
                throw new IllegalArgumentException("El nombre del método no puede estar vacío");

            metodoPago.setNombre(nuevoNombre);
        }

        MetodoPago metodoPagoActualizado = metodoPagoRepository.save(metodoPago);

        return metodoPagoMapper.toResponse(metodoPagoActualizado);
    }

    public void eliminarMetodoPago(Integer id) {
        metodoPagoRepository.deleteById(id);
    }

    public MetodoPago obtenerEntidadPorId(Integer id) {
        return metodoPagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe método de pago con id: " + id));
    }
}
