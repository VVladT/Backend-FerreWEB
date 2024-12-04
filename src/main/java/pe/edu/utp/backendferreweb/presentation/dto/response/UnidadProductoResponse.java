package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnidadProductoResponse {
    private Integer idProducto;
    private UnidadResponse unidad;
    private Double precio;
    private Double equivalencia;
}
