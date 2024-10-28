package pe.edu.utp.backendferreweb.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseInitializer {
    @Value("${firebase.auth.private-key}")
    private String privateKey;

    @PostConstruct
    public void init() {
        try {
            FileInputStream serviceAccount = new FileInputStream(privateKey);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException ignore) {
        }
    }
}
