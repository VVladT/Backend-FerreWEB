package pe.edu.utp.backendferreweb.presentation.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnidadRequest {
    private String nombre;
}
