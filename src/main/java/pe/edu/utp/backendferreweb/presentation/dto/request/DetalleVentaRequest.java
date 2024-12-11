package pe.edu.utp.backendferreweb.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

@Data
public class DetalleVentaRequest {
    @NotNull(groups = {ValidCreacion.class},
            message = "El detalle de compra debe estar asociado a un producto.")
    private Integer idProducto;

    @NotNull(groups = {ValidCreacion.class},
            message = "Debe seleccionar una unidad en el detalle de compra.")
    private Integer idUnidad;

    @NotNull(groups = {ValidCreacion.class},
            message = "La cantidad de productos en el detalle no puede ser nula.")
    @Min(groups = {ValidCreacion.class, ValidActualizacion.class},
            value = 1,
            message = "La cantidad de productos debe ser de al menos 1")
    private Double cantidad;
}
