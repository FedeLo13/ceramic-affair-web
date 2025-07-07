package es.uca.tfg.ceramic_affair_web.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Clase que representa una imagen en el sistema
 * 
 * @version 1.0
 */
@Entity
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String ruta;

    @Column(nullable = false, length = 20)
    private String formato;

    private float tamano;
    private float ancho;
    private float alto;

    /**
     * Constructor vacío para JPA
     */
    public Imagen() {
    }

    /**
     * Constructor con parámetros para crear una imagen.
     * 
     * @param ruta   la ruta de la imagen
     * @param formato el formato de la imagen (ej. "jpg", "png")
     * @param tamano  el tamaño de la imagen en bytes
     * @param ancho   el ancho de la imagen en píxeles
     * @param alto    el alto de la imagen en píxeles
     */
    public Imagen(String ruta, String formato, float tamano, float ancho, float alto) {
        this.ruta = ruta;
        this.formato = formato;
        this.tamano = tamano;
        this.ancho = ancho;
        this.alto = alto;
    }

    /**
     * Método para obtener el ID de la imagen.
     * 
     * @return el ID de la imagen
     */
    public Long getId() {
        return id;
    }

    /**
     * Método para obtener la ruta de la imagen.
     * 
     * @return la ruta de la imagen
     */
    public String getRuta() {
        return ruta;
    }

    /**
     * Método para obtener el formato de la imagen.
     * 
     * @return el formato de la imagen
     */
    public String getFormato() {
        return formato;
    }

    /**
     * Método para obtener el tamaño de la imagen.
     * 
     * @return el tamaño de la imagen en bytes
     */
    public float getTamano() {
        return tamano;
    }

    /**
     * Método para obtener el ancho de la imagen.
     * 
     * @return el ancho de la imagen en píxeles
     */
    public float getAncho() {
        return ancho;
    }

    /**
     * Método para obtener el alto de la imagen.
     * 
     * @return el alto de la imagen en píxeles
     */
    public float getAlto() {
        return alto;
    }
}
