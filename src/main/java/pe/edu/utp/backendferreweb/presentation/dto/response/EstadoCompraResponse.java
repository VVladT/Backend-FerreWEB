package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstadoCompraResponse {
    private Integer idEstadoCompra;
    private String estado;
}
