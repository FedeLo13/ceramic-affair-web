package es.uca.tfg.ceramic_affair_web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import com.fasterxml.jackson.databind.ObjectMapper;

import es.uca.tfg.ceramic_affair_web.DTOs.SuscripcionDTO;
import es.uca.tfg.ceramic_affair_web.controllers.common.SuscriptorController;
import es.uca.tfg.ceramic_affair_web.entities.Suscriptor;
import es.uca.tfg.ceramic_affair_web.exceptions.SuscriptorExceptionHandler;
import es.uca.tfg.ceramic_affair_web.repositories.SuscriptorRepo;
import es.uca.tfg.ceramic_affair_web.security.JwtUtils;
import es.uca.tfg.ceramic_affair_web.services.EmailService;
import es.uca.tfg.ceramic_affair_web.services.RecaptchaService;

/**
 * Test para el controlador SuscriptorController.
 * Verifica el correcto funcionamiento de los endpoints relacionados con la suscripción y verificación de suscriptores.
 * 
 * @version 1.0
 */
@WebMvcTest(controllers = SuscriptorController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SuscriptorExceptionHandler.class))
@AutoConfigureMockMvc
public class SuscriptorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SuscriptorRepo suscriptorRepo;

    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private RecaptchaService recaptchaService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Controlador - Suscribir un nuevo suscriptor")
    void testSuscribirNuevoSuscriptor() throws Exception {
        SuscripcionDTO dto = new SuscripcionDTO("test@example.com", "recaptcha-token");
        when(recaptchaService.verifyRecaptcha(dto.getRecaptchaToken())).thenReturn(true);
        when(suscriptorRepo.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(suscriptorRepo.save(any((Suscriptor.class)))).thenAnswer(inv -> {
            Suscriptor suscriptor = inv.getArgument(0);
            suscriptor.setId(1L); // Simular ID generado
            return suscriptor;
        });

        //Act & Assert
        mockMvc.perform(post("/api/public/suscriptores/suscribir")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Suscriptor creado y correo de verificación enviado"))
                .andExpect(jsonPath("$.data").value("Verification email has been sent"));

        // Verificar que se envió el correo electrónico
        ArgumentCaptor<Suscriptor> captor = ArgumentCaptor.forClass(Suscriptor.class);
        verify(suscriptorRepo).save(captor.capture());
        verify(emailService).sendEmail(eq("test@example.com"), anyString(), anyString());

        Suscriptor savedSuscriptor = captor.getValue();
        assert(!savedSuscriptor.isVerificado()); // Verificar que el suscriptor no está verificado
    }

    @Test
    @DisplayName("Controlador - Suscribir un suscriptor ya verificado")
    void testSuscribirSuscriptorYaVerificado() throws Exception {
        Suscriptor existingSuscriptor = new Suscriptor("test@example.com");
        existingSuscriptor.setVerificado(true);

        when(recaptchaService.verifyRecaptcha(anyString())).thenReturn(true);
        when(suscriptorRepo.findByEmail(existingSuscriptor.getEmail())).thenReturn(Optional.of(existingSuscriptor));

        SuscripcionDTO dto = new SuscripcionDTO(existingSuscriptor.getEmail(), "recaptcha-token");

        mockMvc.perform(post("/api/public/suscriptores/suscribir")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Business exception"))
                .andExpect(jsonPath("$.message").value("The subscriber with email: test@example.com is already subscribed"))
                .andExpect(jsonPath("$.path").value("/api/public/suscriptores/suscribir"));
    }

    @Test
    @DisplayName("Controlador - Suscribir un suscriptor con verificación pendiente")
    void testSuscribirSuscriptorConVerificacionPendiente() throws Exception {
        Suscriptor existingSuscriptor = new Suscriptor("test@example.com");
        existingSuscriptor.setVerificado(false);
        existingSuscriptor.setFechaExpiracionToken(LocalDateTime.now().plusDays(1)); // Token no expirado

        when(recaptchaService.verifyRecaptcha(anyString())).thenReturn(true);
        when(suscriptorRepo.findByEmail(existingSuscriptor.getEmail())).thenReturn(Optional.of(existingSuscriptor));

        SuscripcionDTO dto = new SuscripcionDTO(existingSuscriptor.getEmail(), "recaptcha-token");

        mockMvc.perform(post("/api/public/suscriptores/suscribir")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Business exception"))
                .andExpect(jsonPath("$.message").value("The verification of the subscriber with email: test@example.com is pending"))
                .andExpect(jsonPath("$.path").value("/api/public/suscriptores/suscribir"));
    }

    @Test
    @DisplayName("Controlador - Suscribir un suscriptor no verificado con token expirado -> Regenerar token y reenviar correo")
    void testSuscribirSuscriptorNoVerificadoTokenExpirado() throws Exception {
        Suscriptor existingSuscriptor = new Suscriptor("test@example.com");
        existingSuscriptor.setVerificado(false);
        existingSuscriptor.setFechaExpiracionToken(LocalDateTime.now().minusDays(1)); // Token expirado

        when(recaptchaService.verifyRecaptcha(anyString())).thenReturn(true);
        when(suscriptorRepo.findByEmail(existingSuscriptor.getEmail())).thenReturn(Optional.of(existingSuscriptor));
        when(suscriptorRepo.save(any(Suscriptor.class))).thenReturn(existingSuscriptor);

        SuscripcionDTO dto = new SuscripcionDTO(existingSuscriptor.getEmail(), "recaptcha-token");

        mockMvc.perform(post("/api/public/suscriptores/suscribir")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Suscriptor encontrado y correo de verificación reenviado"))
                .andExpect(jsonPath("$.data").value("Verification email has been resent"));

        // Verificar que se envió el correo electrónico de verificación
        verify(emailService, times(1)).sendEmail(eq(existingSuscriptor.getEmail()), anyString(), anyString());
    }

    @Test
    @DisplayName("Controlador - Suscribir un suscriptor con reCAPTCHA inválido")
    void testSuscribirSuscriptorRecaptchaInvalido() throws Exception {
        when(recaptchaService.verifyRecaptcha(anyString())).thenReturn(false);

        SuscripcionDTO dto = new SuscripcionDTO("test@example.com", "recaptcha-token");
        mockMvc.perform(post("/api/public/suscriptores/suscribir")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Invalid reCAPTCHA"))
                .andExpect(jsonPath("$.message").value("The reCAPTCHA token is invalid or has expired. Please try again."))
                .andExpect(jsonPath("$.path").value("/api/public/suscriptores/suscribir"));
    }

    @Test
    @DisplayName("Controlador - Desuscribir un suscriptor")
    void testDesuscribirSuscriptor() throws Exception {
        Suscriptor suscriptor = new Suscriptor("test@example.com");
        suscriptor.setTokenDesuscripcion("desuscripcion-token");

        when(suscriptorRepo.findByTokenDesuscripcion("desuscripcion-token")).thenReturn(Optional.of(suscriptor));

        mockMvc.perform(get("/api/public/suscriptores/desuscribir")
                    .param("token", "desuscripcion-token"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost:5173/confirmation?status=unsubscribed"));

        // Verificar que el suscriptor fue eliminado
        verify(suscriptorRepo).delete(suscriptor);
    }

    @Test
    @DisplayName("Controlador - Desuscribir un suscriptor con token no encontrado")
    void testDesuscribirSuscriptorConTokenNoEncontrado() throws Exception {
        when(suscriptorRepo.findByTokenDesuscripcion("invalid-token")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/public/suscriptores/desuscribir")
                    .param("token", "invalid-token"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost:5173/confirmation?status=not_found"));
    }

    @Test
    @DisplayName("Controlador - Verificar un suscriptor con token válido")
    void testVerificarSuscriptorConTokenValido() throws Exception {
        Suscriptor suscriptor = new Suscriptor("test@example.com");
        suscriptor.setFechaExpiracionToken(LocalDateTime.now().plusDays(1)); // Token válido

        when(suscriptorRepo.findByTokenVerificacion("valid-token")).thenReturn(Optional.of(suscriptor));
        when(suscriptorRepo.save(any(Suscriptor.class))).thenReturn(suscriptor);

        mockMvc.perform(get("/api/public/suscriptores/verificar")
                    .param("token", "valid-token"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost:5173/confirmation?status=subscribed"));

        // Verificar que el suscriptor fue marcado como verificado
        verify(suscriptorRepo).save(any(Suscriptor.class));
    }

    @Test
    @DisplayName("Controlador - Verificar un suscriptor con token no encontrado")
    void testVerificarSuscriptorConTokenNoEncontrado() throws Exception {
        when(suscriptorRepo.findByTokenVerificacion("invalid-token")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/public/suscriptores/verificar")
                    .param("token", "invalid-token"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost:5173/confirmation?status=not_found"));
    }

    @Test
    @DisplayName("Controlador - Verificar un suscriptor con token expirado")
    void testVerificarSuscriptorConTokenExpirado() throws Exception {
        Suscriptor suscriptor = new Suscriptor("test@example.com");
        suscriptor.setFechaExpiracionToken(LocalDateTime.now().minusDays(1)); // Token expirado

        when(suscriptorRepo.findByTokenVerificacion("expired-token")).thenReturn(Optional.of(suscriptor));

        mockMvc.perform(get("/api/public/suscriptores/verificar")
                    .param("token", "expired-token"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost:5173/confirmation?status=expired"));
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
