package pe.edu.utp.backendferreweb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.backendferreweb.model.Categoria;
import pe.edu.utp.backendferreweb.service.CategoriaService;

import java.util.List;

@RestController
public class CategoriaController {
    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping("/categorias")
    public ResponseEntity<List<Categoria>> getCategorias() {
        List<Categoria> entities = service.listar();
        return ResponseEntity.ok(entities);
    }
}
