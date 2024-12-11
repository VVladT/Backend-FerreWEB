package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.EstadoCompra;
import pe.edu.utp.backendferreweb.presentation.dto.request.EstadoCompraRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.EstadoCompraResponse;

@Component
public class EstadoCompraMapper {
    public EstadoCompra toEntity(EstadoCompraRequest request) {
        if (request == null) return null;

        return EstadoCompra.builder()
                .estado(request.getEstado())
                .build();
    }

    public EstadoCompraResponse toResponse(EstadoCompra estadoCompra) {
        if (estadoCompra == null) return null;

        return EstadoCompraResponse.builder()
                .idEstadoCompra(estadoCompra.getIdEstadoCompra())
                .estado(estadoCompra.getEstado())
                .build();
    }
}
