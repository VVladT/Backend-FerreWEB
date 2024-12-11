package pe.edu.utp.backendferreweb.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import pe.edu.utp.backendferreweb.util.validation.annotation.NotBlankIfNotNull;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidActualizacion;
import pe.edu.utp.backendferreweb.util.validation.groups.ValidCreacion;

import java.util.List;

@Data
@Builder
public class ProductoRequest {

    @NotBlank(groups = ValidCreacion.class,
            message = "El producto debe estar asociado a una categoría.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "El producto debe estar asociado a una categoría.")
    private String categoria;

    @NotBlank(groups = ValidCreacion.class,
            message = "El producto debe estar asociado a una unidad por defecto.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "El producto debe estar asociado a una unidad por defecto.")
    private String unidadPorDefecto;

    @Valid
    @NotNull(groups = ValidCreacion.class,
            message = "La lista de unidades permitidas es nula.")
    @Size(groups = {ValidCreacion.class, ValidActualizacion.class},
            min = 1,
            message = "La lista de unidades permitidas debe tener al menos una unidad.")
    private List<UnidadProductoRequest> unidadesPermitidas;

    @Valid
    @NotNull(groups = ValidCreacion.class,
            message = "La lista de almacenes es nula.")
    @Size(groups = {ValidCreacion.class, ValidActualizacion.class},
            min = 1,
            message = "La lista de almacenes debe tener al menos un almacén.")
    private List<AlmacenCantidadRequest> almacenes;

    @NotBlank(groups = ValidCreacion.class,
            message = "El nombre del producto no puede estar vacío.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "El nombre del producto no puede estar vacío")
    private String nombre;

    @NotBlank(groups = ValidCreacion.class,
            message = "La descripción del producto no puede estar vacía.")
    @NotBlankIfNotNull(groups = ValidActualizacion.class,
            message = "La descripción del producto no puede estar vacía")
    private String descripcion;
}
