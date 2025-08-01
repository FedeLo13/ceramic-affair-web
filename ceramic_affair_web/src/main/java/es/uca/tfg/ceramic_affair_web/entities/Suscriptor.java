package es.uca.tfg.ceramic_affair_web.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Clase que representa un suscriptor en el sistema.
 * 
 * @version 1.0
 */
@Entity
public class Suscriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean verificado;

    @Column(unique = true)
    private String tokenVerificacion;

    @Column(nullable = false)
    private LocalDateTime fechaCreacionToken;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracionToken;

    /**
     * Constructor vacío para JPA.
     */
    public Suscriptor() {
    }

    /**
     * Constructor con parámetros para crear un suscriptor.
     * 
     * @param email el correo electrónico del suscriptor
     */
    public Suscriptor(String email) {
        this.email = email;
        this.verificado = false;
        generarNuevoTokenVerificacion();
    }

    /**
     * Método para obtener el ID del suscriptor.
     * 
     * @return el ID del suscriptor
     */
    public Long getId() {
        return id;
    }

    /**
     * Método para establecer el ID del suscriptor.
     * 
     * @param id el nuevo ID del suscriptor
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Método para obtener el correo electrónico del suscriptor.
     * 
     * @return el correo electrónico del suscriptor
     */
    public String getEmail() {
        return email;
    }

    /**
     * Método para saber si el suscriptor está verificado.
     * 
     * @return true si el suscriptor está verificado, false en caso contrario
     */
    public boolean isVerificado() {
        return verificado;
    }

    /**
     * Método para obtener el token de verificación del suscriptor.
     * 
     * @return el token de verificación del suscriptor
     */
    public String getTokenVerificacion() {
        return tokenVerificacion;
    }

    /**
     * Método para obtener la fecha de creación del token de verificación del suscriptor.
     * 
     * @return la fecha de creación del token de verificación del suscriptor
     */
    public LocalDateTime getFechaCreacionToken() {
        return fechaCreacionToken;
    }

    /**
     * Método para obtener la fecha de expiración del token de verificación del suscriptor.
     * 
     * @return la fecha de expiración del token de verificación del suscriptor
     */
    public LocalDateTime getFechaExpiracionToken() {
        return fechaExpiracionToken;
    }

    /**
     * Método para establecer el correo electrónico del suscriptor.
     * 
     * @param email el nuevo correo electrónico del suscriptor
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Método para establecer el estado de verificación del suscriptor.
     * 
     * @param verificado el nuevo estado de verificación del suscriptor
     */
    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    /**
     * Método para establecer el token de verificación del suscriptor.
     * 
     * @param tokenVerificacion el nuevo token de verificación del suscriptor
     */
    public void setTokenVerificacion(String tokenVerificacion) {
        this.tokenVerificacion = tokenVerificacion;
    }

    /**
     * Método para establecer la fecha de creación del suscriptor.
     * 
     * @param fechaCreacion la nueva fecha de creación del suscriptor
     */
    public void setFechaCreacionToken(LocalDateTime fechaCreacion) {
        this.fechaCreacionToken = fechaCreacion;
    }

    /**
     * Método para establecer la fecha de expiración del token de verificación del suscriptor.
     * 
     * @param fechaExpiracion la nueva fecha de expiración del token de verificación del suscriptor
     */
    public void setFechaExpiracionToken(LocalDateTime fechaExpiracion) {
        this.fechaExpiracionToken = fechaExpiracion;
    }

    /**
     * Método para verificar el suscriptor.
     */
    public void verificar() {
        this.verificado = true;
    }

    /**
     * Método para regenerar el token de verificación del suscriptor.
     */
    public void regenerarTokenVerificacion() {
        generarNuevoTokenVerificacion();
    }

    private void generarNuevoTokenVerificacion() {
        this.tokenVerificacion = UUID.randomUUID().toString();
        this.fechaCreacionToken = LocalDateTime.now();
        this.fechaExpiracionToken = this.fechaCreacionToken.plusDays(1); // Token válido por 1 día
    }
}
