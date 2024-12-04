package pe.edu.utp.backendferreweb.persistence.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ProductosPorAlmacenPK implements Serializable {
    private Integer idProducto, idAlmacen;
}
