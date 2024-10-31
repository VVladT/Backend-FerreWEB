package pe.edu.utp.backendferreweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Producto;
import pe.edu.utp.backendferreweb.persistence.model.Unidad;
import pe.edu.utp.backendferreweb.persistence.model.UnidadesPorProducto;
import pe.edu.utp.backendferreweb.persistence.model.UnidadesPorProductoPK;
import pe.edu.utp.backendferreweb.persistence.repository.UnidadesPorProductoRepository;
import pe.edu.utp.backendferreweb.presentation.dto.request.UnidadProductoRequest;

@Service
@RequiredArgsConstructor
public class UnidadesPorProductoService {
    private final UnidadesPorProductoRepository unidadesPorProductoRepository;
    private final UnidadService unidadService;

    public UnidadesPorProducto registrarUnidadesPorProducto(Producto producto, UnidadProductoRequest request) {
        String nombreUnidad = request.getNombreUnidad();
        Double precioUnidad = request.getPrecioPorUnidad();

        Unidad unidad = unidadService.obtenerPorNombre(nombreUnidad);

        UnidadesPorProductoPK unidadProductoPK = new UnidadesPorProductoPK();
        unidadProductoPK.setIdUnidad(unidad.getIdUnidad());
        unidadProductoPK.setIdProducto(producto.getIdProducto());

        UnidadesPorProducto unidadProducto = unidadesPorProductoRepository.findById(unidadProductoPK)
                .orElseGet(() -> unidadesPorProductoRepository.save(
                        UnidadesPorProducto.builder()
                                .primaryKey(unidadProductoPK)
                                .producto(producto)
                                .unidad(unidad)
                                .build()
                ));

        unidadProducto.setPrecio(precioUnidad);

        return unidadesPorProductoRepository.save(unidadProducto);
    }
}
