package pe.edu.utp.backendferreweb.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.backendferreweb.persistence.model.Producto;
import pe.edu.utp.backendferreweb.persistence.model.Proveedor;
import pe.edu.utp.backendferreweb.persistence.model.Usuario;
import pe.edu.utp.backendferreweb.persistence.repository.ProductoRepository;
import pe.edu.utp.backendferreweb.presentation.dto.request.CotizacionRequest;
import pe.edu.utp.backendferreweb.presentation.dto.request.DetalleTransferenciaRequest;
import pe.edu.utp.backendferreweb.presentation.dto.response.OrdenCompraResponse;
import pe.edu.utp.backendferreweb.presentation.dto.response.UsuarioResponse;
import pe.edu.utp.backendferreweb.service.external.JavaMailService;

import java.io.File;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailMessageService {
    private final JavaMailService javaMailService;

    @SneakyThrows
    public String enviarOrdenCompra(File ordenXlsx, OrdenCompraResponse ordenCompra) {
        UsuarioResponse usuario = ordenCompra.getUsuarioSolicitante();
        String nombreCompleto = usuario.getNombreCompleto();

        String fileName = String.format("orden_compra_%08d.xlsx", ordenCompra.getIdOrdenCompra());
        String mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        String destinatario = ordenCompra.getProveedor().getEmail();
        String asunto = String.format("Envío de Orden de Compra %08d", ordenCompra.getIdOrdenCompra());
        String contenido = String.format("""
                Estimados señores de %s,
                
                Le envío adjunta la orden de compra %08d realizada con respecto a los productos solicitados.
                Por favor, confirme la recepción del archivo adjunto y la aceptación de la orden a la mayor brevedad posible.
                
                Si hay algún detalle que necesite ser aclarado o si se requiere modificar alguna parte de la orden, no dude en ponerse en contacto con nosotros.
                
                Agradecemos su pronta atención.
                
                Saludos cordiales,
                
                %s
                Gerente General
                Ferretería Linares - Olano
                %s""",
                ordenCompra.getProveedor().getNombre(),
                ordenCompra.getIdOrdenCompra(),
                nombreCompleto,
                usuario.getUsername());

        String reply = usuario.getUsername();
        String cc = ordenCompra.getUsuarioAutorizacion().getUsername();

        javaMailService.sendEmail(destinatario, asunto, contenido,
                Files.readAllBytes(ordenXlsx.toPath()), fileName, mimeType, reply, cc);

        return "Mensaje enviado correctamente";
    }

    @SneakyThrows
    public String enviarDetallesTransferencia(OrdenCompraResponse ordenCompra,
                                              DetalleTransferenciaRequest request,
                                              MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String nombreCompleto = String.format("%s %s %s",
                usuario.getNombre(),
                usuario.getApellidoPaterno(),
                usuario.getApellidoMaterno());

        String mimeType = file.getContentType();
        String fileName = file.getOriginalFilename();
        byte[] fileBytes = file.getBytes();

        String asunto = String.format("Confirmación de Pago de la Orden de Compra %08d",
                ordenCompra.getIdOrdenCompra());
        String destinatario = ordenCompra.getProveedor().getEmail();
        String contenido = String.format("""
                Estimados señores de %s,
                
                Espero que se encuentren bien,
                Por medio de este mensaje le confirmamos que el pago correspondiente a nuestra orden %08d ha sido realizado con éxito.
                
                A continuación se le proporcionan los detalles de la transferencia:
                
                Entidad Bancaria: %s
                N° Operación: %s
                Fecha Operación: %s
                Monto: %.2f SOLES
                
                Adjunto a este correo les envío el comprobante de transferencia para su validación.
                
                Quedo atento a su confirmación y, si necesitan más información, no duden en contactarme.
                
                Saludos cordiales,
                %s
                Gerente General
                Ferretería Linares - Olano
                %s""",
                ordenCompra.getProveedor().getNombre(),
                ordenCompra.getIdOrdenCompra(),
                request.getEntidadBancaria(),
                request.getNumeroOperacion(),
                request.getFechaTransferencia().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                request.getMontoTransferido(),
                nombreCompleto,
                usuario.getUsername());

        String replyTo = usuario.getUsername();

        javaMailService.sendEmailWithFileAndReply(destinatario, asunto, contenido,
                fileBytes, fileName, mimeType, replyTo);

        return "Mensaje enviado correctamente.";
    }
}
