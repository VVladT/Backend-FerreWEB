package pe.edu.utp.backendferreweb.service.external;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendSimpleEmail(String destinatario, String asunto, String contenido) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject(asunto);
        message.setText(contenido);
        mailSender.send(message);
    }

    public void sendEmailWithFile(String to, String subject, String text,
                                  ByteArrayOutputStream fileBaos,
                                  String fileName, String mimeType)
            throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        ByteArrayDataSource dataSource = new ByteArrayDataSource(fileBaos.toByteArray(), mimeType);

        helper.addAttachment(fileName, dataSource);

        mailSender.send(message);
    }
}
