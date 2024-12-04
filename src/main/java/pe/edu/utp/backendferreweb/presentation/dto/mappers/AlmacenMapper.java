package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.Almacen;
import pe.edu.utp.backendferreweb.presentation.dto.request.AlmacenRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.AlmacenResponse;

@Component
public class AlmacenMapper {
    public Almacen toEntity(AlmacenRequest request) {
        if (request == null) return null;

        return Almacen.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .build();
    }

    public AlmacenResponse toResponse(Almacen almacen) {
        if (almacen == null) return null;

        return AlmacenResponse.builder()
                .idAlmacen(almacen.getIdAlmacen())
                .nombre(almacen.getNombre())
                .direccion(almacen.getDireccion())
                .build();
    }
}
