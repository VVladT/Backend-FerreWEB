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
@Table(name = "venta")
@Inheritance(strategy = InheritanceType.JOINED)
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;

    @ManyToOne
    @JoinColumn(name = "id_estado_venta")
    private EstadoVenta estadoVenta;

    @ManyToOne
    @JoinColumn(name = "id_almacen")
    private Almacen almacen;

    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fecha;

    @Column(name = "total")
    private Double total;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "igv")
    private Double igv;

    @ManyToOne
    @JoinColumn(name = "id_usuario_responsable")
    private Usuario responsable;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_venta")
    private Set<DetalleVenta> detalles;

    public void addDetalle(DetalleVenta detalle) {
        if (detalles == null) detalles = new HashSet<>();
        detalles.add(detalle);
    }

    @PrePersist
    public void calcularValores() {
        calcularSubtotal();
        calcularIgv();
        calcularTotal();
    }

    private void calcularSubtotal() {
        subtotal = 0.0;

        if (detalles == null) return;
        if (detalles.isEmpty()) return;

        for (DetalleVenta detalle : detalles) {
            subtotal += detalle.getPrecioUnitario() * detalle.getCantidad();
        }
    }

    private void calcularIgv() {
        igv = (subtotal) * (0.18);
    }

    private void calcularTotal() {
        total = subtotal + igv;
    }
}
