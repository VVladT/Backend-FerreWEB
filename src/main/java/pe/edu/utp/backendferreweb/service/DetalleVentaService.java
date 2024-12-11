package pe.edu.utp.backendferreweb.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.DetalleVenta;
import pe.edu.utp.backendferreweb.persistence.repository.DetalleVentaRepository;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.DetalleVentaMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.DetalleVentaRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class DetalleVentaService {

    private final DetalleVentaMapper detalleVentaMapper;
    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVenta registrarDetalle(DetalleVentaRequest request) {
        Integer idProducto = request.getIdProducto();
        Integer idUnidad = request.getIdUnidad();
        Double cantidad = request.getCantidad();

        if (idProducto == null) throw new IllegalArgumentException("El id del producto no puede ser nulo");
        if (idUnidad == null) throw new IllegalArgumentException("El id de la unidad no puede ser nulo");
        if (cantidad == null) throw new IllegalArgumentException("La cantidad no puede ser nula");
        if (cantidad < 1) throw new IllegalArgumentException("La cantidad no puede ser menor a 1");

        DetalleVenta detalleCompra = detalleVentaMapper.toEntity(request);
        return detalleVentaRepository.save(detalleCompra);
    }
}
