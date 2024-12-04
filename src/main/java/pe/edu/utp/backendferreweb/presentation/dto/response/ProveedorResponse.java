package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProveedorResponse {
    private Integer idProveedor;
    private String ruc;
    private String nombre;
    private String nombreComercial;
    private String email;
    private String telefono;
    private String direccion;
}
