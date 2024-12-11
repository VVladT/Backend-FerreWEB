package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.TipoEntrega;
import pe.edu.utp.backendferreweb.presentation.dto.request.TipoEntregaRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.TipoEntregaResponse;

@Component
public class TipoEntregaMapper {
    public TipoEntrega toEntity(TipoEntregaRequest request) {
        if (request == null) return null;

        return TipoEntrega.builder()
                .tipo(request.getTipo())
                .build();
    }

    public TipoEntregaResponse toResponse(TipoEntrega tipoEntrega) {
        if (tipoEntrega == null) return null;

        return TipoEntregaResponse.builder()
                .idTipoEntrega(tipoEntrega.getIdTipoEntrega())
                .tipo(tipoEntrega.getTipo())
                .build();
    }
}
