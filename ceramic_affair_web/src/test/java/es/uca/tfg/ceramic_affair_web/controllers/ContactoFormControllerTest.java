package es.uca.tfg.ceramic_affair_web.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.uca.tfg.ceramic_affair_web.DTOs.ContactoFormDTO;
import es.uca.tfg.ceramic_affair_web.controllers.common.ContactoFormController;
import es.uca.tfg.ceramic_affair_web.exceptions.EmailException;
import es.uca.tfg.ceramic_affair_web.repositories.ContactoFormRepo;
import es.uca.tfg.ceramic_affair_web.security.JwtUtils;
import es.uca.tfg.ceramic_affair_web.services.EmailService;
import es.uca.tfg.ceramic_affair_web.services.RecaptchaService;

@WebMvcTest(controllers = ContactoFormController.class)
@AutoConfigureMockMvc
public class ContactoFormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ContactoFormRepo contactoFormRepo;

    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private RecaptchaService recaptchaService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Controlador - Enviar formulario de contacto")
    void testEnviarFormularioContacto() throws Exception {
        ContactoFormDTO dto = new ContactoFormDTO();
        dto.setNombre("John");
        dto.setApellidos("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setAsunto("Test Subject");
        dto.setMensaje("Hello, this is a test message.");
        dto.setRecaptchaToken("valid-token");

        // Simular la verificación del reCAPTCHA
        when(recaptchaService.verifyRecaptcha(dto.getRecaptchaToken())).thenReturn(true);

        mockMvc.perform(post("/api/public/contacto/enviar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Formulario enviado correctamente"))
            .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("Controlador - Enviar formulario de contacto con reCAPTCHA inválido")
    void testEnviarFormularioContactoRecaptchaInvalido() throws Exception {
        ContactoFormDTO dto = new ContactoFormDTO();
        dto.setNombre("John");
        dto.setApellidos("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setAsunto("Test Subject");
        dto.setMensaje("Hello, this is a test message.");
        dto.setRecaptchaToken("invalid-token");

        // Simular la verificación del reCAPTCHA
        when(recaptchaService.verifyRecaptcha(dto.getRecaptchaToken())).thenReturn(false);

        mockMvc.perform(post("/api/public/contacto/enviar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Invalid reCAPTCHA"))
            .andExpect(jsonPath("$.message").value("The reCAPTCHA token is invalid or has expired. Please try again."))
            .andExpect(jsonPath("$.path").value("/api/public/contacto/enviar"));
    }

    @Test
    @DisplayName("Controlador - Enviar formulario de contacto con error al enviar correo")
    void testEnviarFormularioContactoErrorEnvioCorreo() throws Exception {
        ContactoFormDTO dto = new ContactoFormDTO();
        dto.setNombre("John");
        dto.setApellidos("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setAsunto("Test Subject");
        dto.setMensaje("Hello, this is a test message.");
        dto.setRecaptchaToken("valid-token");

        // Simular la verificación del reCAPTCHA
        when(recaptchaService.verifyRecaptcha(dto.getRecaptchaToken())).thenReturn(true);
        doThrow(new EmailException.EnvioFallido(new RuntimeException("Error al enviar el correo")))
            .when(emailService).sendEmail(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/public/contacto/enviar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.error").value("Failed to send email"))
            .andExpect(jsonPath("$.message").value("Failed to send email"))
            .andExpect(jsonPath("$.path").value("/api/public/contacto/enviar"));
    }

    @TestConfiguration
    public static class NoSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll()
                );
            return http.build();
        }
    }

}
