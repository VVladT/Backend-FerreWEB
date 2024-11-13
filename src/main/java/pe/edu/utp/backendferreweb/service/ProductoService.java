package pe.edu.utp.backendferreweb.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.persistence.model.*;
import pe.edu.utp.backendferreweb.persistence.repository.*;
import pe.edu.utp.backendferreweb.presentation.dto.request.AlmacenCantidadRequest;
import pe.edu.utp.backendferreweb.presentation.dto.request.ProductoRequest;
import pe.edu.utp.backendferreweb.presentation.dto.request.UnidadProductoRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;
    private final UnidadService unidadService;
    private final UnidadesPorProductoService unidadesPorProductoService;
    private final ProductosPorAlmacenService productosPorAlmacenService;
    private final StorageService storageService;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAllActive();
    }

    public Producto obtenerPorId(Integer id) {
        Producto producto = productoRepository.findActiveById(id);

        if (producto == null) throw new EntityNotFoundException("El producto no existe.");

        return producto;
    }

    @Transactional
    public Producto crearProducto(ProductoRequest request, MultipartFile imagen) {
        Categoria categoria = categoriaService.obtenerPorNombre(request.getCategoria());
        Unidad unidadPorDefecto = unidadService.obtenerPorNombre(request.getUnidadPorDefecto());
        Producto nuevoProducto = Producto.builder()
                .categoria(categoria)
                .unidadPorDefecto(unidadPorDefecto)
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .build();

        nuevoProducto = productoRepository.save(nuevoProducto);

        return actualizarCargaDeProducto(nuevoProducto, request, imagen);
    }

    @Transactional
    public Producto editarProducto(Integer id, ProductoRequest request, MultipartFile imagen) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));

        productoExistente.setNombre(request.getNombre());
        productoExistente.setDescripcion(request.getDescripcion());

        Categoria nuevaCategoria = categoriaService.obtenerPorNombre(request.getCategoria());
        productoExistente.setCategoria(nuevaCategoria);

        Unidad nuevaUnidadPorDefecto = unidadService.obtenerPorNombre(request.getUnidadPorDefecto());
        productoExistente.setUnidadPorDefecto(nuevaUnidadPorDefecto);

        return actualizarCargaDeProducto(productoExistente, request, imagen);
    }

    public void eliminarProducto(Integer id) {
        Producto productoParaEliminar = productoRepository.findActiveById(id);

        if (productoParaEliminar == null) throw new EntityNotFoundException("El producto no existe.");

        productoParaEliminar.setFechaEliminado(LocalDateTime.now());

        productoRepository.save(productoParaEliminar);
    }

    private Producto actualizarCargaDeProducto(Producto producto, ProductoRequest request, MultipartFile imagen) {
        if (imagen != null && !imagen.isEmpty()) {
            String rutaImagen = storageService.uploadImage(imagen, "producto/" + producto.getIdProducto());
            producto.setRutaImagen(rutaImagen);
        }

        for (UnidadProductoRequest unidadProductoRequest : request.getUnidadesPermitidas()) {
            unidadesPorProductoService.registrarUnidadesPorProducto(producto, unidadProductoRequest);
        }

        for (AlmacenCantidadRequest almacenCantidadRequest : request.getAlmacenes()) {
            productosPorAlmacenService.registrarProductosPorAlmacen(producto, almacenCantidadRequest);
        }

        return productoRepository.save(producto);
    }
}
