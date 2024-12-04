package pe.edu.utp.backendferreweb.presentation.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductoRequest {
    private String categoria;
    private String unidadPorDefecto;
    private List<UnidadProductoRequest> unidadesPermitidas;
    private List<AlmacenCantidadRequest> almacenes;
    private String nombre;
    private String descripcion;
}
