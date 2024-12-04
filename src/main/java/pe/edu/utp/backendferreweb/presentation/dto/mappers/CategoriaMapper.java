package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.Categoria;
import pe.edu.utp.backendferreweb.presentation.dto.request.CategoriaRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.CategoriaResponse;

@Component
public class CategoriaMapper {
    public Categoria toEntity(CategoriaRequest request) {
        if (request == null) return null;

        return Categoria.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .build();
    }

    public CategoriaResponse toResponse(Categoria categoria) {
        if (categoria == null) return null;

        return CategoriaResponse.builder()
                .idCategoria(categoria.getIdCategoria())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .rutaImagen(categoria.getRutaImagen())
                .build();
    }
}
