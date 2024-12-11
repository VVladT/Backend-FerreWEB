package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.presentation.dto.request.CategoriaRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.CategoriaResponse;
import pe.edu.utp.backendferreweb.service.CategoriaService;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

import java.util.List;


@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> obtenerCategorias() {
        List<CategoriaResponse> categorias = categoriaService.obtenerTodas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerCategoria(@PathVariable Integer id) {
        CategoriaResponse categoria = categoriaService.obtenerPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crearCategoria(@RequestPart
                                                            @Validated(ValidCreacion.class)
                                                            CategoriaRequest request,
                                                            @RequestPart(required = false)
                                                            MultipartFile imagen) {
        CategoriaResponse nuevaCategoria = categoriaService.crearCategoria(request, imagen);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizarCategoria(@PathVariable Integer id,
                                                                 @RequestPart
                                                                 @Validated(ValidActualizacion.class)
                                                                 CategoriaRequest request,
                                                                 @RequestPart(required = false)
                                                                 MultipartFile imagen) {
        CategoriaResponse categoriaActualizada = categoriaService.editarCategoria(id, request, imagen);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
