package es.uca.tfg.ceramic_affair_web.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

/**
 * Clase que representa una newsletter en el sistema.
 * 
 * @version 1.0
 */
@Entity
public class Newsletter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String asunto;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Lob
    private String contenido;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    /**
     * Constructor vacío para JPA.
     */
    public Newsletter() {
    }

    /**
     * Constructor con parámetros para crear una newsletter.
     * 
     * @param asunto        el asunto de la newsletter
     * @param contenido     el contenido de la newsletter
     */
    public Newsletter(String asunto, String contenido) {
        this.asunto = asunto;
        this.contenido = contenido;
    }

    /**
     * Método para obtener el ID de la newsletter.
     * 
     * @return el ID de la newsletter
     */
    public Long getId() {
        return id;
    }

    /**
     * Método para obtener el asunto de la newsletter.
     * 
     * @return el asunto de la newsletter
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Método para establecer el asunto de la newsletter.
     * 
     * @param asunto el nuevo asunto de la newsletter
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * Método para obtener el contenido de la newsletter.
     * 
     * @return el contenido de la newsletter
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Método para establecer el contenido de la newsletter.
     * 
     * @param contenido el nuevo contenido de la newsletter
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * Método para obtener la fecha de creación de la newsletter.
     * 
     * @return la fecha de creación de la newsletter
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Método para establecer la fecha de creación de la newsletter.
     * 
     * @param fechaCreacion la nueva fecha de creación de la newsletter
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
