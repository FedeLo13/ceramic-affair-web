package es.uca.tfg.ceramic_affair_web.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

/**
 * Clase que representa una plantilla de newsletter en el sistema.
 * 
 * @version 1.0
 */
@Entity
public class PlantillaNewsletter {

    @Id
    private Long id = 1L; // ID fijo para asegurar que solo haya una plantilla

    @Column(nullable = false)
    private String asunto;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Lob
    private String contenido;

    /**
     * Constructor vacío para JPA.
     */
    public PlantillaNewsletter() {
    }

    /**
     * Constructor con parámetros para crear una plantilla de newsletter.
     * 
     * @param asunto    el asunto de la plantilla
     * @param contenido el contenido de la plantilla
     */
    public PlantillaNewsletter(String asunto, String contenido) {
        this.id = 1L; // Aseguramos que el ID sea siempre 1
        this.asunto = asunto;
        this.contenido = contenido;
    }

    /**
     * Método para obtener el ID de la plantilla.
     * 
     * @return el ID de la plantilla
     */
    public Long getId() {
        return id;
    }

    /**
     * Método para obtener el asunto de la plantilla.
     * 
     * @return el asunto de la plantilla
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Método para obtener el contenido de la plantilla.
     * 
     * @return el contenido de la plantilla
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Método para establecer el asunto de la plantilla.
     * 
     * @param asunto el nuevo asunto de la plantilla
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * Método para establecer el contenido de la plantilla.
     * 
     * @param contenido el nuevo contenido de la plantilla
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
