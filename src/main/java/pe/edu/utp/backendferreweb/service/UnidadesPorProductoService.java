package pe.edu.utp.backendferreweb.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Producto;
import pe.edu.utp.backendferreweb.persistence.model.Unidad;
import pe.edu.utp.backendferreweb.persistence.model.UnidadesPorProducto;
import pe.edu.utp.backendferreweb.persistence.model.UnidadesPorProductoPK;
import pe.edu.utp.backendferreweb.persistence.repository.UnidadesPorProductoRepository;
import pe.edu.utp.backendferreweb.presentation.dto.request.UnidadProductoRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class UnidadesPorProductoService {
    private final UnidadesPorProductoRepository unidadesPorProductoRepository;
    private final UnidadService unidadService;

    public UnidadesPorProducto registrarUnidadesPorProducto(Producto producto, UnidadProductoRequest request) {
        String nombreUnidad = request.getNombreUnidad();
        Double precioUnidad = request.getPrecioPorUnidad();
        Double equivalencia = request.getEquivalencia();

        if (nombreUnidad == null) throw new IllegalArgumentException("El nombre de la unidad no puede ser nulo.");
        if (nombreUnidad.isBlank()) throw new IllegalArgumentException("El nombre de la unidad no puede estar vacío.");
        if (precioUnidad == null) throw new IllegalArgumentException("El precio por unidad no puede ser nulo");
        if (precioUnidad <= 0) throw new IllegalArgumentException("El precio por unidad no puede ser menor a 0");
        if (equivalencia == null) throw new IllegalArgumentException("La equivalencia no puede ser nula");
        if (equivalencia <= 0) throw new IllegalArgumentException("La equivalencia no puede ser menor a 0");

        Unidad unidad = unidadService.obtenerPorNombre(nombreUnidad);

        UnidadesPorProductoPK unidadProductoPK = new UnidadesPorProductoPK();
        unidadProductoPK.setIdUnidad(unidad.getIdUnidad());
        unidadProductoPK.setIdProducto(producto.getIdProducto());

        UnidadesPorProducto unidadProducto = UnidadesPorProducto.builder()
                .primaryKey(unidadProductoPK)
                .producto(producto)
                .unidad(unidad)
                .precio(precioUnidad)
                .equivalencia(equivalencia)
                .build();

        return unidadesPorProductoRepository.save(unidadProducto);
    }
}
