package pe.edu.utp.backendferreweb.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Table Proveedor {
 *   id_proveedor INT [primary key, increment] // Identificador único
 *   ruc CHAR(11) [not null]
 *   nombre VARCHAR(100) [not null, unique] // Nombre del proveedor
 *   nombre_comercial VARCHAR(60) [null]
 *   email VARCHAR(100) [not null] // Email del proveedor
 *   telefono VARCHAR(15) [not null] // Teléfono del proveedor
 *   direccion VARCHAR(100) [not null] // Dirección del proveedor
 * }
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Proveedor")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;


}
