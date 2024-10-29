package pe.edu.utp.backendferreweb.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ProductosPorAlmacenPK {
    private Integer idProducto, idAlmacen;
}
