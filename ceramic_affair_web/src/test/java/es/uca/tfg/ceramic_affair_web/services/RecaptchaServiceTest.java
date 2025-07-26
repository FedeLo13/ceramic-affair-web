package es.uca.tfg.ceramic_affair_web.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import es.uca.tfg.ceramic_affair_web.DTOs.RecaptchaResponse;
import reactor.core.publisher.Mono;

/**
 * Clase de prueba para el servicio RecaptchaService.
 * Proporciona pruebas unitarias para las operaciones relacionadas con la verificación de reCAPTCHA
 * 
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class RecaptchaServiceTest {

    private WebClient webClient;
    private WebClient.RequestBodyUriSpec uriSpec;
    private WebClient.RequestBodySpec bodySpec;
    private WebClient.RequestHeadersSpec<?> headersSpec;
    private WebClient.ResponseSpec responseSpec;

    private RecaptchaService recaptchaService;

    @BeforeEach
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void setUp() {
        webClient = mock(WebClient.class);
        uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        bodySpec = mock(WebClient.RequestBodySpec.class);
        headersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(bodySpec);
        when(bodySpec.contentType(MediaType.APPLICATION_FORM_URLENCODED)).thenReturn(bodySpec);
        when(bodySpec.bodyValue(anyString())).thenReturn((WebClient.RequestHeadersSpec) headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        recaptchaService = new RecaptchaService(webClient);
    }

    @Test
    @DisplayName("Servicio - Verificar reCAPTCHA")
    void testVerificarRecaptcha() {
        RecaptchaResponse mockResponse = new RecaptchaResponse(true, 0.9f, "success", LocalDateTime.now(), "localhost", null);
        when(responseSpec.bodyToMono(RecaptchaResponse.class)).thenReturn(Mono.just(mockResponse));

        boolean result = recaptchaService.verifyRecaptcha("test-token");

        assertTrue(result, "El resultado de la verificación de reCAPTCHA debería ser verdadero");
    }

    @Test
    @DisplayName("Servicio - Verificar reCAPTCHA (fallo)")
    void testVerificarRecaptchaFallo() {
        RecaptchaResponse mockResponse = new RecaptchaResponse(false, 0.1f, "error", LocalDateTime.now(), "localhost", null);
        when(responseSpec.bodyToMono(RecaptchaResponse.class)).thenReturn(Mono.just(mockResponse));

        boolean result = recaptchaService.verifyRecaptcha("test-token");

        assertFalse(result, "El resultado de la verificación de reCAPTCHA debería ser falso");
    }
}
