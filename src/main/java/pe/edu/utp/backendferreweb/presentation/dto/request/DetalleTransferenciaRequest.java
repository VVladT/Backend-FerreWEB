package pe.edu.utp.backendferreweb.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DetalleTransferenciaRequest {
    @NotBlank(message = "Ingrese una entidad bancaria válida.")
    private String entidadBancaria;

    private LocalDateTime fechaTransferencia;

    @NotNull(message = "El monto no puede ser nulo.")
    @Min(value = 10,
            message = "El monto debe ser mayor a 10 soles.")
    private Double montoTransferido;

    @NotNull(message = "El número de operacion no puede ser nulo.")
    @Size(min = 6,
            message = "Ingrese un número de operación válido.")
    private String numeroOperacion;
}
