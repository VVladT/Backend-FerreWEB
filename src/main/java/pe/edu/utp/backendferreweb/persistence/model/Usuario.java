package pe.edu.utp.backendferreweb.persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
    @Column(name = "email")
    private String email;

    @Pattern(regexp = "^9\\d{8}$")
    @Column(name = "telefono")
    private String telefono;

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
        boolean emailNonExists = email == null || email.isBlank();
        boolean telefonoNonExists = telefono == null || telefono.isBlank();

        if (emailNonExists && telefonoNonExists) {
            throw new UsernameNotFoundException("The user has not a email or telephone.");
        }

        return emailNonExists ? telefono : email;
    }
}
