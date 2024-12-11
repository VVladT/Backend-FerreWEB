package pe.edu.utp.backendferreweb.presentation.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.backendferreweb.presentation.dto.request.EstadoCompraRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.EstadoCompraResponse;
import pe.edu.utp.backendferreweb.service.EstadoCompraService;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

import java.util.List;

@RestController
@RequestMapping("/api/estados-compra")
@RequiredArgsConstructor
public class EstadoCompraController {
    private final EstadoCompraService estadoCompraService;

    @GetMapping
    public ResponseEntity<List<EstadoCompraResponse>> obtenerEstados() {
        return ResponseEntity.ok(estadoCompraService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoCompraResponse> obtenerEstado(@PathVariable Integer id) {
        return ResponseEntity.ok(estadoCompraService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EstadoCompraResponse> crearEstado(@RequestBody
                                                            @Validated(ValidCreacion.class)
                                                            EstadoCompraRequest request) {
        return ResponseEntity.ok(estadoCompraService.crearEstado(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoCompraResponse> actualizarEstado(@PathVariable Integer id,
                                                                 @RequestBody
                                                                 @Validated(ValidActualizacion.class)
                                                                 EstadoCompraRequest request) {
        return ResponseEntity.ok(estadoCompraService.editarEstado(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstado(@PathVariable Integer id) {
        estadoCompraService.eliminarEstado(id);
        return ResponseEntity.noContent().build();
    }
}
