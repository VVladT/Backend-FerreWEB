package pe.edu.utp.backendferreweb.service;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StorageService {
    @Value("${firebase.storage.bucket-name}")
    private String bucketName;

    @Value("${firebase.upload.default-filetype}")
    private String defaultType;

    public void uploadFile(MultipartFile file, String fileName) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket(bucketName);
        bucket.create(fileName, file.getBytes(), defaultType);
        System.out.println("File uploaded successfully: " + fileName);
    }

    public void uploadFile(byte[] fileBytes, String fileName) {
        Bucket bucket = StorageClient.getInstance().bucket(bucketName);
        bucket.create(fileName, fileBytes, defaultType);
        System.out.println("File uploaded successfully: " + fileName);
    }
}
