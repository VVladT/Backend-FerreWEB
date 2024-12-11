package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.backendferreweb.presentation.dto.request.VentaPresencialRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.VentaResponse;
import pe.edu.utp.backendferreweb.service.VentaService;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {
    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<VentaResponse>> obtenerTodas() {
        return ResponseEntity.ok(ventaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtenerVenta(@PathVariable Integer id) {
        return ResponseEntity.ok(ventaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<VentaResponse> crearOrdenCompra(@RequestBody
                                                          @Validated(ValidCreacion.class)
                                                          VentaPresencialRequest request) {
        return ResponseEntity.ok(ventaService.registrarVentaPresencial(request));
    }

}
