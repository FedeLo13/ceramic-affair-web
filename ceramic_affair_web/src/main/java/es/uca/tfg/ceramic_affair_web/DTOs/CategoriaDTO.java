package es.uca.tfg.ceramic_affair_web.DTOs;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para la entidad Categoria.
 * Este DTO se utiliza para transferir datos de categorías entre la capa de presentación y la capa de servicio.
 * 
 * @version 1.1
 */
public class CategoriaDTO {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombre;

    public CategoriaDTO() {
        // Constructor por defecto
    }

    public CategoriaDTO(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
