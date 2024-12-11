package pe.edu.utp.backendferreweb.persistence.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Productos_por_Almacen")
public class ProductosPorAlmacen {
    @EmbeddedId
    private ProductosPorAlmacenPK primaryKey = new ProductosPorAlmacenPK();

    @MapsId("idProducto")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @MapsId("idAlmacen")
    @ManyToOne
    @JoinColumn(name = "id_almacen")
    private Almacen almacen;

    @Column(name = "cantidad", nullable = false)
    private Double cantidad;
}
