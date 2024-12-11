package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.presentation.dto.request.DetalleTransferenciaRequest;
import pe.edu.utp.backendferreweb.presentation.dto.request.OrdenCompraRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.FileResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.OrdenCompraResponse;
import pe.edu.utp.backendferreweb.service.OrdenCompraService;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes-compra")
@RequiredArgsConstructor
public class OrdenCompraController {
    private final OrdenCompraService ordenCompraService;

    @GetMapping
    public ResponseEntity<List<OrdenCompraResponse>> obtenerTodas() {
        return ResponseEntity.ok(ordenCompraService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<OrdenCompraResponse> crearOrdenCompra(@RequestBody
                                                               @Validated(ValidCreacion.class)
                                                               OrdenCompraRequest request) {
        return ResponseEntity.ok(ordenCompraService.crearOrdenCompra(request));
    }

    @PatchMapping("/aprobar/{id}")
    public ResponseEntity<OrdenCompraResponse> aprobarOrdenCompra(@PathVariable Integer id) {
        return ResponseEntity.ok(ordenCompraService.aprobarOrdenCompra(id));
    }

    @PatchMapping("/pagar/{id}")
    public ResponseEntity<OrdenCompraResponse> pagarOrdenCompra(@PathVariable Integer id,
                                                                @RequestPart DetalleTransferenciaRequest request,
                                                                @RequestPart MultipartFile file) {
        return ResponseEntity.ok(ordenCompraService.pagarOrdenCompra(id, request, file));
    }

    @PatchMapping("/procesar/{id}")
    public ResponseEntity<OrdenCompraResponse> procesarEntrega(@PathVariable Integer id) {
        return ResponseEntity.ok(ordenCompraService.procesarEntrega(id));
    }

    @PatchMapping("/cancelar/{id}")
    public ResponseEntity<OrdenCompraResponse> cancelarOrden(@PathVariable Integer id,
                                                             @RequestBody String motivo) {
        return ResponseEntity.ok(ordenCompraService.cancelarOrdenCompra(id, motivo));
    }

    @GetMapping("/descargar/{id}")
    public ResponseEntity<byte[]> descargarOrdenCompra(@PathVariable Integer id) throws IOException {
        FileResponse ordenCompra = null;
        byte[] excelBytes = null;

        try {
            ordenCompra = ordenCompraService.descargarOrdenCompra(id);
        } catch (IOException e) {
            throw new IOException("Error al descargar la orden de compra.");
        }
        try {
            excelBytes = Files.readAllBytes(ordenCompra.getFile().toPath());
        } catch (IOException e) {
            throw new IOException("Error al leer los bytes del archivo.");
        }

        ordenCompra.getFile().delete();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" +
                ordenCompra.getName() + ordenCompra.getExtension());


        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }
}
