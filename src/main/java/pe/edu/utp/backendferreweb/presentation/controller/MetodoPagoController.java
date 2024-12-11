package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.backendferreweb.presentation.dto.request.MetodoPagoRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.MetodoPagoResponse;
import pe.edu.utp.backendferreweb.service.MetodoPagoService;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

import java.util.List;

@RestController
@RequestMapping("/api/metodos-pago")
@RequiredArgsConstructor
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;

    @GetMapping
    public ResponseEntity<List<MetodoPagoResponse>> obtenerMetodos() {
        List<MetodoPagoResponse> metodos = metodoPagoService.obtenerTodos();
        return ResponseEntity.ok(metodos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoResponse> obtenerMetodo(@PathVariable Integer id) {
        MetodoPagoResponse metodo = metodoPagoService.obtenerPorId(id);
        return ResponseEntity.ok(metodo);
    }

    @PostMapping
    public ResponseEntity<MetodoPagoResponse> crearMetodo(@RequestBody
                                                          @Validated(ValidCreacion.class)
                                                          MetodoPagoRequest metodoPago) {
        MetodoPagoResponse nuevoMetodo = metodoPagoService.crearMetodoPago(metodoPago);
        return ResponseEntity.ok(nuevoMetodo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoResponse> actualizarMetodo(@PathVariable Integer id,
                                                               @RequestBody
                                                               @Validated(ValidActualizacion.class)
                                                               MetodoPagoRequest metodoPago) {
        MetodoPagoResponse metodoActualizado = metodoPagoService.editarMetodoPago(id, metodoPago);
        return ResponseEntity.ok(metodoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMetodo(@PathVariable Integer id) {
        metodoPagoService.eliminarMetodoPago(id);
        return ResponseEntity.noContent().build();
    }
}
