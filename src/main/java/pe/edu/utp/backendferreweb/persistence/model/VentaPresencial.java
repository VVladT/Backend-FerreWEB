package pe.edu.utp.backendferreweb.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "venta_presencial")
@PrimaryKeyJoinColumn(name = "id_venta_presencial",
        referencedColumnName = "id_venta")
public class VentaPresencial extends Venta {
    @Column(name = "dni_cliente")
    private String dniCliente;
}
