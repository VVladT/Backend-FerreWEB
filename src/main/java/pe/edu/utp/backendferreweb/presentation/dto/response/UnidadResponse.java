package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnidadResponse {
    private Integer idUnidad;
    private String nombre;
}
