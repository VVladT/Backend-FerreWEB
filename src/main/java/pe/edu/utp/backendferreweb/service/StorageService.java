package pe.edu.utp.backendferreweb.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.exceptions.FileUploadException;
import pe.edu.utp.backendferreweb.util.conversion.ImageConverter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final FirebaseApp firebaseApp;

    @Value("${firebase.storage.bucket-name}")
    private String bucketName;

    @Value("${firebase.upload.default-filetype}")
    private String defaultType;

    @Value("${firebase.storage.url-base}")
    private String baseUrl;

    @SneakyThrows
    public String uploadImage(MultipartFile image, String fileName) {
        byte[] adaptedImage = null;
        try {
            adaptedImage = ImageConverter.adaptImageToUpload(image.getBytes());
        } catch (IOException e) {
            throw new FileUploadException("No se pudo subir la imagen.");
        }

        return uploadFile(adaptedImage, fileName);
    }


    @SneakyThrows
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            return uploadFile(file.getBytes(), fileName);
        } catch (IOException e) {
            throw new FileUploadException("No se pudo subir el archivo.");
        }
    }

    public String uploadFile(byte[] fileBytes, String fileName) {
        return uploadFile(fileBytes, fileName, defaultType);
    }

    @SneakyThrows
    public String uploadFile(MultipartFile file, String fileName, String type) {
        try {
            return uploadFile(file.getBytes(), fileName, type);
        } catch (IOException e) {
            throw new FileUploadException("No se pudo subir el archivo.");
        }
    }

    public String uploadFile(byte[] fileBytes, String fileName, String type) {
        StorageClient storageClient = StorageClient.getInstance(firebaseApp);
        storageClient.bucket().create(fileName, fileBytes, type);
        return baseUrl + "/b/" + bucketName + "/o/" + fileName.replace("/", "%2F") + "?alt=media";
    }
}
