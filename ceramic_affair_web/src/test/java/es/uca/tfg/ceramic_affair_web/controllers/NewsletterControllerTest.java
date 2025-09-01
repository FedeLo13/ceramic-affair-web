package es.uca.tfg.ceramic_affair_web.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.uca.tfg.ceramic_affair_web.DTOs.NewsletterDTO;
import es.uca.tfg.ceramic_affair_web.controllers.admin.NewsletterController;
import es.uca.tfg.ceramic_affair_web.entities.Newsletter;
import es.uca.tfg.ceramic_affair_web.entities.Suscriptor;
import es.uca.tfg.ceramic_affair_web.exceptions.EmailException;
import es.uca.tfg.ceramic_affair_web.repositories.NewsletterRepo;
import es.uca.tfg.ceramic_affair_web.repositories.SuscriptorRepo;
import es.uca.tfg.ceramic_affair_web.security.JwtUtils;
import es.uca.tfg.ceramic_affair_web.services.EmailService;

/**
 * Test para el controlador NewsletterController.
 * Verifica el correcto funcionamiento de los endpoints relacionados con el envío de newsletters.
 * 
 * @version 1.0
 */
@WebMvcTest(controllers = NewsletterController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad para pruebas
public class NewsletterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NewsletterRepo newsletterRepo;

    @MockitoBean
    private SuscriptorRepo suscriptorRepo;

    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Controlador - Enviar Newsletter")
    public void testEnviarNewsletter() throws Exception {
        NewsletterDTO newsletterDTO = new NewsletterDTO("Asunto de prueba", "Mensaje de prueba");

        // Mockear repositorio para que devuelva 2 suscriptores verificados
        List<Suscriptor> suscriptoresMock = List.of(
            new Suscriptor("usuario1@example.com"),
            new Suscriptor("usuario2@example.com")
        );
        when(suscriptorRepo.findByVerificadoTrue()).thenReturn(suscriptoresMock);

        when(newsletterRepo.save(any(Newsletter.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/api/admin/newsletter/enviar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newsletterDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Newsletter enviada correctamente"))
                .andExpect(jsonPath("$.data").value("Newsletter enviada a 2 suscriptores."));

        // Verificar que se guardó la newsletter en la base de datos
        ArgumentCaptor<Newsletter> newsletterCaptor = ArgumentCaptor.forClass(Newsletter.class);
        verify(newsletterRepo).save(newsletterCaptor.capture());
        assertEquals("Asunto de prueba", newsletterCaptor.getValue().getAsunto());
        assertEquals("Mensaje de prueba", newsletterCaptor.getValue().getContenido());

        // Verificar que se envió el correo electrónico a los suscriptores
        verify(emailService, times(suscriptoresMock.size())).sendEmail(
            any(String.class), 
            eq("Asunto de prueba"),
            any(String.class)
        );
    }

    @Test
    @DisplayName("Controlador - Enviar Newsletter error al enviar correo")
    public void testEnviarNewsletterErrorAlEnviarCorreo() throws Exception {
        NewsletterDTO newsletterDTO = new NewsletterDTO("Asunto de prueba", "Mensaje de prueba");

        List<Suscriptor> suscriptoresMock = List.of(new Suscriptor("user@example.com"));
        when(suscriptorRepo.findByVerificadoTrue()).thenReturn(suscriptoresMock);
        when(newsletterRepo.save(any(Newsletter.class))).thenAnswer(inv -> inv.getArgument(0));

        doThrow(new EmailException.EnvioFallido(new RuntimeException("Error de envío")))
            .when(emailService).sendEmail(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/admin/newsletter/enviar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsletterDTO)))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.error").value("Failed to send email"))
            .andExpect(jsonPath("$.message").value("Failed to send email"))
            .andExpect(jsonPath("$.path").value("/api/admin/newsletter/enviar"));
    }

    @Test
    @DisplayName("Controlador - Enviar Newsletter error de validación")
    public void testEnviarNewsletterErrorValidacion() throws Exception {
        NewsletterDTO newsletterDTO = new NewsletterDTO("", ""); // Asunto y mensaje vacíos

        mockMvc.perform(post("/api/admin/newsletter/enviar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newsletterDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation errors"))
                .andExpect(jsonPath("$.message").value("Please check the marked fields."))
                .andExpect(jsonPath("$.path").value("/api/admin/newsletter/enviar"))
                .andExpect(jsonPath("$.validationErrors.asunto").value("El asunto es obligatorio"))
                .andExpect(jsonPath("$.validationErrors.mensaje").value("El mensaje es obligatorio"));
    }
}
