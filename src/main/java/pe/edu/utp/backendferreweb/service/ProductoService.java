package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.persistence.model.*;
import pe.edu.utp.backendferreweb.persistence.repository.*;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.ProductoMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.AlmacenCantidadRequest;
import pe.edu.utp.backendferreweb.presentation.dto.request.ProductoRequest;
import pe.edu.utp.backendferreweb.presentation.dto.request.UnidadProductoRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.ProductoResponse;
import pe.edu.utp.backendferreweb.service.external.StorageService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;
    private final UnidadService unidadService;
    private final UnidadesPorProductoService unidadesPorProductoService;
    private final ProductosPorAlmacenService productosPorAlmacenService;
    private final StorageService storageService;
    private final ProductoMapper productoMapper;

    public List<ProductoResponse> obtenerTodos() {
        return productoRepository.findAllActive().stream()
                .map(productoMapper::toResponse)
                .toList();
    }

    public ProductoResponse obtenerPorId(Integer id) {
        Producto producto = productoRepository.findActiveById(id);

        if (producto == null) throw new EntityNotFoundException("No existe producto con id: " + id);

        return productoMapper.toResponse(producto);
    }

    public ProductoResponse crearProducto(ProductoRequest request, MultipartFile imagen) {
        String nombre = request.getNombre();
        String descripcion = request.getDescripcion();
        String categoriaStr = request.getCategoria();
        String unidadDefStr = request.getUnidadPorDefecto();

        if (nombre == null) throw new IllegalArgumentException("El nombre no puede ser nulo");
        if (nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío");
        if (descripcion == null) throw new IllegalArgumentException("La descripción no puede ser nula");
        if (descripcion.isBlank()) throw new IllegalArgumentException("La descripción no puede estar vacía");
        if (categoriaStr == null) throw new IllegalArgumentException("La categoría no puede ser nula");
        if (categoriaStr.isBlank()) throw new IllegalArgumentException("La categoría no puede estar vacía");
        if (unidadDefStr == null) throw new IllegalArgumentException("La unidad por defecto no puede ser nula");
        if (unidadDefStr.isBlank()) throw new IllegalArgumentException("La unidad por defecto no puede estar vacía");
        if (imagen == null) throw new IllegalArgumentException("La imagen no puede ser nula");
        if (imagen.isEmpty()) throw new IllegalArgumentException("La imagen no puede estar vacía");

        Producto producto = productoMapper.toEntity(request);
        Producto nuevoProducto = productoRepository.save(producto);

        Integer idProducto = nuevoProducto.getIdProducto();

        request.getAlmacenes().forEach(almacen -> almacen.setIdProducto(idProducto));
        request.getUnidadesPermitidas().forEach(unidad -> unidad.setIdProducto(idProducto));
        String rutaImagen = storageService.uploadImage(imagen, "producto/" + idProducto + ".webp");

        nuevoProducto = productoMapper.toEntity(request);
        nuevoProducto.setIdProducto(idProducto);

        asignarUnidadesPermitidas(request.getUnidadesPermitidas(), nuevoProducto);
        asignarAlmacenes(request.getAlmacenes(), nuevoProducto);

        nuevoProducto.setRutaImagen(rutaImagen);
        nuevoProducto = productoRepository.save(nuevoProducto);

        return productoMapper.toResponse(nuevoProducto);
    }

    public ProductoResponse editarProducto(Integer id, ProductoRequest request, MultipartFile imagen) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));

        String nuevoNombre = request.getNombre();
        String nuevaDescripcion = request.getDescripcion();
        String nuevaCategoriaStr = request.getCategoria();
        String nuevaUnidadDefStr = request.getUnidadPorDefecto();

        if (nuevoNombre != null) {
            if (nuevoNombre.isBlank())
                throw new IllegalArgumentException("El nuevo nombre no puede estar vacío");
            productoExistente.setNombre(request.getNombre());
        }

        if (nuevaDescripcion != null) {
            if (nuevaDescripcion.isBlank())
                throw new IllegalArgumentException("La nueva descripción no puede estar vacío");

            productoExistente.setDescripcion(request.getDescripcion());
        }

        if (nuevaCategoriaStr != null) {
            if (nuevaCategoriaStr.isBlank())
                throw new IllegalArgumentException("La nueva categoría no puede estar vacía");

            Categoria nuevaCategoria = categoriaService.obtenerPorNombre(request.getCategoria());
            productoExistente.setCategoria(nuevaCategoria);
        }

        if (nuevaUnidadDefStr != null) {
            if (nuevaUnidadDefStr.isBlank())
                throw new IllegalArgumentException("La nueva unidad por defecto no puede estar vacía");

            Unidad nuevaUnidadPorDefecto = unidadService.obtenerPorNombre(request.getUnidadPorDefecto());
            productoExistente.setUnidadPorDefecto(nuevaUnidadPorDefecto);
        }

        asignarUnidadesPermitidas(request.getUnidadesPermitidas(), productoExistente);
        asignarAlmacenes(request.getAlmacenes(), productoExistente);

        if (imagen != null && !imagen.isEmpty()) {
            String rutaImagen = storageService.uploadImage(imagen, "producto/" + id + ".webp");
            productoExistente.setRutaImagen(rutaImagen);
        }

        Producto productoActualizado = productoRepository.save(productoExistente);

        return productoMapper.toResponse(productoActualizado);
    }

    public void eliminarProducto(Integer id) {
        Producto productoParaEliminar = productoRepository.findActiveById(id);

        if (productoParaEliminar == null) throw new EntityNotFoundException("El producto no existe.");

        productoParaEliminar.setFechaEliminado(LocalDateTime.now());

        productoRepository.save(productoParaEliminar);
    }

    public Producto obtenerEntidadPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe producto con id: " + id));
    }

    private void asignarUnidadesPermitidas(List<UnidadProductoRequest> unidadesPermitidas, Producto nuevoProducto) {
        if (unidadesPermitidas != null) {
            nuevoProducto.setUnidadesPermitidas(new HashSet<>());

            for (UnidadProductoRequest unidadProductoRequest : unidadesPermitidas) {
                UnidadesPorProducto unidadPorProducto =
                        unidadesPorProductoService.registrarUnidadesPorProducto(nuevoProducto, unidadProductoRequest);
                nuevoProducto.addUnidadPermitida(unidadPorProducto);
            }
        }
    }

    private void asignarAlmacenes(List<AlmacenCantidadRequest> almacenes, Producto nuevoProducto) {
        if (almacenes != null) {
            nuevoProducto.setAlmacenes(new HashSet<>());

            for (AlmacenCantidadRequest almacenCantidadRequest : almacenes) {
                ProductosPorAlmacen productosPorAlmacen =
                        productosPorAlmacenService.registrarProductosPorAlmacen(nuevoProducto, almacenCantidadRequest);
                nuevoProducto.addAlmacen(productosPorAlmacen);
            }
        }
    }
}
