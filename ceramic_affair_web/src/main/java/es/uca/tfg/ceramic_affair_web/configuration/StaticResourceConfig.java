package es.uca.tfg.ceramic_affair_web.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración para los recursos estáticos de la aplicación.
 * Esta clase se encarga de definir la ubicación de los recursos estáticos
 * 
 * @version 1.1
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${ceramic.affair.images.path}")
    private String imagesPath;

    /**
     * Configura la ubicación de los recursos estáticos.
     * 
     * @param registry el registrador de recursos estáticos
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configura la ruta para servir imágenes desde el directorio "uploads"
        String resourceLocation = "file:" + (imagesPath.endsWith("/") ? imagesPath : imagesPath + "/");
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation);
        System.out.println("Sirviendo imágenes desde: " + resourceLocation);
    }
}
