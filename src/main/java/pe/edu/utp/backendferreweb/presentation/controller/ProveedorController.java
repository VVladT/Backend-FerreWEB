package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.backendferreweb.presentation.dto.request.ProveedorRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.ProveedorResponse;
import pe.edu.utp.backendferreweb.service.ProveedorService;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
public class ProveedorController {
    private final ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<ProveedorResponse>> obtenerTodos() {
        return ResponseEntity.ok(proveedorService.obtenerTodos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<ProveedorResponse> buscarPorRuc(@RequestParam String ruc) {
        return ResponseEntity.ok(proveedorService.buscarPorRuc(ruc));
    }

    @PostMapping
    public ResponseEntity<ProveedorResponse> crearProveedor(@RequestBody ProveedorRequest request) {
        return ResponseEntity.ok(proveedorService.crearProveedor(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponse> actualizarProveedor(
            @PathVariable Integer id,
            @RequestBody ProveedorRequest request) {
        return ResponseEntity.ok(proveedorService.editarProveedor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable Integer id) {
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.noContent().build();
    }
}
