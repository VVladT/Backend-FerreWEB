package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlmacenResponse {
    private Integer idAlmacen;
    private String nombre;
    private String direccion;
}
