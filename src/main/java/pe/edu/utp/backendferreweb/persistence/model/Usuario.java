package pe.edu.utp.backendferreweb.persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static pe.edu.utp.backendferreweb.util.conversion.BlobConverter.blobToUtf8;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Usuario")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "dni", nullable = false)
    private String dni;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido_paterno", nullable = false)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", nullable = false)
    private String apellidoMaterno;

    @Email
    @Column(name = "username")
    private String username;

    @Column(name = "direccion")
    private String direccion;

    @Lob
    @Column(name = "contraseña", nullable = false)
    private byte[] contrasena;

    @Column(name = "imagen")
    private String rutaImagen;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminado;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Roles_por_Usuario",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Rol> roles;

    public boolean addRol(Rol rol) {
        if (roles == null) roles = new HashSet<>();
        return roles.add(rol);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return blobToUtf8(contrasena);
    }

    @Override
    public String getUsername() {
        return username;
    }
}
