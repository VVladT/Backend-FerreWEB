package pe.edu.utp.backendferreweb.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

import java.util.List;

@Data
public class VentaPresencialRequest {
    @Valid
    @NotNull(groups = ValidCreacion.class,
            message = "La lista de detalles es nula.")
    @Size(groups = {ValidCreacion.class, ValidActualizacion.class},
            min = 1,
            message = "La lista de detalles debe tener al menos un detalle.")
    private List<DetalleVentaRequest> detalles;

    @NotNull(groups = ValidCreacion.class,
            message = "El DNI no puede ser nulo.")
    @Pattern(groups = {ValidCreacion.class},
            regexp = "^\\d{8}$",
            message = "El DNI del cliente debe contener exactamente 8 dígitos numéricos.")
    private String dniCliente;

    @NotNull(groups = ValidCreacion.class,
            message = "La venta debe tener un almacén de origen.")
    private Integer idAlmacen;

    @NotNull(groups = ValidCreacion.class,
            message = "La venta debe tener un método de pago.")
    private Integer idMetodoPago;
}
