package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.persistence.model.Categoria;
import pe.edu.utp.backendferreweb.persistence.repository.CategoriaRepository;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.CategoriaMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.CategoriaRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.CategoriaResponse;
import pe.edu.utp.backendferreweb.service.external.StorageService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final StorageService storageService;
    private final CategoriaMapper categoriaMapper;

    public List<CategoriaResponse> obtenerTodas() {
        return categoriaRepository.findAllActive().stream()
                .map(categoriaMapper::toResponse)
                .toList();
    }

    public CategoriaResponse obtenerPorId(Integer id) {
        Categoria categoria = categoriaRepository.findActiveById(id);

        if (categoria == null) throw new EntityNotFoundException("No existe categoría con id: " + id);

        return categoriaMapper.toResponse(categoria);
    }

    public CategoriaResponse crearCategoria(CategoriaRequest request, MultipartFile imagen) {
        String nombre = request.getNombre();
        String descripcion = request.getDescripcion();

        if (nombre == null) throw new IllegalArgumentException("El nombre no puede ser nulo");
        if (nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío");
        if (descripcion == null) throw new IllegalArgumentException("La descripción no puede ser nula");
        if (descripcion.isBlank()) throw new IllegalArgumentException("La descripción no puede estar vacía");

        if (categoriaRepository.existsByNombre(nombre)) {
            throw new EntityExistsException("Ya existe una categoría con nombre: " + nombre);
        }

        Categoria categoria = categoriaMapper.toEntity(request);

        Categoria categoriaGuardada = categoriaRepository.save(categoria);

        if (imagen != null && !imagen.isEmpty()) {
            String rutaImagen = storageService.uploadImage(imagen, "categoria/" + categoriaGuardada.getIdCategoria() + ".webp");
            categoriaGuardada.setRutaImagen(rutaImagen);
            categoriaGuardada = categoriaRepository.save(categoriaGuardada);
        }

        return categoriaMapper.toResponse(categoriaGuardada);
    }

    public CategoriaResponse editarCategoria(Integer id, CategoriaRequest request, MultipartFile imagen) {
        String nuevoNombre = request.getNombre();
        String nuevaDescripcion = request.getDescripcion();
        Categoria categoriaParaActualizar = categoriaRepository.findActiveById(id);

        if (categoriaParaActualizar == null) throw new EntityNotFoundException("No existe categoria con id: " + id);

        if (!categoriaParaActualizar.getNombre().equals(nuevoNombre) &&
                categoriaRepository.existsByNombre(nuevoNombre)) {
            throw new EntityExistsException("Ya existe categoría con el nombre: " + nuevoNombre);
        }

        if (nuevoNombre != null) {
            if (nuevoNombre.isBlank())
                throw new IllegalArgumentException("El nuevo nombre no puede estar vacío");

            categoriaParaActualizar.setNombre(nuevoNombre);
        }

        if (nuevaDescripcion != null) {
            if (nuevaDescripcion.isBlank())
                throw new IllegalArgumentException("La nueva descripción no puede estar vacía");

            categoriaParaActualizar.setDescripcion(nuevaDescripcion);
        }

        if (imagen != null && !imagen.isEmpty()) {
            String rutaImagen = storageService.uploadImage(imagen, "categoria/" + categoriaParaActualizar.getIdCategoria() + ".webp");
            categoriaParaActualizar.setRutaImagen(rutaImagen);
        }

        Categoria categoriaActualizada = categoriaRepository.save(categoriaParaActualizar);

        return categoriaMapper.toResponse(categoriaActualizada);
    }

    public void eliminarCategoria(Integer id) {
        if (categoriaRepository.isAssociatedWithProduct(id))
            throw new DataIntegrityViolationException("No se pudo eliminar la categoría debido a que está asociada con un producto");

        Categoria categoriaParaEliminar = categoriaRepository.findActiveById(id);

        if (categoriaParaEliminar == null)
            throw new EntityNotFoundException("No existe categoría con id: " + id);

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
