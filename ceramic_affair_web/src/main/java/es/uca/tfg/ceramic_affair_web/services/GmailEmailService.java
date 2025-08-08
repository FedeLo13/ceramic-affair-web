package es.uca.tfg.ceramic_affair_web.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

/**
 * Implementación del servicio de envío de correos electrónicos utilizando Gmail.
 * 
 * @version 1.0
 */
@Service
@Primary
public class GmailEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(GmailEmailService.class.getName());

    private final JavaMailSender mailSender;

    public GmailEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("fedevlopez17@gmail.com", "Ceramic Affair");
            helper.setText(body, true);

            mailSender.send(message);
        } catch (Exception e) {
            logger.error("Error al enviar el correo electrónico a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error al enviar el correo electrónico", e);
        }
    }
}
