package pe.edu.utp.backendferreweb.auth.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String user;
    private String contrasena;
}
