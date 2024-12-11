package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class FileResponse {
    private File file;
    private String name;
    private String mimeType;
    private String extension;
}
