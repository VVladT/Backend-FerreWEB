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

    @GetMapping("/{id}")
    public ResponseEntity<List<Rol>> obtenerRolesDeUsuario(@PathVariable Integer id) {
        List<Rol> roles = rolService.obtenerRolesPorIdUsuario(id);
        return ResponseEntity.ok(roles);
    }

    @PostMapping()
    public ResponseEntity<Rol> crearRol(@RequestPart RolRequest request,
                                                  @RequestPart MultipartFile imagen) {
        Rol nuevoRol = rolService.crearRol(request, imagen);
        return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Integer id) {
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }
}
