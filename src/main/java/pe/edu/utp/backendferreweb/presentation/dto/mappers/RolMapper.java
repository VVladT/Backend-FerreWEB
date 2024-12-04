package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.Rol;
import pe.edu.utp.backendferreweb.presentation.dto.request.RolRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.RolResponse;

@Component
public class RolMapper {

    public Rol toEntity(RolRequest request) {
        if (request == null) return null;

        return Rol.builder()
                .tipo(request.getTipo())
                .build();
    }

    public RolResponse toResponse(Rol rol) {
        if (rol == null) return null;

        return RolResponse.builder()
                .idRol(rol.getIdRol())
                .tipo(rol.getTipo())
                .rutaImagen(rol.getRutaImagen())
                .build();
    }
}
