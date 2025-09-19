package es.uca.tfg.ceramic_affair_web.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Clase de configuración para las propiedades de imágenes de Ceramic Affair.
 * Esta clase se encarga de cargar las propiedades relacionadas con las imágenes
 * 
 * @version 1.0
 */
@ConfigurationProperties(prefix = "ceramic.affair.images")
public class ImagenProperties {
    private String path = "uploads";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
