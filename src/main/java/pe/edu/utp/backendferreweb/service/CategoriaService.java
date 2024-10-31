package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.persistence.model.Categoria;
import pe.edu.utp.backendferreweb.persistence.repository.CategoriaRepository;
import pe.edu.utp.backendferreweb.presentation.dto.request.CategoriaRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final FileService fileService;

    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAllActive();
    }

    public Categoria obtenerPorId(Integer id) {
        Categoria categoria = categoriaRepository.findActiveById(id);

        if (categoria == null) throw new EntityNotFoundException("No existe categoría con id: " + id);

        return categoria;
    }

    public Categoria crearCategoria(CategoriaRequest request, MultipartFile imagen) throws IOException {
        String nombre = request.getNombre();
        String descripcion = request.getDescripcion();

        if (categoriaRepository.existsByNombre(nombre)) {
            throw new EntityExistsException("Ya existe una categoría con nombre: " + nombre);
        } else {
            Categoria categoria = Categoria.builder()
                    .nombre(nombre)
                    .descripcion(descripcion)
                    .build();

            Categoria categoriaGuardada = categoriaRepository.save(categoria);

            if (imagen != null && !imagen.isEmpty()) {
                String rutaImagen = fileService.uploadImage(imagen, "categoria\\" + categoria.getIdCategoria());
                categoriaGuardada.setRutaImagen(rutaImagen);
            }

            return categoriaRepository.save(categoriaGuardada);
        }
    }

    public Categoria editarCategoria(Integer id, CategoriaRequest request, MultipartFile imagen) throws IOException {
        String nuevoNombre = request.getNombre();
        String nuevaDescripcion = request.getDescripcion();

        if (categoriaRepository.existsByNombre(nuevoNombre)) {
            throw new EntityExistsException("La categoría con el nombre \""
                    + nuevoNombre + "\" ya existe.");
        } else {
            Categoria categoriaParaActualizar = categoriaRepository.findActiveById(id);

            if (categoriaParaActualizar == null) throw new EntityNotFoundException("La categoría no existe.");

            categoriaParaActualizar.setNombre(nuevoNombre);
            categoriaParaActualizar.setDescripcion(nuevaDescripcion);

            if (imagen != null && !imagen.isEmpty()) {
                String rutaImagen = fileService.uploadImage(imagen, "categoria\\" + categoriaParaActualizar.getIdCategoria());
                categoriaParaActualizar.setRutaImagen(rutaImagen);
            }

            return categoriaRepository.save(categoriaParaActualizar);
        }
    }

    public void eliminarCategoria(Integer id) {
        Categoria categoriaParaEliminar = categoriaRepository.findActiveById(id);

        if (categoriaParaEliminar == null) throw new EntityNotFoundException("La categoría no existe.");

        categoriaParaEliminar.setFechaEliminado(LocalDateTime.now());

        categoriaRepository.save(categoriaParaEliminar);
    }

    public Categoria obtenerPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .orElseGet(() -> categoriaRepository.save(Categoria.builder()
                                .nombre(nombre)
                                .build()));
    }
}
