package es.uca.tfg.ceramic_affair_web.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Clase de prueba para la entidad PlantillaNewsletter.
 * Proporciona pruebas unitarias para los métodos de la clase PlantillaNewsletter.
 * 
 * @version 1.0
 */
public class PlantillaNewsletterTest {

    @Test
    @DisplayName("PlantillaNewsletter - Constructor vacío")
    public void testConstructorVacio() {
        PlantillaNewsletter plantilla = new PlantillaNewsletter();

        assertNotNull(plantilla); // Verifica que la instancia no sea nula
        assertEquals(Long.valueOf(1L), plantilla.getId()); // Verifica que el ID sea 1
        assertNull(plantilla.getAsunto()); // Verifica que el asunto sea nulo (no se ha establecido)
        assertNull(plantilla.getContenido()); // Verifica que el contenido sea nulo (no se ha establecido)
    }

    @Test
    @DisplayName("PlantillaNewsletter - Constructor con parámetros")
    public void testConstructorConParametros() {
        String asunto = "Bienvenido a Ceramic Affair";
        String contenido = "<h1>Hola,</h1><p>Gracias por unirte a nuestra comunidad.</p>";
        PlantillaNewsletter plantilla = new PlantillaNewsletter(asunto, contenido);

        assertNotNull(plantilla); // Verifica que la instancia no sea nula
        assertEquals(Long.valueOf(1L), plantilla.getId()); // Verifica que el ID sea 1
        assertEquals(asunto, plantilla.getAsunto()); // Verifica que el asunto se haya establecido correctamente
        assertEquals(contenido, plantilla.getContenido()); // Verifica que el contenido se haya establecido correctamente
    }

    @Test
    @DisplayName("PlantillaNewsletter - Getters y Setters")
    public void testGettersYSetters() {
        PlantillaNewsletter plantilla = new PlantillaNewsletter();
        String asunto = "Actualización de Producto";
        String contenido = "<p>Tenemos una nueva actualización para ti.</p>";

        plantilla.setAsunto(asunto);
        plantilla.setContenido(contenido);

        assertEquals(asunto, plantilla.getAsunto()); // Verifica que el asunto se haya establecido correctamente
        assertEquals(contenido, plantilla.getContenido()); // Verifica que el contenido se haya establecido correctamente
    }

    @Test
    @DisplayName("PlantillaNewsletter - Unicidad del ID")
    public void testUnicidad() {
        PlantillaNewsletter plantilla1 = new PlantillaNewsletter("Asunto 1", "Contenido 1");
        PlantillaNewsletter plantilla2 = new PlantillaNewsletter("Asunto 2", "Contenido 2");

        assertEquals(Long.valueOf(1L), plantilla1.getId()); // Verifica que el ID sea siempre 1
        assertEquals(Long.valueOf(1L), plantilla2.getId()); // Verifica que el ID sea siempre 1
        assertNotEquals(plantilla1.getAsunto(), plantilla2.getAsunto()); // Los asuntos pueden ser diferentes
        assertNotEquals(plantilla1.getContenido(), plantilla2.getContenido()); // Los contenidos pueden ser diferentes
    }
}
