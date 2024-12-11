package pe.edu.utp.backendferreweb.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.EstadoVenta;
import pe.edu.utp.backendferreweb.persistence.repository.EstadoVentaRespository;

@Service
@Transactional
@RequiredArgsConstructor
public class EstadoVentaService {
    private final EstadoVentaRespository estadoVentaRespository;

    public EstadoVenta obtenerPorNombre(String nombre) {
        return estadoVentaRespository.findByEstado(nombre)
                .orElseGet(() -> estadoVentaRespository.save(EstadoVenta.builder()
                                .estado(nombre)
                                .build()));
    }
}
