package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UsuarioResponse {
    private Integer id;
    private String dni;
    private String username;
    private String nombreCompleto;
    private String nombre;
    private String apellidoPat;
    private String apellidoMat;
    private String direccion;
    private List<RolResponse> roles;
    private String rutaImagen;
    private String fechaEliminacion;
}
