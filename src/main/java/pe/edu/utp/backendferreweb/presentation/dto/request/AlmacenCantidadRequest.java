package pe.edu.utp.backendferreweb.presentation.dto.request;

import lombok.Data;

@Data
public class AlmacenCantidadRequest {
    private Integer idProducto;
    private String nombreAlmacen;
    private Integer cantidadProductos;
}
