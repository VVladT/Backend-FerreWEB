package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.backendferreweb.persistence.model.Almacen;
import pe.edu.utp.backendferreweb.presentation.dto.request.AlmacenRequest;
import pe.edu.utp.backendferreweb.service.AlmacenService;

import java.util.List;

@RestController
@RequestMapping("/api/almacenes")
@RequiredArgsConstructor
public class AlmacenController {

    private final AlmacenService almacenService;

    @GetMapping
    public ResponseEntity<List<Almacen>> obtenerAlmacenes() {
        List<Almacen> almacenes = almacenService.obtenerTodos();
        return ResponseEntity.ok(almacenes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Almacen> obtenerAlmacen(@PathVariable Integer id) {
        Almacen almacen = almacenService.obtenerPorId(id);
        return ResponseEntity.ok(almacen);
    }

    @PostMapping
    public ResponseEntity<Almacen> crearAlmacen(@RequestBody AlmacenRequest request) {
        Almacen nuevoAlmacen = almacenService.crearAlmacen(request);
        return new ResponseEntity<>(nuevoAlmacen, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Almacen> actualizarAlmacen(@PathVariable Integer id, @RequestBody AlmacenRequest request) {
        Almacen almacenActualizado = almacenService.editarAlmacen(id, request);
        return ResponseEntity.ok(almacenActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlmacen(@PathVariable Integer id) {
        almacenService.eliminarAlmacen(id);
        return ResponseEntity.noContent().build();
    }
}
