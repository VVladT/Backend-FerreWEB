package pe.edu.utp.backendferreweb.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrdenCompraRequest {
    @NotNull(groups = ValidCreacion.class,
            message = "La orden de compra debe estar asociada a un proveedor.")
    private Integer idProveedor;

    @Valid
    @NotNull(groups = ValidCreacion.class,
            message = "La lista de detalles es nula.")
    @Size(groups = {ValidCreacion.class, ValidActualizacion.class},
            min = 1,
            message = "La lista de detalles debe tener al menos un detalle.")
    private List<DetalleCompraRequest> detalles;

    private Double precioEnvio;
    private Double otrosPagos;

    @NotNull(groups = ValidCreacion.class,
            message = "La orden de compra debe tener un almacén de destino.")
    private Integer idAlmacenDestino;

    @NotNull(groups = ValidCreacion.class,
            message = "La orden de compra debe tener un método de pago.")
    private Integer idMetodoPago;

    @NotNull(groups = ValidCreacion.class,
            message = "La orden de compra debe tener un método de entrega asociado.")
    private Integer idTipoEntrega;

    @NotNull(groups = ValidCreacion.class,
            message = "La orden de compra debe tener una fecha de espera estimada.")
    private LocalDateTime fechaEsperada;
}
