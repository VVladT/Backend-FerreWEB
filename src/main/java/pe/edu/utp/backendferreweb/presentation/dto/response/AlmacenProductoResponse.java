package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlmacenProductoResponse {
    private Integer idProducto;
    private AlmacenResponse almacen;
    private Integer cantidad;
}
