package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.TipoEntrega;
import pe.edu.utp.backendferreweb.persistence.repository.TipoEntregaRepository;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.TipoEntregaMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.TipoEntregaRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.TipoEntregaResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoEntregaService {

    private final TipoEntregaRepository tipoEntregaRepository;
    private final TipoEntregaMapper tipoEntregaMapper;

    public List<TipoEntregaResponse> obtenerTodos() {
        return tipoEntregaRepository.findAll().stream()
                .map(tipoEntregaMapper::toResponse)
                .toList();
    }

    public TipoEntregaResponse obtenerPorId(Integer id) {
        TipoEntrega tipoEntrega = tipoEntregaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe tipo de entrega con id: " + id));

        return tipoEntregaMapper.toResponse(tipoEntrega);
    }

    public TipoEntregaResponse crearTipoEntrega(TipoEntregaRequest request) {
        String tipo = request.getTipo();

        if (tipo == null) throw new IllegalArgumentException("El tipo de entrega no puede ser nulo");
        if (tipo.isBlank()) throw new IllegalArgumentException("El tipo de entrega no puede estar vacío");

        if (tipoEntregaRepository.existsByTipo(tipo))
            throw new EntityExistsException("Ya existe tipo de entrega con el tipo: " + tipo);

        TipoEntrega tipoEntrega = tipoEntregaMapper.toEntity(request);
        TipoEntrega tipoCreado = tipoEntregaRepository.save(tipoEntrega);

        return tipoEntregaMapper.toResponse(tipoCreado);
    }

    public TipoEntregaResponse editarTipoEntrega(Integer id, TipoEntregaRequest request) {
        String tipo = request.getTipo();

        TipoEntrega tipoEntrega = tipoEntregaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe tipo de entrega con id: " + id));

        if (tipo != null) {
            if (tipo.isBlank())
                throw new IllegalArgumentException("El tipo de entrega no puede estar vacío");

            tipoEntrega.setTipo(tipo);
        }

        TipoEntrega tipoActualizado = tipoEntregaRepository.save(tipoEntrega);

        return tipoEntregaMapper.toResponse(tipoActualizado);
    }

    public void eliminarTipoEntrega(Integer id) {
        tipoEntregaRepository.deleteById(id);
    }

    public TipoEntrega obtenerEntidadPorId(Integer id) {
        return tipoEntregaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe tipo de entrega con id: " + id));
    }
}
