package pe.edu.utp.backendferreweb.service;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.util.conversion.ImageConverter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {
    @Value("${firebase.storage.bucket-name}")
    private String bucketName;

    @Value("${firebase.upload.default-filetype}")
    private String defaultType;

    @Value("${firebase.storage.url-base}")
    private String baseUrl;

    public String uploadImage(MultipartFile image, String fileName) throws IOException {
        byte[] adaptedImage = ImageConverter.adaptImageToUpload(image.getBytes());

        return uploadFile(adaptedImage, fileName);
    }


    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        return uploadFile(file.getBytes(), fileName);
    }

    public String uploadFile(byte[] fileBytes, String fileName) {
        return uploadFile(fileBytes, fileName, defaultType);
    }

    public String uploadFile(MultipartFile file, String fileName, String type) throws IOException {
        return uploadFile(file.getBytes(), fileName, type);
    }

    public String uploadFile(byte[] fileBytes, String fileName, String type) {
        Bucket bucket = StorageClient.getInstance().bucket(bucketName);
        bucket.create(fileName, fileBytes, type);
        return baseUrl + "/b/" + bucketName + "/o/" + fileName + "?alt=media";
    }
}
