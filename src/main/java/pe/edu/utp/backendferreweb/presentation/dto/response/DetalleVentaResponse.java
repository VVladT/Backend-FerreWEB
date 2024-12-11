package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetalleVentaResponse {
    private Integer idDetalle;
    private Integer idProducto;
    private String producto;
    private String unidad;
    private Double cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
