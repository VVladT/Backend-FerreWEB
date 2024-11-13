package pe.edu.utp.backendferreweb.config.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.SneakyThrows;
import org.hibernate.internal.util.config.ConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;

@Configuration
public class FirebaseConfig {
    @Value("${firebase.auth.private-key}")
    private String privateKeyPath;

    @Value("${firebase.storage.bucket-name}")
    private String bucketName;

    @Value("${firebase.project.id}")
    private String projectId;

    @SneakyThrows
    @Bean
    public FirebaseApp firebaseApp() {
        File privateKeyJson = null;
        FileInputStream serviceAccount = null;

        try {
            privateKeyJson = createTemporalFile();
            serviceAccount = new FileInputStream(privateKeyJson);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(bucketName)
                    .setProjectId(projectId)
                    .build();

            return FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new ConfigurationException(e.getMessage());
        } finally {
            if (serviceAccount != null) serviceAccount.close();
            if (privateKeyJson != null) privateKeyJson.delete();
        }
    }

    private File createTemporalFile() throws IOException {
        File privateKeyJson;
        try (InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(privateKeyPath)) {

            privateKeyJson = File.createTempFile("firebase-service-account", ".json");

            try (FileOutputStream outputStream = new FileOutputStream(privateKeyJson)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }

        return privateKeyJson;
    }
}
