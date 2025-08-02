package es.uca.tfg.ceramic_affair_web.DTOs;

import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para la entidad FindMePost.
 * Este DTO se utiliza para transferir datos de publicaciones "Encuéntrame" entre la capa de presentación y la capa de servicio.
 * 
 * @version 1.0
 */
public class FindMePostDTO {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDateTime fechaFin;

    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90.0", inclusive = true, message = "Latitud mínima es -90")
    @DecimalMax(value = "90.0", inclusive = true, message = "LLatitud máxima es 90")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-180.0", inclusive = true, message = "Longitud mínima es -180")
    @DecimalMax(value = "180.0", inclusive = true, message = "Longitud máxima es 180")
    private Double longitud;

    public FindMePostDTO() {
        // Constructor por defecto
    }

    // Constructor para crear un FindMePostDTO con ID, usar este constructor para crear un FindMePostDTO a partir de un FindMePost existente
    public FindMePostDTO(Long id, String titulo, String descripcion, LocalDateTime fechaInicio, LocalDateTime fechaFin, Double latitud, Double longitud) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Constructor para crear un FindMePostDTO sin ID, usar este constructor para crear un nuevo DTO
    // con el objetivo de guardarlo en la base de datos
    public FindMePostDTO(String titulo, String descripcion, LocalDateTime fechaInicio, LocalDateTime fechaFin, Double latitud, Double longitud) {
        this.id = null; // ID será generado por la base de datos (usar el otro constructor para crear un DTO con ID)
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
