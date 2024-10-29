package pe.edu.utp.backendferreweb.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class UnidadesPorProductoPK {
    private Integer idProducto, idUnidad;
}
