package pe.edu.utp.backendferreweb.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import pe.edu.utp.backendferreweb.auth.config.ExpireTimeConfig;
import pe.edu.utp.backendferreweb.util.TimeConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ExpireTimeConfigTest {
    @Value("${spring.security.jwt.expire.days:1}")
    private int days;

    @Value("${spring.security.jwt.expire.hours:0}")
    private int hours;

    @Value("${spring.security.jwt.expire.minutes:0}")
    private int minutes;

    @Autowired
    private ExpireTimeConfig expireTime;

    @Test
    void testValuesOfTime() {
        assertEquals(expireTime.getMillis(), TimeConverter.getMillis(days, hours, minutes));
    }
}
