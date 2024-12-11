package pe.edu.utp.backendferreweb.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Rol")
public class Rol implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "tipo", unique = true, nullable = false)
    private String tipo;

    @Column(name = "imagen")
    private String rutaImagen;

    @Override
    public String getAuthority() {
        return getTipo();
    }
}
