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
@Table(name = "auditoria")
public class AuditoriaLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    @Column(name = "nombre_entidad")
    private String nombreEntidad;

    @Column(name = "accion")
    private String accion;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "fecha_cambio")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaCambio;

    @Column(name = "id_entidad")
    private Integer idEntidad;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
