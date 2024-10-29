package pe.edu.utp.backendferreweb.model;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private Set<UnidadesPorProducto> unidades;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "disponible", nullable = false)
    private Boolean disponible;

    @Column(name = "imagen")
    private String rutaImagen;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private Set<ProductosPorAlmacen> productosPorAlmacen;
}
