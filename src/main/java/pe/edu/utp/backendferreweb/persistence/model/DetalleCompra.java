package pe.edu.utp.backendferreweb.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "detalle_compra")
public class DetalleCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_compra")
    private Integer idDetalleCompra;

    @OneToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @OneToOne
    @JoinColumn(name = "id_unidad")
    private Unidad unidad;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario")
    private Double precioUnitario;

    @Column(name = "subtotal")
    private Double subtotal;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminado;

    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        subtotal = cantidad * precioUnitario;
    }
}
