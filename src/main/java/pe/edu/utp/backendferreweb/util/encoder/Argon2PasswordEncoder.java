package pe.edu.utp.backendferreweb.util.encoder;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.CharSequenceUtils.toCharArray;

@Component
public class Argon2PasswordEncoder implements PasswordEncoder {
    private static final Argon2 ARG2 = Argon2Factory.create();

    public String encode(CharSequence rawPassword) {
        return ARG2.hash(10, 65536, 1, toCharArray(rawPassword));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return ARG2.verify(encodedPassword, toCharArray(rawPassword));
    }
}
