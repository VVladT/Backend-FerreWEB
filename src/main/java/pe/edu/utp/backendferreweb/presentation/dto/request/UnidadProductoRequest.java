package pe.edu.utp.backendferreweb.presentation.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnidadProductoRequest {
    private Integer idProducto;
    private String nombreUnidad;
    private Double precioPorUnidad;
    private Double equivalencia;
}
