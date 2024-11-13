package pe.edu.utp.backendferreweb.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_unidad")
    private Unidad unidadPorDefecto;

    @OneToMany(mappedBy = "producto")
    private Set<UnidadesPorProducto> unidades;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminado;

    @Column(name = "imagen")
    private String rutaImagen;

    @OneToMany(mappedBy = "producto")
    private Set<ProductosPorAlmacen> productosPorAlmacen;

    @PrePersist
    @PreUpdate
    public void calcularStock() {
        stock = 0;

        if (productosPorAlmacen != null && !productosPorAlmacen.isEmpty()) {
            for (ProductosPorAlmacen productoAlmacen : productosPorAlmacen) {
                stock += productoAlmacen.getCantidad();
            }
        }
    }
}
