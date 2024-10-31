package pe.edu.utp.backendferreweb.util.conversion;

import com.luciad.imageio.webp.WebPWriteParam;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageConverter {
    @Value("${firebase.upload.default-filetype}")
    private static String defaultType = "image/webp";

    public static byte[] adaptImageToUpload(byte[] imageBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage bi = ImageIO.read(bais);

        ImageWriter writer = ImageIO.getImageWritersByMIMEType(defaultType).next();
        if (writer == null) {
            throw new IllegalStateException("No ImageWriter found for format " + defaultType);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);

        ImageWriteParam writeParam = writer.getDefaultWriteParam();
        configParams(writeParam);
        writer.write(null, new IIOImage(bi, null, null), writeParam);
        ios.close();

        return baos.toByteArray();
    }

    private static void configParams(ImageWriteParam writeParam) {
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
        writeParam.setCompressionQuality(0.60f);
    }
}
