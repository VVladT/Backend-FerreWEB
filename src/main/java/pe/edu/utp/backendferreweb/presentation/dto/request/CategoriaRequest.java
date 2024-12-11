package pe.edu.utp.backendferreweb.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import pe.edu.utp.backendferreweb.util.validation.annotation.NotBlankIfNotNull;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

@Data
public class CategoriaRequest {
    @NotBlank(groups = ValidCreacion.class,
            message = "El nombre de la categoría no puede estar vacío.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "El nombre de la categoría no puede estar vacío.")
    private String nombre;

    @NotBlank(groups = ValidCreacion.class,
            message = "La descripción de la categoría no puede estar vacía.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "La descripción de la categoría no puede estar vacía.")
    private String descripcion;
}
