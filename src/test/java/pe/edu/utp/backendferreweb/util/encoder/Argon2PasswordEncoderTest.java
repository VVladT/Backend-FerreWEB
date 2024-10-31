package pe.edu.utp.backendferreweb.util.encoder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

@ActiveProfiles("test")
@SpringBootTest
public class Argon2PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testEncodeAndMatches() {
        String[] passwords = {
                "5eCur1ty_Pa55w0rd*",
                "sadh398djcwuic_cdad3",
                "asdaiodmiaczx-cdad",
                "Adsadmcdi1234"
        };

        for (String password : passwords) {
            String hashedPassword = passwordEncoder.encode(password);
            Assertions.assertTrue(passwordEncoder.matches(password, hashedPassword),
                    "Password verification failed for: " + password);
        }
    }

    @Test
    void testIncorrectPassword() {
        String hashedPassword = passwordEncoder.encode("hola1308");
        Assertions.assertFalse(passwordEncoder.matches("HOLA1308", hashedPassword),
                "Verification should fail for incorrect password.");
    }

    @Test
    void testTimeOfEncrypt() {
        String[] passwords = {
                "5eCur1ty_Pa55w0rd*",
                "sadh398djcwuic_cdad3",
                "asdaiodmiaczx-cdad",
                "Adsadmcdi1234"
        };

        for (String password : passwords) {
            Assertions.assertTimeout(Duration.ofSeconds(2), () -> {
                passwordEncoder.encode(password);
            }, "Time exceeded durind encrypt to: " + password);
        }
    }
}
