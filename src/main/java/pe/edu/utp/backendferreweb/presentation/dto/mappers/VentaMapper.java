package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.Venta;
import pe.edu.utp.backendferreweb.persistence.model.VentaPresencial;
import pe.edu.utp.backendferreweb.presentation.dto.response.DetalleVentaResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.VentaResponse;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VentaMapper {
    private final UsuarioMapper usuarioMapper;
    private final DetalleVentaMapper detalleVentaMapper;

    public VentaResponse toResponse(Venta venta) {
        if (venta instanceof VentaPresencial vp) {
            return presencialToResponse(vp);
        }

        return null;
    }

    public VentaResponse presencialToResponse(VentaPresencial venta) {
        if (venta == null) return null;

        List<DetalleVentaResponse> detalles = null;
        if (venta.getDetalles() != null) {
            detalles = venta.getDetalles().stream()
                    .map(detalleVentaMapper::toResponse)
                    .toList();
        }

        return VentaResponse.builder()
                .idVenta(venta.getIdVenta())
                .estado(venta.getEstadoVenta().getEstado())
                .dniCliente(venta.getDniCliente())
                .responsable(usuarioMapper.toResponse(venta.getResponsable()))
                .fecha(venta.getFecha().toString())
                .detalles(detalles)
                .subtotal(venta.getSubtotal())
                .igv(venta.getIgv())
                .total(venta.getTotal())
                .build();
    }
}
