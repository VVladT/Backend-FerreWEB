package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VentaResponse {
    private Integer idVenta;
    private String estado;
    private String fecha;
    private Double total;
    private Double subtotal;
    private Double igv;
    private UsuarioResponse responsable;
    private String dniCliente;
    private List<DetalleVentaResponse> detalles;
}
