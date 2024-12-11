package pe.edu.utp.backendferreweb.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import pe.edu.utp.backendferreweb.util.validation.annotation.NotBlankIfNotNull;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

@Data
@Builder
public class ProveedorRequest {
    @NotNull(groups = ValidCreacion.class,
            message = "El RUC del proveedor no puede ser nulo.")
    @Pattern(groups = {ValidCreacion.class, ValidActualizacion.class},
            regexp = "^\\d{11}$",
            message = "El RUC del proveedor debe contener exactamente 11 dígitos numéricos.")
    private String ruc;

    @NotBlank(groups = ValidCreacion.class, message = "El nombre del proveedor  no puede estar vacío.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "El nombre del proveedor no puede estar vacío.")
    private String nombre;

    @NotBlankIfNotNull(groups = {ValidCreacion.class, ValidActualizacion.class},
            message = "El nombre comercial del proveedor no puede estar vacío.")
    private String nombreComercial;

    @NotNull(groups = ValidCreacion.class, message = "El correo electrónico no puede ser nulo.")
    @Email(groups = {ValidCreacion.class, ValidActualizacion.class},
            message = "El correo electrónico del proveedor debe ser válido.")
    private String email;

    @NotNull(groups = ValidCreacion.class, message = "El teléfono no puede ser nulo.")
    @Pattern(groups = {ValidCreacion.class, ValidActualizacion.class},
            regexp = "^9\\d{8}$",
            message = "El teléfono del proveedor debe ser válido.")
    private String telefono;

    @NotBlank(groups = ValidCreacion.class, message = "La dirección del proveedor no puede estar vacía.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class, message = "La dirección del proveedor no puede estar vacía.")
    private String direccion;
}
