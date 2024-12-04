package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.Unidad;
import pe.edu.utp.backendferreweb.presentation.dto.request.UnidadRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.UnidadResponse;

@Component
public class UnidadMapper {
    public Unidad toEntity(UnidadRequest request) {
        if (request == null) return null;

        return Unidad.builder()
                .nombre(request.getNombre())
                .build();
    }

    public UnidadResponse toResponse(Unidad unidad) {
        if (unidad == null) return null;

        return UnidadResponse.builder()
                .idUnidad(unidad.getIdUnidad())
                .nombre(unidad.getNombre())
                .build();
    }
}
