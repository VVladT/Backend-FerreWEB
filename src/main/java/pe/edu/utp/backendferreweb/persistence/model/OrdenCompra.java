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
@Table(name = "orden_compra")
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden_compra")
    private Integer idOrdenCompra;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_orden_compra")
    private Set<DetalleCompra> detalles;

    @ManyToOne
    @JoinColumn(name = "id_usuario_autorizacion")
    private Usuario usuarioAutorizacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario_solicitante")
    private Usuario solicitante;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_almacen")
    private Almacen destino;

    @ManyToOne
    @JoinColumn(name = "id_estado_compra")
    private EstadoCompra estadoCompra;

    @ManyToOne
    @JoinColumn(name = "id_metodo_pago")
    private MetodoPago metodoPago;

    @ManyToOne
    @JoinColumn(name = "id_tipo_entrega")
    private TipoEntrega tipoEntrega;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "precio_envio")
    private Double precioEnvio;

    @Column(name = "otros_pagos")
    private Double otrosPagos;

    @Column(name = "igv")
    private Double igv;

    @Column(name = "total")
    private Double total;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_esperada")
    private LocalDateTime fechaEntregaEsperada;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_entregada")
    private LocalDateTime fechaEntregada;

    public void addDetalle(DetalleCompra detalle) {
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

        for (DetalleCompra detalle : detalles) {
            subtotal += detalle.getPrecioUnitario() * detalle.getCantidad();
        }
    }

    private void calcularIgv() {
        igv = (subtotal) * (0.18);
    }

    private void calcularTotal() {
        if (otrosPagos == null) {
            otrosPagos = 0.0;
        }

        total = subtotal + igv + precioEnvio + otrosPagos;
    }
}
