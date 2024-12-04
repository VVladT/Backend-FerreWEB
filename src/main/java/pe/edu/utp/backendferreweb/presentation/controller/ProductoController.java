package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.presentation.dto.request.ProductoRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.ProductoResponse;
import pe.edu.utp.backendferreweb.service.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> obtenerProductos() {
        List<ProductoResponse> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerProducto(@PathVariable Integer id) {
        ProductoResponse producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crearProducto(@RequestPart ProductoRequest request,
                                                  @RequestPart MultipartFile imagen) {
        ProductoResponse nuevoProducto = productoService.crearProducto(request, imagen);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizarProducto(@PathVariable Integer id,
                                                       @RequestPart ProductoRequest request,
                                                       @RequestPart(required = false) MultipartFile imagen) {
        ProductoResponse productoActualizado = productoService.editarProducto(id, request, imagen);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
