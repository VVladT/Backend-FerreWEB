package pe.edu.utp.backendferreweb.presentation.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.utp.backendferreweb.util.validation.annotation.NotBlankIfNotNull;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidLogin;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequest {
    @NotNull(groups = {ValidCreacion.class, ValidLogin.class},
            message = "El correo electrónico no puede ser nulo.")
    @Email(groups = {ValidCreacion.class, ValidLogin.class, ValidActualizacion.class},
            message = "El correo electrónico debe ser un correo válido.")
    private String user;

    @NotNull(groups = ValidCreacion.class,
            message = "El DNI no puede ser nulo.")
    @Pattern(groups = {ValidCreacion.class, ValidActualizacion.class},
            regexp = "^\\d{8}$",
            message = "El DNI del usuario debe contener exactamente 8 dígitos numéricos.")
    private String dni;

    @NotBlank(groups = ValidCreacion.class, message = "El nombre del usuario no puede estar vacío.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "El nombre del usuario no puede estar vacío.")
    private String nombre;

    @NotBlank(groups = ValidCreacion.class, message = "El apellido paterno del usuario no puede estar vacío.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "El apellido paterno del usuario no puede estar vacío.")
    private String apellidoPaterno;

    @NotBlank(groups = ValidCreacion.class, message = "El apellido materno del usuario no puede estar vacío.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "El apellido materno del usuario no puede estar vacío.")
    private String apellidoMaterno;

    @NotBlank(groups = {ValidCreacion.class, ValidLogin.class},
            message = "La contraseña no puede estar vacía.")
    @NotBlankIfNotNull(groups = {ValidLogin.class, ValidActualizacion.class},
            message = "La contraseña no puede estar vacía.")
    @Size(groups = {ValidCreacion.class, ValidLogin.class},
            min = 6,
            message = "La contraseña debe tener al menos 6 caractéres.")
    private String contrasena;

    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "La dirección no puede estar vacía.")
    private String direccion;

    @Size(groups = ValidActualizacion.class,
            min = 1,
            message = "El usuario debe tener al menos un rol asociado.")
    private List<String> roles;
}
