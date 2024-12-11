package pe.edu.utp.backendferreweb.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import pe.edu.utp.backendferreweb.util.validation.annotation.NotBlankIfNotNull;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

@Data
@Builder
public class UnidadRequest {

    @NotBlank(groups = ValidCreacion.class,
            message = "El nombre de la unidad no puede estar vacío.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "El nombre de la unidad no puede estar vacío.")
    private String nombre;
}
