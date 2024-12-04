package pe.edu.utp.backendferreweb.presentation.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProveedorRequest {
    private String ruc;
    private String nombre;
    private String nombreComercial;
    private String email;
    private String telefono;
    private String direccion;
}
