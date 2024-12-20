package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.backendferreweb.presentation.dto.request.AlmacenRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.AlmacenResponse;
import pe.edu.utp.backendferreweb.service.AlmacenService;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

import java.util.List;

@RestController
@RequestMapping("/api/almacenes")
@RequiredArgsConstructor
public class AlmacenController {

    private final AlmacenService almacenService;

    @GetMapping
    public ResponseEntity<List<AlmacenResponse>> obtenerAlmacenes() {
        List<AlmacenResponse> almacenes = almacenService.obtenerTodos();
        return ResponseEntity.ok(almacenes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlmacenResponse> obtenerAlmacen(@PathVariable Integer id) {
        AlmacenResponse almacen = almacenService.obtenerPorId(id);
        return ResponseEntity.ok(almacen);
    }

    @PostMapping
    public ResponseEntity<AlmacenResponse> crearAlmacen(@RequestBody
                                                        @Validated(ValidCreacion.class)
                                                        AlmacenRequest request) {
        AlmacenResponse nuevoAlmacen = almacenService.crearAlmacen(request);
        return new ResponseEntity<>(nuevoAlmacen, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlmacenResponse> actualizarAlmacen(@PathVariable Integer id,
                                                             @RequestBody
                                                             @Validated(ValidActualizacion.class)
                                                             AlmacenRequest request) {
        AlmacenResponse almacenActualizado = almacenService.editarAlmacen(id, request);
        return ResponseEntity.ok(almacenActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlmacen(@PathVariable Integer id) {
        almacenService.eliminarAlmacen(id);
        return ResponseEntity.noContent().build();
    }
}
