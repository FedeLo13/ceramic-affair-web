package es.uca.tfg.ceramic_affair_web.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;

/**
 * Clase de prueba para el servicio GmailEmailService.
 * Proporciona pruebas unitarias para las operaciones relacionadas con el envío de correos electrónicos.
 * 
 * @version 1.0 
 */
@ExtendWith(MockitoExtension.class)
public class GmailEmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private GmailEmailService gmailEmailService;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    public void setUp() {
        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    @DisplayName("Servicio - Enviar correo electrónico")
    void testEnviarCorreoElectronico() throws Exception {
        // Configurar los parámetros del correo electrónico
        String destinatario = "test@example.com";
        String asunto = "Asunto de prueba";
        String cuerpo = "<h1>Hola</h1><p>Este es un correo de prueba.</p>";

        // Llamar al método a probar
        gmailEmailService.sendEmail(destinatario, asunto, cuerpo);

        // Verificar que se haya creado un mensaje MIME
        Mockito.verify(mailSender, Mockito.times(1)).send(Mockito.any(MimeMessage.class));
    }

    @Test
    @DisplayName("Servicio - Enviar correo electrónico (excepción)")
    void testEnviarCorreoElectronicoExcepcion() throws Exception {
        // Simular una excepción al enviar el correo electrónico
        Mockito.doThrow(new RuntimeException("Error al enviar el correo")).when(mailSender).send(Mockito.any(MimeMessage.class));

        // Verificar que se lanza una excepción al intentar enviar el correo electrónico
        Assertions.assertThatThrownBy(() -> {
            gmailEmailService.sendEmail("test@example.com", "Asunto de prueba", "<h1>Hola</h1><p>Este es un correo de prueba.</p>");
        }).isInstanceOf(RuntimeException.class)
          .hasMessageContaining("Error al enviar el correo");
    }
}
