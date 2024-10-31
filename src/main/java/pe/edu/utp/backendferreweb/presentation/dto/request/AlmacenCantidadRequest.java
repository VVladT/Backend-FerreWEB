package pe.edu.utp.backendferreweb.presentation.dto.request;

import lombok.Data;

@Data
public class AlmacenCantidadRequest {
    private String nombreAlmacen;
    private int cantidadProductos;
}
