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
@Table(name = "tipo_entrega")
public class TipoEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_entrega")
    private Integer idTipoEntrega;

    @Column(name = "tipo")
    private String tipo;
}
