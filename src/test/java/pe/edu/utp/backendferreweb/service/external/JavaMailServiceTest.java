package pe.edu.utp.backendferreweb.service.external;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class JavaMailServiceTest {
    @Autowired
    JavaMailService javaMailService;

    @Test
    void sendSimpleEmail() {
        String destino = "vvladimirtm134@gmail.com";
        String asunto = "Prueba Mensaje Simple";
        String contenido = "Este es un mensaje de prueba, si deseas ignorarlo, puedes hacerlo.";

        javaMailService.sendSimpleEmail(destino, asunto, contenido);
    }

    @Test
    void sendEmailWithFile() {
    }
}