package pe.edu.utp.backendferreweb.presentation.dto.request;

import lombok.Data;

@Data
public class UnidadProductoRequest {
    private String nombreUnidad;
    private Double precioPorUnidad;
}
