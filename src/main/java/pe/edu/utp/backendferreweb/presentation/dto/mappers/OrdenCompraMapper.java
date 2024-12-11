package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.*;
import pe.edu.utp.backendferreweb.presentation.dto.request.OrdenCompraRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.*;
import pe.edu.utp.backendferreweb.service.AlmacenService;
import pe.edu.utp.backendferreweb.service.MetodoPagoService;
import pe.edu.utp.backendferreweb.service.ProveedorService;
import pe.edu.utp.backendferreweb.service.TipoEntregaService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrdenCompraMapper {

    private final ProveedorService proveedorService;
    private final DetalleCompraMapper detalleCompraMapper;
    private final AlmacenService almacenService;
    private final MetodoPagoService metodoPagoService;
    private final TipoEntregaService tipoEntregaService;
    private final ProveedorMapper proveedorMapper;
    private final AlmacenMapper almacenMapper;
    private final EstadoCompraMapper estadoCompraMapper;
    private final MetodoPagoMapper metodoPagoMapper;
    private final TipoEntregaMapper tipoEntregaMapper;
    private final UsuarioMapper usuarioMapper;

    public OrdenCompra toEntity(OrdenCompraRequest request) {
        if (request == null) return null;

        Proveedor proveedor =
                proveedorService.obtenerEntidadPorId(request.getIdProveedor());

        Almacen almacen =
                almacenService.obtenerEntidadPorId(request.getIdAlmacenDestino());

        MetodoPago metodoPago =
                metodoPagoService.obtenerEntidadPorId(request.getIdMetodoPago());

        TipoEntrega tipoEntrega =
                tipoEntregaService.obtenerEntidadPorId(request.getIdTipoEntrega());

        return OrdenCompra.builder()
                .proveedor(proveedor)
                .precioEnvio(request.getPrecioEnvio())
                .otrosPagos(request.getOtrosPagos())
                .destino(almacen)
                .metodoPago(metodoPago)
                .tipoEntrega(tipoEntrega)
                .fechaEntregaEsperada(request.getFechaEsperada())
                .build();
    }

    public OrdenCompraResponse toResponse(OrdenCompra ordenCompra) {
        if (ordenCompra == null) return null;

        List<DetalleCompraResponse> detalles = null;
        if (ordenCompra.getDetalles() != null) {
            detalles = ordenCompra.getDetalles().stream()
                    .map(detalleCompraMapper::toResponse)
                    .toList();
        }

        ProveedorResponse proveedor =
                proveedorMapper.toResponse(ordenCompra.getProveedor());

        AlmacenResponse almacen =
                almacenMapper.toResponse(ordenCompra.getDestino());

        EstadoCompraResponse estadoCompra =
                estadoCompraMapper.toResponse(ordenCompra.getEstadoCompra());

        MetodoPagoResponse metodoPago =
                metodoPagoMapper.toResponse(ordenCompra.getMetodoPago());

        TipoEntregaResponse tipoEntrega =
                tipoEntregaMapper.toResponse(ordenCompra.getTipoEntrega());

        return OrdenCompraResponse.builder()
                .idOrdenCompra(ordenCompra.getIdOrdenCompra())
                .detalles(detalles)
                .proveedor(proveedor)
                .fechaEmision(ordenCompra.getFechaEmision().toString())
                .fechaEsperada(ordenCompra.getFechaEntregaEsperada().toString())
                .subtotal(ordenCompra.getSubtotal())
                .otrosPagos(ordenCompra.getOtrosPagos())
                .precioEnvio(ordenCompra.getPrecioEnvio())
                .igv(ordenCompra.getIgv())
                .total(ordenCompra.getTotal())
                .destino(almacen)
                .estadoCompra(estadoCompra)
                .metodoPago(metodoPago)
                .tipoEntrega(tipoEntrega)
                .usuarioSolicitante(usuarioMapper.toResponse(ordenCompra.getSolicitante()))
                .usuarioAutorizacion(usuarioMapper.toResponse(ordenCompra.getUsuarioAutorizacion()))
                .build();
    }
}
