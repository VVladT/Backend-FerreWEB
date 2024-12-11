package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetodoPagoResponse {
    private Integer idMetodoPago;
    private String nombre;
}
