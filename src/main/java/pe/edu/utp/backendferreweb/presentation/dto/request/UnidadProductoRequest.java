package pe.edu.utp.backendferreweb.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import pe.edu.utp.backendferreweb.util.validation.annotation.NotBlankIfNotNull;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

@Data
@Builder
public class UnidadProductoRequest {

    @NotBlank(groups = ValidCreacion.class,
            message = "El nombre de la uniadad no puede estar vacío.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "El nombre de la uniadad no puede estar vacío.")
    private String nombreUnidad;

    @NotNull(groups = ValidCreacion.class,
            message = "El precio por unidad no puede ser nulo.")
    @Min(groups = {ValidCreacion.class, ValidActualizacion.class},
            value = 0,
            message = "El precio no puede ser negativo")
    private Double precioPorUnidad;

    @NotNull(groups = ValidCreacion.class,
            message = "La equivalencia no puede ser nula.")
    @Min(groups = {ValidCreacion.class, ValidActualizacion.class},
            value = 0,
            message = "La equivalencia no puede ser negativa")
    private Double equivalencia;
}
