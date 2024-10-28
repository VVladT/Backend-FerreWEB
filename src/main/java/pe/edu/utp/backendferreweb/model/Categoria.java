package pe.edu.utp.backendferreweb.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "nombre_categoria")
    private String nombreCategoria;

    @Column(name = "descripcion")
    private String descripcion;
}
