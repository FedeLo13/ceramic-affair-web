package es.uca.tfg.ceramic_affair_web.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para el inicio de sesión.
 * Este DTO se utiliza para transferir datos de inicio de sesión entre la capa de presentación y la capa de servicio.
 * 
 * @version 1.0
 */
public class LoginDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public LoginDTO() {
        // Constructor por defecto
    }

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
