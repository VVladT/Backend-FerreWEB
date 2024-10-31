package pe.edu.utp.backendferreweb.presentation.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ProductoRequest {
    private String categoria;
    private String unidadPorDefecto;
    private List<UnidadProductoRequest> unidadesPermitidas;
    private List<AlmacenCantidadRequest> almacenes;
    private String nombre;
    private String descripcion;
}
