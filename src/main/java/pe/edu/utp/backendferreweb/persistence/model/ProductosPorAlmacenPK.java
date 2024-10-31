package pe.edu.utp.backendferreweb.persistence.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class ProductosPorAlmacenPK implements Serializable {
    private Integer idProducto, idAlmacen;
}
