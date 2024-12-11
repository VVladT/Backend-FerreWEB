package pe.edu.utp.backendferreweb.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.utp.backendferreweb.presentation.dto.request.CotizacionRequest;
import pe.edu.utp.backendferreweb.service.CotizacionService;

@Controller
@RequestMapping("/api/cotizaciones")
@RequiredArgsConstructor
public class CotizacionController {
    private final CotizacionService cotizacionService;

    @PostMapping()
    public ResponseEntity<String> solicitarCotizacion(@RequestBody
                                                      @Validated
                                                      CotizacionRequest request) {
        return ResponseEntity.ok(cotizacionService.enviarMensajesDeCotizacion(request));
    }
}
