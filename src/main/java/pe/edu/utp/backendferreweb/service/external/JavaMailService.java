package pe.edu.utp.backendferreweb.service.external;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JavaMailService {
    private final JavaMailSender mailSender;

    public void sendMailWithReply(String destinatario, String asunto, String contenido, String replyTo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setReplyTo(replyTo);
        message.setSubject(asunto);
        message.setText(contenido);
        mailSender.send(message);
    }

    public void sendSimpleEmail(String destinatario, String asunto, String contenido) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject(asunto);
        message.setText(contenido);
        mailSender.send(message);
    }

    public void sendEmail(String destinatario,
                          String asunto,
                          String contenido,
                          byte[] fileBytes,
                          String fileName,
                          String mimeType,
                          String replyTo,
                          String cc) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(contenido);
        helper.setReplyTo(replyTo);
        helper.setCc(cc);

        ByteArrayDataSource dataSource = new ByteArrayDataSource(fileBytes, mimeType);

        helper.addAttachment(fileName, dataSource);

        mailSender.send(message);
    }

    public void sendEmailWithFileAndReply(String destinatario, String asunto, String contenido,
                                          byte[] fileBytes,
                                          String fileName, String mimeType,
                                          String replyTo)
            throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(contenido);
        helper.setReplyTo(replyTo);

        ByteArrayDataSource dataSource = new ByteArrayDataSource(fileBytes, mimeType);

        helper.addAttachment(fileName, dataSource);

        mailSender.send(message);
    }

    public void sendEmailWithFile(String destinatario, String asunto, String contenido,
                                  byte[] fileBytes,
                                  String fileName, String mimeType)
            throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(contenido);

        ByteArrayDataSource dataSource = new ByteArrayDataSource(fileBytes, mimeType);

        helper.addAttachment(fileName, dataSource);

        mailSender.send(message);
    }
}
