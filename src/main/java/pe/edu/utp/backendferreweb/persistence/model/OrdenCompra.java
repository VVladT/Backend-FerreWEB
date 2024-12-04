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
@Table(name = "orden_compra")
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden_compra")
    private Integer idOrdenCompra;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_almacen")
    private Almacen destino;

    @ManyToOne
    @JoinColumn(name = "id_estado_compra")
    private EstadoCompra estadoCompra;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    @Column(name = "precio_envio")
    private Double precioEnvio;

    @Column(name = "otros_pagos")
    private Double otrosPagos;

    @Column(name = "total")
    private Double total;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminado;
}
