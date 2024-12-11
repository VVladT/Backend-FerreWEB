package pe.edu.utp.backendferreweb.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import pe.edu.utp.backendferreweb.util.validation.annotation.NotBlankIfNotNull;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

@Data
public class TipoEntregaRequest {
    @NotBlank(groups = ValidCreacion.class,
            message = "El tipo de entrega no puede estar vacío.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "El tipo de entrega no puede estar vacío.")
    private String tipo;
}
