package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.backendferreweb.presentation.dto.request.TipoEntregaRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.TipoEntregaResponse;
import pe.edu.utp.backendferreweb.service.TipoEntregaService;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-entrega")
@RequiredArgsConstructor
public class TipoEntregaController {

    private final TipoEntregaService tipoEntregaService;

    @GetMapping
    public ResponseEntity<List<TipoEntregaResponse>> obtenerTipos() {
        List<TipoEntregaResponse> tipos = tipoEntregaService.obtenerTodos();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoEntregaResponse> obtenerTipo(@PathVariable Integer id) {
        TipoEntregaResponse tipo = tipoEntregaService.obtenerPorId(id);
        return ResponseEntity.ok(tipo);
    }

    @PostMapping
    public ResponseEntity<TipoEntregaResponse> crearMetodo(@RequestBody
                                                           @Validated(ValidCreacion.class)
                                                           TipoEntregaRequest request) {
        TipoEntregaResponse nuevoTipo = tipoEntregaService.crearTipoEntrega(request);
        return ResponseEntity.ok(nuevoTipo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoEntregaResponse> actualizarMetodo(@PathVariable Integer id,
                                                                @RequestBody
                                                                @Validated(ValidActualizacion.class)
                                                                TipoEntregaRequest request) {
        TipoEntregaResponse tipoActualizado = tipoEntregaService.editarTipoEntrega(id, request);
        return ResponseEntity.ok(tipoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMetodo(@PathVariable Integer id) {
        tipoEntregaService.eliminarTipoEntrega(id);
        return ResponseEntity.noContent().build();
    }
}
