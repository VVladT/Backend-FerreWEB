package pe.edu.utp.backendferreweb.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.utp.backendferreweb.util.validation.annotation.NotBlankIfNotNull;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RolRequest {
    @NotBlank(groups = ValidCreacion.class,
            message = "El tipo de rol no puede estar vacío.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "El tipo de rol no puede estar vacío.")
    private String tipo;
}
