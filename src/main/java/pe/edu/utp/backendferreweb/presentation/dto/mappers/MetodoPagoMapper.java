package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.MetodoPago;
import pe.edu.utp.backendferreweb.presentation.dto.request.MetodoPagoRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.MetodoPagoResponse;

@Component
public class MetodoPagoMapper {
    public MetodoPago toEntity(MetodoPagoRequest request) {
        if (request == null) return null;

        return MetodoPago.builder()
                .nombre(request.getNombre())
                .build();
    }

    public MetodoPagoResponse toResponse(MetodoPago metodoPago) {
        if (metodoPago == null) return null;

        return MetodoPagoResponse.builder()
                .idMetodoPago(metodoPago.getIdMetodoPago())
                .nombre(metodoPago.getNombre())
                .build();
    }
}
