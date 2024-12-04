package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.backendferreweb.presentation.dto.request.UnidadRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.UnidadResponse;
import pe.edu.utp.backendferreweb.service.UnidadService;

import java.util.List;

@RestController
@RequestMapping("/api/unidades")
@RequiredArgsConstructor
public class UnidadController {

    private final UnidadService unidadService;

    @GetMapping
    public ResponseEntity<List<UnidadResponse>> obtenerUnidades() {
        List<UnidadResponse> unidades = unidadService.obtenerTodas();
        return ResponseEntity.ok(unidades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadResponse> obtenerUnidad(@PathVariable Integer id) {
        UnidadResponse unidad = unidadService.obtenerPorId(id);
        return ResponseEntity.ok(unidad);
    }

    @PostMapping
    public ResponseEntity<UnidadResponse> crearUnidad(@RequestBody UnidadRequest request) {
        UnidadResponse nuevaUnidad = unidadService.crearUnidad(request);
        return new ResponseEntity<>(nuevaUnidad, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadResponse> actualizarUnidad(@PathVariable Integer id, @RequestBody UnidadRequest request) {
        UnidadResponse unidadActualizada = unidadService.editarUnidad(id, request);
        return ResponseEntity.ok(unidadActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUnidad(@PathVariable Integer id) {
        unidadService.eliminarUnidad(id);
        return ResponseEntity.noContent().build();
    }
}
