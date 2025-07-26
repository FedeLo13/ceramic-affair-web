package es.uca.tfg.ceramic_affair_web.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para el formulario de contacto.
 * Este DTO se utiliza para transferir datos del formulario de contacto entre la capa de presentación y la capa de servicio.
 * 
 * @version 1.0
 */
public class ContactoFormDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorios")
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 255, message = "El asunto no puede exceder los 255 caracteres")
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @NotBlank(message = "El token de reCAPTCHA es obligatorio")
    private String recaptchaToken;

    public ContactoFormDTO() {
        // Constructor por defecto
    }

    public ContactoFormDTO(String nombre, String apellidos, String email, String asunto, String mensaje, String recaptchaToken) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.recaptchaToken = recaptchaToken;
    }

    // Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getRecaptchaToken() {
        return recaptchaToken;
    }

    public void setRecaptchaToken(String recaptchaToken) {
        this.recaptchaToken = recaptchaToken;
    }
}
