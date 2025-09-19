package es.uca.tfg.ceramic_affair_web.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SuscripcionDTO {
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "El token de reCAPTCHA es obligatorio")
    private String recaptchaToken;

    public SuscripcionDTO() {
        // Constructor por defecto
    }

    public SuscripcionDTO(String email, String recaptchaToken) {
        this.email = email;
        this.recaptchaToken = recaptchaToken;
    }

    public String getEmail() {
        return email;
    }

    public String getRecaptchaToken() {
        return recaptchaToken;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRecaptchaToken(String recaptchaToken) {
        this.recaptchaToken = recaptchaToken;
    }
}
