package es.uca.tfg.ceramic_affair_web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import es.uca.tfg.ceramic_affair_web.DTOs.RecaptchaResponse;

/** 
 * Servicio para la verificación de reCAPTCHA 
 * 
 * @version 1.0
 */
// TODO: Poner el dominio de producción en la configuración de reCAPTCHA
@Service
public class RecaptchaService {

    @Value("${recaptcha.secret-key}")
    private String recaptchaSecretKey;

    private final WebClient webClient;

    @Autowired
    public RecaptchaService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.google.com/recaptcha/api").build();
    }

    // Constructor necesario para pruebas unitarias
    public RecaptchaService(WebClient webClient) {
        this.webClient = webClient;
    }

    public boolean verifyRecaptcha(String recaptchaResponse) {
        String url = "https://www.google.com/recaptcha/api/siteverify";

        RecaptchaResponse response = webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("secret=" + recaptchaSecretKey + "&response=" + recaptchaResponse)
                .retrieve()
                .bodyToMono(RecaptchaResponse.class)
                .block();

        return response != null && response.isSuccess() && response.getScore() >= 0.5;
    }
}
