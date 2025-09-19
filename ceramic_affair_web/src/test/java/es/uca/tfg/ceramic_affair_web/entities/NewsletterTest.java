package es.uca.tfg.ceramic_affair_web.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Clase de prueba para la entidad Newsletter.
 * Proporciona pruebas unitarias para los métodos de la clase Newsletter.
 * 
 * @version 1.0
 */
public class NewsletterTest {

    @Test
    @DisplayName("Newsletter - Constructor vacío")
    public void testConstructorVacio() {
        Newsletter newsletter = new Newsletter();

        assertNotNull(newsletter); // Verifica que la instancia no sea nula
        assertNull(newsletter.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertNull(newsletter.getAsunto()); // Verifica que el asunto sea nulo (no se ha establecido)
        assertNull(newsletter.getContenido()); // Verifica que el contenido sea nulo (no se ha establecido)
        assertNull(newsletter.getFechaCreacion()); // Verifica que la fecha de creación sea nula (aún no persiste en la base de datos)
    }

    @Test
    @DisplayName("Newsletter - Constructor con parámetros")
    public void testConstructorConParametros() {
        String asunto = "Novedades de la semana";
        String contenido = "Esta semana tenemos grandes ofertas en nuestra tienda.";
        Newsletter newsletter = new Newsletter(asunto, contenido);

        assertNotNull(newsletter); // Verifica que la instancia no sea nula
        assertNull(newsletter.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertEquals(asunto, newsletter.getAsunto()); // Verifica que el asunto se haya establecido correctamente
        assertEquals(contenido, newsletter.getContenido()); // Verifica que el contenido se haya establecido correctamente
        assertNull(newsletter.getFechaCreacion()); // Verifica que la fecha de creación sea nula (aún no persiste en la base de datos)
    }

    @Test
    @DisplayName("Newsletter - Getters y Setters")
    public void testGettersYSetters() {
        Newsletter newsletter = new Newsletter();
        String asunto = "Actualización de la cuenta";
        String contenido = "Hemos actualizado los términos de servicio.";

        newsletter.setAsunto(asunto);
        newsletter.setContenido(contenido);
        newsletter.setFechaCreacion(LocalDateTime.now());

        assertEquals(null, newsletter.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertEquals(asunto, newsletter.getAsunto()); // Verifica que el asunto se haya establecido correctamente
        assertEquals(contenido, newsletter.getContenido()); // Verifica que el contenido se haya establecido correctamente
        assertNotNull(newsletter.getFechaCreacion()); // Verifica que la fecha de creación se haya establecido correctamente
    }
}
