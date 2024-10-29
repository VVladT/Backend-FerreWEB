package pe.edu.utp.backendferreweb.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Unidades_por_Producto")
public class UnidadesPorProducto {
    @EmbeddedId
    private UnidadesPorProductoPK primaryKey;

    @MapsId("idProducto")
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @MapsId("idUnidad")
    @ManyToOne
    @JoinColumn(name = "id_unidad")
    private Unidad unidad;

    @Column(name = "precio")
    private Double precio;
}

