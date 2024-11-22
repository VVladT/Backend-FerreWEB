package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolResponse {
    private int idRol;
    private String tipo;
    private String rutaImagen;
}
