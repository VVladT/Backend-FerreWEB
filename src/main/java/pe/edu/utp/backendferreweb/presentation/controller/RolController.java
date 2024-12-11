package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.presentation.dto.request.RolRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.RolResponse;
import pe.edu.utp.backendferreweb.service.RolService;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

import java.util.List;

@RestController
@RequestMapping("api/roles")
@RequiredArgsConstructor
public class RolController {
    private final RolService rolService;

    @GetMapping
    public ResponseEntity<List<RolResponse>> obtenerRoles() {
        List<RolResponse> roles = rolService.obtenerTodos();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<RolResponse> obtenerRol(@PathVariable Integer id) {
        RolResponse rol = rolService.obtenerPorId(id);
        return ResponseEntity.ok(rol);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<RolResponse> obtenerRol(@PathVariable String tipo) {
        RolResponse rol = rolService.obtenerPorTipo(tipo);
        return ResponseEntity.ok(rol);
    }

    @PostMapping()
    public ResponseEntity<RolResponse> crearRol(@RequestPart
                                                @Validated(ValidCreacion.class)
                                                RolRequest request,
                                                @RequestPart(required = false)
                                                MultipartFile imagen) {
        RolResponse nuevoRol = rolService.crearRol(request, imagen);
        return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolResponse> actualizarRol(@PathVariable Integer id,
                                                     @RequestPart
                                                     @Validated(ValidActualizacion.class)
                                                     RolRequest request,
                                                     @RequestPart(required = false)
                                                     MultipartFile imagen) {
        RolResponse nuevoRol = rolService.editarRol(id, request, imagen);
        return ResponseEntity.ok(nuevoRol);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Integer id) {
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }
}
