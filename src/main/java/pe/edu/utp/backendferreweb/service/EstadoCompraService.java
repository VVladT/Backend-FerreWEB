package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.EstadoCompra;
import pe.edu.utp.backendferreweb.persistence.repository.EstadoCompraRepository;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.EstadoCompraMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.EstadoCompraRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.EstadoCompraResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoCompraService {

    private final EstadoCompraRepository estadoCompraRepository;
    private final EstadoCompraMapper estadoCompraMapper;

    public List<EstadoCompraResponse> obtenerTodos() {
        return estadoCompraRepository.findAll().stream()
                .map(estadoCompraMapper::toResponse)
                .toList();
    }

    public EstadoCompraResponse obtenerPorId(Integer id) {
        EstadoCompra estadoCompra = estadoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe estado de compra con id: " + id));

        return estadoCompraMapper.toResponse(estadoCompra);
    }

    public EstadoCompraResponse crearEstado(EstadoCompraRequest request) {
        String estado = request.getEstado();

        if (estado == null) throw new IllegalArgumentException("El estado de compra no puede ser nulo");
        if (estado.isBlank()) throw new IllegalArgumentException("El estado de compra no puede estar vacío");

        EstadoCompra estadoCompra = estadoCompraMapper.toEntity(request);
        EstadoCompra nuevoEstado = estadoCompraRepository.save(estadoCompra);

        return estadoCompraMapper.toResponse(nuevoEstado);
    }

    public EstadoCompraResponse editarEstado(Integer id, EstadoCompraRequest request) {
        String nuevoEstado = request.getEstado();
        EstadoCompra estadoEncontrado = estadoCompraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe estado de compra con el id: " + id));

        if (nuevoEstado != null) {
            if (nuevoEstado.isBlank())
                throw new IllegalArgumentException("El estado de compra no puede estar vacío");

            estadoEncontrado.setEstado(nuevoEstado);
        }

        EstadoCompra estadoActualizado = estadoCompraRepository.save(estadoEncontrado);

        return estadoCompraMapper.toResponse(estadoActualizado);
    }

    public void eliminarEstado(Integer id) {
        estadoCompraRepository.deleteById(id);
    }

    public EstadoCompra obtenerPorNombre(String nombre) {
        return estadoCompraRepository.findByEstado(nombre)
                .orElseGet(() -> estadoCompraRepository.save(EstadoCompra.builder()
                                .estado(nombre)
                                .build()));
    }
}
