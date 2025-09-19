package es.uca.tfg.ceramic_affair_web.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

/**
 * Clase que representa una publicación de tipo "Encuéntrame" en el sistema.
 * 
 * @version 1.0
 */
@Entity
public class FindMePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    @Lob
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    /**
     * Constructor vacío para JPA.
     */
    public FindMePost() {
    }

    /**
     * Constructor con parámetros para crear una publicación "Encuéntrame".
     * 
     * @param titulo       el título de la publicación
     * @param descripcion  la descripción de la publicación
     * @param fechaInicio  la fecha y hora de inicio de la publicación
     * @param fechaFin     la fecha y hora de finalización de la publicación
     * @param latitud      la latitud del lugar asociado a la publicación
     * @param longitud     la longitud del lugar asociado a la publicación
     */
    public FindMePost(String titulo, String descripcion, LocalDateTime fechaInicio, LocalDateTime fechaFin, Double latitud, Double longitud) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    /**
     * Método para obtener el ID de la publicación.
     * 
     * @return el ID de la publicación
     */
    public Long getId() {
        return id;
    }

    /**
     * Método para establecer el ID de la publicación.
     * 
     * @param id el nuevo ID de la publicación
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Método para obtener el título de la publicación.
     * 
     * @return el título de la publicación
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Método para establecer el título de la publicación.
     * 
     * @param titulo el nuevo título de la publicación
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Método para obtener la descripción de la publicación.
     * 
     * @return la descripción de la publicación
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Método para establecer la descripción de la publicación.
     * 
     * @param descripcion la nueva descripción de la publicación
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Método para obtener la fecha y hora de inicio de la publicación.
     * 
     * @return la fecha y hora de inicio de la publicación
     */
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Método para establecer la fecha y hora de inicio de la publicación.
     * 
     * @param fechaInicio la nueva fecha y hora de inicio de la publicación
     */
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Método para obtener la fecha y hora de finalización de la publicación.
     * 
     * @return la fecha y hora de finalización de la publicación
     */
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    /**
     * Método para establecer la fecha y hora de finalización de la publicación.
     * 
     * @param fechaFin la nueva fecha y hora de finalización de la publicación
     */
    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Método para obtener la latitud del lugar asociado a la publicación.
     * 
     * @return la latitud del lugar asociado a la publicación
     */
    public Double getLatitud() {
        return latitud;
    }

    /**
     * Método para establecer la latitud del lugar asociado a la publicación.
     * 
     * @param latitud la nueva latitud del lugar asociado a la publicación
     */
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    /**
     * Método para obtener la longitud del lugar asociado a la publicación.
     * 
     * @return la longitud del lugar asociado a la publicación
     */
    public Double getLongitud() {
        return longitud;
    }

    /**
     * Método para establecer la longitud del lugar asociado a la publicación.
     * 
     * @param longitud la nueva longitud del lugar asociado a la publicación
     */
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
