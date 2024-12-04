package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoriaResponse {
    private Integer idCategoria;
    private String nombre;
    private String descripcion;
    private String rutaImagen;
}
