package pe.edu.utp.backendferreweb.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    @OneToMany(mappedBy = "producto", fetch = FetchType.EAGER)
    private Set<UnidadesPorProducto> unidadesPermitidas;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "stock")
    private Double stock;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminado;

    @Column(name = "imagen")
    private String rutaImagen;

    @OneToMany(mappedBy = "producto", fetch = FetchType.EAGER)
    private Set<ProductosPorAlmacen> almacenes;

    public void addUnidadPermitida(UnidadesPorProducto unidadPermitida) {
        if (unidadesPermitidas == null) unidadesPermitidas = new HashSet<>();
        unidadesPermitidas.add(unidadPermitida);
    }

    public void addAlmacen(ProductosPorAlmacen almacen) {
        if (almacenes == null) almacenes = new HashSet<>();
        almacenes.add(almacen);
    }

    @PrePersist
    @PreUpdate
    public void calcularStock() {
        stock = 0.0;

        if (almacenes != null && !almacenes.isEmpty()) {
            for (ProductosPorAlmacen productoAlmacen : almacenes) {
                stock += productoAlmacen.getCantidad();
            }
        }
    }
}
