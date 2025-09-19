package es.uca.tfg.ceramic_affair_web.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Clase de prueba para la entidad FindMePost.
 * Proporciona pruebas unitarias para los métodos de la clase FindMePost.
 * 
 * @version 1.0
 */
public class FindMePostTest {

    @Test
    @DisplayName("FindMePost - Constructor vacío")
    public void testConstructorVacio() {
        FindMePost findMePost = new FindMePost();

        assertNotNull(findMePost); // Verifica que la instancia no sea nula
        assertNull(findMePost.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertNull(findMePost.getTitulo()); // Verifica que el título sea nulo (no se ha establecido)
        assertNull(findMePost.getDescripcion()); // Verifica que la descripción sea nula (no se ha establecido)
        assertNull(findMePost.getFechaInicio()); // Verifica que la fecha de inicio sea nula (no se ha establecido)
        assertNull(findMePost.getFechaFin()); // Verifica que la fecha de fin sea nula (no se ha establecido)
        assertNull(findMePost.getLatitud()); // Verifica que la latitud sea nula (no se ha establecido)
        assertNull(findMePost.getLongitud()); // Verifica que la longitud sea nula (no se ha establecido)
    }

    @Test
    @DisplayName("FindMePost - Constructor con parámetros")
    public void testConstructorConParametros() {
        String titulo = "Encuéntrame";
        String descripcion = "Descripción de la publicación";
        LocalDateTime fechaInicio = LocalDateTime.now();
        LocalDateTime fechaFin = fechaInicio.plusDays(1);
        Double latitud = 36.5;
        Double longitud = -6.3;

        FindMePost findMePost = new FindMePost(titulo, descripcion, fechaInicio, fechaFin, latitud, longitud);

        assertNotNull(findMePost); // Verifica que la instancia no sea nula
        assertNull(findMePost.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertEquals(titulo, findMePost.getTitulo()); // Verifica que el título sea el esperado
        assertEquals(descripcion, findMePost.getDescripcion()); // Verifica que la descripción sea la esperada
        assertEquals(fechaInicio, findMePost.getFechaInicio()); // Verifica que la fecha de inicio sea la esperada
        assertEquals(fechaFin, findMePost.getFechaFin()); // Verifica que la fecha de fin sea la esperada
        assertEquals(latitud, findMePost.getLatitud()); // Verifica que la latitud sea la esperada
        assertEquals(longitud, findMePost.getLongitud()); // Verifica que la longitud sea la esperada
    }

    @Test
    @DisplayName("FindMePost - Setters y Getters")
    public void testSettersYGetters() {
        FindMePost findMePost = new FindMePost();

        String titulo = "Encuéntrame";
        String descripcion = "Descripción de la publicación";
        LocalDateTime fechaInicio = LocalDateTime.now();
        LocalDateTime fechaFin = fechaInicio.plusDays(1);
        Double latitud = 36.5;
        Double longitud = -6.3;

        findMePost.setTitulo(titulo);
        findMePost.setDescripcion(descripcion);
        findMePost.setFechaInicio(fechaInicio);
        findMePost.setFechaFin(fechaFin);
        findMePost.setLatitud(latitud);
        findMePost.setLongitud(longitud);

        assertEquals(titulo, findMePost.getTitulo());
        assertEquals(descripcion, findMePost.getDescripcion());
        assertEquals(fechaInicio, findMePost.getFechaInicio());
        assertEquals(fechaFin, findMePost.getFechaFin());
        assertEquals(latitud, findMePost.getLatitud());
        assertEquals(longitud, findMePost.getLongitud());
    }
}
