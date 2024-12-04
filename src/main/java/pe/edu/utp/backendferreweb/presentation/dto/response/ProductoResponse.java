package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductoResponse {
    private Integer idProducto;
    private CategoriaResponse categoria;
    private UnidadResponse unidadPorDefecto;
    private List<UnidadProductoResponse> unidadesPermitidas;
    private List<AlmacenProductoResponse> almacenes;
    private String nombre;
    private String descripcion;
    private Integer stock;
    private String rutaImagen;
}
