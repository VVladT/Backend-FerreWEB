package pe.edu.utp.backendferreweb.auth.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String user;
    private String dni;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String contrasena;
}
