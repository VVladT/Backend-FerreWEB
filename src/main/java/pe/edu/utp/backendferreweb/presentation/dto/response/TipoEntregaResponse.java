package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TipoEntregaResponse {
    private Integer idTipoEntrega;
    private String tipo;
}
