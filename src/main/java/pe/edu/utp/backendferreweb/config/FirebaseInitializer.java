package pe.edu.utp.backendferreweb.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

@Configuration
public class FirebaseInitializer {
    @Value("${firebase.auth.private-key}")
    private String privateKeyPath;

    @PostConstruct
    public void init() {
        try {
            File jsonPrivateKey = new File(this.getClass().getClassLoader()
                    .getResource(privateKeyPath).toURI());
            FileInputStream serviceAccount = new FileInputStream(jsonPrivateKey);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
