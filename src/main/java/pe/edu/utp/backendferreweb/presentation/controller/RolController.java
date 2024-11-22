package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.persistence.model.Rol;
import pe.edu.utp.backendferreweb.presentation.dto.request.RolRequest;
import pe.edu.utp.backendferreweb.service.RolService;

import java.util.List;

@RestController
@RequestMapping("api/roles")
@RequiredArgsConstructor
public class RolController {
    private final RolService rolService;

    @GetMapping
    public ResponseEntity<List<Rol>> obtenerRoles() {
        List<Rol> roles = rolService.obtenerTodos();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Rol> obtenerRol(@PathVariable Integer id) {
        Rol rol = rolService.obtenerPorId(id);
        return ResponseEntity.ok(rol);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Rol> obtenerRol(@PathVariable String tipo) {
        Rol rol = rolService.obtenerPorTipo(tipo);
        return ResponseEntity.ok(rol);
    }

    @PostMapping()
    public ResponseEntity<Rol> crearRol(@RequestPart RolRequest request,
                                        @RequestPart(required = false) MultipartFile imagen) {
        Rol nuevoRol = rolService.crearRol(request, imagen);
        return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizarRol(@PathVariable Integer id,
                                             @RequestPart RolRequest request,
                                             @RequestPart(required = false) MultipartFile imagen) {
        Rol nuevoRol = rolService.editarRol(id, request, imagen);
        return ResponseEntity.ok(nuevoRol);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Integer id) {
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }
}
