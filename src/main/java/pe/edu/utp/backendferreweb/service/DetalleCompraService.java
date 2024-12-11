package pe.edu.utp.backendferreweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.DetalleCompra;
import pe.edu.utp.backendferreweb.persistence.repository.DetalleCompraRepository;
import pe.edu.utp.backendferreweb.presentation.dto.mappers.DetalleCompraMapper;
import pe.edu.utp.backendferreweb.presentation.dto.request.DetalleCompraRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.DetalleCompraResponse;

@Service
@RequiredArgsConstructor
public class DetalleCompraService {

    private final DetalleCompraMapper detalleCompraMapper;
    private final DetalleCompraRepository detalleCompraRepository;

    public DetalleCompraResponse crearDetalleCompra(DetalleCompraRequest request) {
        Integer idProducto = request.getIdProducto();
        Integer idUnidad = request.getIdUnidad();
        Double cantidad = request.getCantidad();
        Double precio = request.getPrecio();

        if (idProducto == null) throw new IllegalArgumentException("El id del producto no puede ser nulo");
        if (idUnidad == null) throw new IllegalArgumentException("El id de la unidad no puede ser nulo");
        if (cantidad == null) throw new IllegalArgumentException("La cantidad no puede ser nula");
        if (cantidad <= 10) throw new IllegalArgumentException("La cantidad no puede ser menor a 10");
        if (precio == null) throw new IllegalArgumentException("El precio no puede ser nulo.");
        if (precio < 0) throw new IllegalArgumentException("El precio no puede ser negativo");

        DetalleCompra detalleCompra = detalleCompraMapper.toEntity(request);
        DetalleCompra nuevoDetalle = detalleCompraRepository.save(detalleCompra);
        return detalleCompraMapper.toResponse(nuevoDetalle);
    }

    public DetalleCompra registrarDetalle(DetalleCompraRequest request) {
        Integer idProducto = request.getIdProducto();
        Integer idUnidad = request.getIdUnidad();
        Double cantidad = request.getCantidad();
        Double precio = request.getPrecio();

        if (idProducto == null) throw new IllegalArgumentException("El id del producto no puede ser nulo");
        if (idUnidad == null) throw new IllegalArgumentException("El id de la unidad no puede ser nulo");
        if (cantidad == null) throw new IllegalArgumentException("La cantidad no puede ser nula");
        if (cantidad < 10) throw new IllegalArgumentException("La cantidad no puede ser menor a 10");
        if (precio == null) throw new IllegalArgumentException("El precio no puede ser nulo.");
        if (precio < 0) throw new IllegalArgumentException("El precio no puede ser negativo");

        DetalleCompra detalleCompra = detalleCompraMapper.toEntity(request);
        return detalleCompraRepository.save(detalleCompra);
    }
}
