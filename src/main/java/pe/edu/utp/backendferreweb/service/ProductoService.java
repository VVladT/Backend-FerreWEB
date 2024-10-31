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

import java.io.IOException;
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
    private final FileService fileService;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAllActive();
    }

    public Producto obtenerPorId(Integer id) {
        Producto producto = productoRepository.findActiveById(id);

        if (producto == null) throw new EntityNotFoundException("El producto no existe.");

        return producto;
    }

    @Transactional
    public Producto crearProducto(ProductoRequest request, MultipartFile imagen) throws IOException {
        Categoria categoria = categoriaService.obtenerPorNombre(request.getCategoria());
        Unidad unidadPorDefecto = unidadService.obtenerPorNombre(request.getUnidadPorDefecto());
        Producto nuevoProducto = Producto.builder()
                .categoria(categoria)
                .unidadPorDefecto(unidadPorDefecto)
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .build();

        nuevoProducto = productoRepository.save(nuevoProducto);

        if (imagen != null && !imagen.isEmpty()) {
            String rutaImagen = fileService.uploadImage(imagen, "producto\\" + nuevoProducto.getIdProducto());
            nuevoProducto.setRutaImagen(rutaImagen);
        }

        for (UnidadProductoRequest unidadProductoRequest : request.getUnidadesPermitidas()) {
            unidadesPorProductoService.registrarUnidadesPorProducto(nuevoProducto, unidadProductoRequest);
        }

        for (AlmacenCantidadRequest almacenCantidadRequest : request.getAlmacenes()) {
            productosPorAlmacenService.registrarProductosPorAlmacen(nuevoProducto, almacenCantidadRequest);
        }

        return nuevoProducto;
    }

    @Transactional
    public Producto editarProducto(Integer id, ProductoRequest request, MultipartFile imagen) throws IOException {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));

        productoExistente.setNombre(request.getNombre());
        productoExistente.setDescripcion(request.getDescripcion());

        Categoria nuevaCategoria = categoriaService.obtenerPorNombre(request.getCategoria());
        productoExistente.setCategoria(nuevaCategoria);

        Unidad nuevaUnidadPorDefecto = unidadService.obtenerPorNombre(request.getUnidadPorDefecto());
        productoExistente.setUnidadPorDefecto(nuevaUnidadPorDefecto);

        if (imagen != null && !imagen.isEmpty()) {
            String rutaImagen = fileService.uploadImage(imagen, "producto\\" + productoExistente.getIdProducto());
            productoExistente.setRutaImagen(rutaImagen);
        }

        for (UnidadProductoRequest unidadProductoRequest : request.getUnidadesPermitidas()) {
            unidadesPorProductoService.registrarUnidadesPorProducto(productoExistente, unidadProductoRequest);
        }

        for (AlmacenCantidadRequest almacenCantidadRequest : request.getAlmacenes()) {
            productosPorAlmacenService.registrarProductosPorAlmacen(productoExistente, almacenCantidadRequest);
        }

        return productoRepository.save(productoExistente);
    }

    public void eliminarProducto(Integer id) {
        Producto productoParaEliminar = productoRepository.findActiveById(id);

        if (productoParaEliminar == null) throw new EntityNotFoundException("El producto no existe.");

        productoParaEliminar.setFechaEliminado(LocalDateTime.now());

        productoRepository.save(productoParaEliminar);
    }
}
