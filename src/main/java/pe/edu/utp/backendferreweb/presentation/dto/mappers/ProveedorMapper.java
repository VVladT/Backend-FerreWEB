package pe.edu.utp.backendferreweb.presentation.dto.mappers;

import org.springframework.stereotype.Component;
import pe.edu.utp.backendferreweb.persistence.model.Proveedor;
import pe.edu.utp.backendferreweb.presentation.dto.request.ProveedorRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.ProveedorResponse;

@Component
public class ProveedorMapper {
    public Proveedor toEntity(ProveedorRequest request) {
        if (request == null) return null;

        return Proveedor.builder()
                .ruc(request.getRuc())
                .nombre(request.getNombre())
                .nombreComercial(request.getNombreComercial())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
                .build();
    }

    public ProveedorResponse toResponse(Proveedor proveedor) {
        if (proveedor == null) return null;

        return ProveedorResponse.builder()
                .idProveedor(proveedor.getIdProveedor())
                .ruc(proveedor.getRuc())
                .nombre(proveedor.getNombre())
                .nombreComercial(proveedor.getNombreComercial())
                .email(proveedor.getEmail())
                .telefono(proveedor.getTelefono())
                .direccion(proveedor.getDireccion())
                .build();
    }
}
