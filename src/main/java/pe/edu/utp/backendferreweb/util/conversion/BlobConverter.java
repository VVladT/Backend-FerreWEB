package pe.edu.utp.backendferreweb.util.conversion;

import java.nio.charset.StandardCharsets;

public class BlobConverter {
    public static String blobToUtf8(byte[] blob) {
        return new String(blob, StandardCharsets.UTF_8);
    }

    public static byte[] utf8ToBlob(String text) {
        return text.getBytes(StandardCharsets.UTF_8);
    }
}
