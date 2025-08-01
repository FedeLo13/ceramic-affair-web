package es.uca.tfg.ceramic_affair_web.DTOs;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para la entidad Newsletter.
 * Este DTO se utiliza para transferir datos de newsletters entre la capa de presentación y la capa de servicio.
 * 
 * @version 1.0
 */
public class NewsletterDTO {

    @NotBlank(message = "El asunto es obligatorio")
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    public NewsletterDTO() {
        // Constructor por defecto
    }

    public NewsletterDTO(String asunto, String mensaje) {
        this.asunto = asunto;
        this.mensaje = mensaje;
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
}
