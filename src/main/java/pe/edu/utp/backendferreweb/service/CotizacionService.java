package pe.edu.utp.backendferreweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.edu.utp.backendferreweb.persistence.model.Producto;
import pe.edu.utp.backendferreweb.persistence.model.Proveedor;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.persistence.repository.ProductoRepository;
import pe.edu.utp.backendferreweb.presentation.dto.request.CotizacionRequest;
import pe.edu.utp.backendferreweb.service.external.JavaMailService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CotizacionService {
    private final ProveedorService proveedorService;
    private final JavaMailService javaMailService;
    private final ProductoRepository productoRepository;

    public String enviarMensajesDeCotizacion(CotizacionRequest request) {
        if (request.getFechaLimite().isBefore(LocalDate.now().plusDays(2)))
            throw new IllegalArgumentException("La fecha límite debe ser por lo menos 3 días después de hoy");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        String nombreCompleto = String.format("%s %s %s",
                usuario.getNombre(),
                usuario.getApellidoPaterno(),
                usuario.getApellidoMaterno());

        StringBuilder mensaje = new StringBuilder();

        mensaje.append("\n\nPor medio de la presente, nos dirigimos a ustedes para solicitar una cotización para los siguientes productos:\n\n");

        List<String> productos = productoRepository.findAllById(request.getIdProductos())
                .stream().map(Producto::getNombre).toList();

        for (int i = 0; i < productos.size(); i++) {
            String producto = productos.get(i);
            mensaje.append(i+1).append(". ").append(producto).append("\n\n");
        }

        mensaje.append("Agradeceríamos una cotización detallada que incluya los precios por unidad, descuentos y cualquier cargo adicional, como las tarifas de envío.\n\n");
        mensaje.append("\n\nSi surge alguna pregunta o se necesita más información puede contactar conmigo por medio de ")
                .append(usuario.getUsername()).append(".\n\n");
        mensaje.append("Esperamos recibir su cotización antes de ")
                .append(request.getFechaLimite()).append(".\n\n");
        mensaje.append("Gracias por su atención. Atentamente,\n");
        mensaje.append(nombreCompleto).append(",\n");
        mensaje.append("Representante de la Ferretería Linares - Olano");

        String asunto = "Solicitud de Cotización para Productos ferreteros";
        String reply = usuario.getUsername();

        for (Integer idProveedor : request.getIdProveedores()) {
            Proveedor proveedor = proveedorService.obtenerEntidadPorId(idProveedor);

            int i = mensaje.indexOf("\n");
            mensaje.replace(0, i, "Estimados señores de " + proveedor.getNombre() + ",");

            try {
                javaMailService.sendMailWithReply(proveedor.getEmail(), asunto, mensaje.toString(), reply);
            } catch (Exception e) {
                throw new MailSendException("Ha ocurrido un error al enviar los mensajes");
            }
        }

        return "Mensajes enviados correctamente";
    }
}
