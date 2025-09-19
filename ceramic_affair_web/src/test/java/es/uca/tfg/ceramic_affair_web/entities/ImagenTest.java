package es.uca.tfg.ceramic_affair_web.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Clase de prueba para la entidad Imagen.
 * Proporciona pruebas unitarias para los métodos de la clase Imagen.
 * 
 * @version 1.0
 */
public class ImagenTest {

    @Test
    @DisplayName("Imagen - Constructor vacío")
    public void testConstructorVacio() {
        Imagen imagen = new Imagen();

        assertNotNull(imagen); // Verifica que la instancia no sea nula
        assertNull(imagen.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertNull(imagen.getRuta()); // Verifica que la ruta sea nula (no se ha establecido)
        assertNull(imagen.getFormato()); // Verifica que el formato sea nulo (no se ha establecido)
        assertEquals(0.0f, imagen.getTamano()); // Verifica que el tamaño sea 0.0 por defecto
        assertEquals(0.0f, imagen.getAncho()); // Verifica que el ancho sea 0.0 por defecto
        assertEquals(0.0f, imagen.getAlto()); // Verifica que el alto sea 0.0 por defecto
    }

    @Test
    @DisplayName("Imagen - Constructor con parámetros")
    public void testConstructorConParametros() {
        String ruta = "ruta/a/la/imagen.jpg";
        String formato = "jpg";
        float tamano = 1024.0f; // Tamaño en bytes
        float ancho = 800.0f; // Ancho en píxeles
        float alto = 600.0f; // Alto en píxeles

        Imagen imagen = new Imagen(ruta, formato, tamano, ancho, alto);

        assertNotNull(imagen); // Verifica que la instancia no sea nula
        assertNull(imagen.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertEquals(ruta, imagen.getRuta()); // Verifica que la ruta sea la esperada
        assertEquals(formato, imagen.getFormato()); // Verifica que el formato sea el esperado
        assertEquals(tamano, imagen.getTamano()); // Verifica que el tamaño sea el esperado
        assertEquals(ancho, imagen.getAncho()); // Verifica que el ancho sea el esperado
        assertEquals(alto, imagen.getAlto()); // Verifica que el alto sea el esperado
    }

    @Test
    @DisplayName("Imagen - Getters")
    public void testGetters() {
        Imagen imagen = new Imagen("ruta/a/la/imagen.jpg", "jpg", 1024.0f, 800.0f, 600.0f);

        assertNotNull(imagen); // Verifica que la instancia no sea nula
        assertNull(imagen.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertEquals("ruta/a/la/imagen.jpg", imagen.getRuta()); // Verifica que la ruta sea la esperada
        assertEquals("jpg", imagen.getFormato()); // Verifica que el formato sea el esperado
        assertEquals(1024.0f, imagen.getTamano()); // Verifica que el tamaño sea el esperado
        assertEquals(800.0f, imagen.getAncho()); // Verifica que el ancho sea el esperado
        assertEquals(600.0f, imagen.getAlto()); // Verifica que el alto sea el esperado
    }
}
