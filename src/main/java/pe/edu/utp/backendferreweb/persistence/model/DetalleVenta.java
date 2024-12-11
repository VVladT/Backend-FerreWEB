package pe.edu.utp.backendferreweb.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalleCompra;

    @OneToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @OneToOne
    @JoinColumn(name = "id_unidad")
    private Unidad unidad;

    @Column(name = "cantidad")
    private Double cantidad;

    @Column(name = "precio_unitario")
    private Double precioUnitario;

    @Column(name = "subtotal")
    private Double subtotal;

    @PrePersist
    @PreUpdate
    public void calcularValores() {
        calcularSubtotal();
    }

    private void calcularSubtotal() {
        subtotal = cantidad * precioUnitario;
    }
}