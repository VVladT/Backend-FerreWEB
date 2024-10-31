package pe.edu.utp.backendferreweb.util.conversion;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class ImageConverterTest {
    @Test
    void testAdaptImageToUpload() throws IOException {
        BufferedImage testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(testImage, "jpg", baos);
        byte[] imageBytes = baos.toByteArray();

        byte[] result = ImageConverter.adaptImageToUpload(imageBytes);
        assertNotNull(result);
        assertTrue(result.length > 0);
        assertTrue(result.length < imageBytes.length);
    }
}
