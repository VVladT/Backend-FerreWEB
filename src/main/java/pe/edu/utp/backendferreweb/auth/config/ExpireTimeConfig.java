package pe.edu.utp.backendferreweb.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.util.TimeConverter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.jwt.expire")
public class ExpireTimeConfig {
    private int days = 1;
    private int hours = 0;
    private int minutes = 0;

    public int getMillis() {
        return TimeConverter.getMillis(days, hours, minutes);
    }
}
