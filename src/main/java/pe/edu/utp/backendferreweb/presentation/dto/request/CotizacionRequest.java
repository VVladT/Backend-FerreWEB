package pe.edu.utp.backendferreweb.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CotizacionRequest {
    @NotNull(message = "La lista de proveedores a solicitar cotización es nula.")
    @Size(min = 1,
            message = "La cotización requiere al menos un proveedor.")
    private List<Integer> idProveedores;

    @NotNull(message = "La lista de productos a cotizar es nula.")
    @Size(min = 1,
            message = "La cotización requiere al menos un producto.")
    private List<Integer> idProductos;

    @NotNull(message = "La fecha máxima para la cotización es requerida.")
    private LocalDate fechaLimite;
}
