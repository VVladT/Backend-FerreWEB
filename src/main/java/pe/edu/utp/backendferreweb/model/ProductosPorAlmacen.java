package pe.edu.utp.backendferreweb.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Productos_por_Almacen")
public class ProductosPorAlmacen {
    @EmbeddedId
    private ProductosPorAlmacenPK primaryKey;

    @MapsId("idProducto")
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @MapsId("idAlmacen")
    @ManyToOne
    @JoinColumn(name = "id_almacen")
    private Almacen almacen;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
}

