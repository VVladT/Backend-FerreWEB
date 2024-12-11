package pe.edu.utp.backendferreweb.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrdenCompraResponse {
    private Integer idOrdenCompra;
    private List<DetalleCompraResponse> detalles;
    private ProveedorResponse proveedor;
    private String fechaEmision;
    private String fechaEsperada;
    private Double subtotal;
    private Double precioEnvio;
    private Double otrosPagos;
    private Double igv;
    private Double total;
    private AlmacenResponse destino;
    private EstadoCompraResponse estadoCompra;
    private MetodoPagoResponse metodoPago;
    private TipoEntregaResponse tipoEntrega;
    private UsuarioResponse usuarioSolicitante;
    private UsuarioResponse usuarioAutorizacion;
}
