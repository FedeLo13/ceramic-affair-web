package es.uca.tfg.ceramic_affair_web.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Clase de prueba para la entidad Suscriptor.
 * Proporciona pruebas unitarias para los métodos de la clase Suscriptor.
 * 
 * @version 1.0
 */
public class SuscriptorTest {

    @Test
    @DisplayName("Suscriptor - Constructor vacío")
    public void testConstructorVacio() {
        Suscriptor suscriptor = new Suscriptor();

        assertNotNull(suscriptor); // Verifica que la instancia no sea nula
        assertNull(suscriptor.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertNull(suscriptor.getEmail()); // Verifica que el email sea nulo (no se ha establecido)
        assertFalse(suscriptor.isVerificado()); // Verifica que verificado sea false por defecto
        assertNull(suscriptor.getTokenVerificacion()); // Verifica que el token de verificación sea nulo (no se ha generado)
        assertNull(suscriptor.getTokenDesuscripcion()); // Verifica que el token de desuscripción sea nulo (no se ha generado)
        assertNull(suscriptor.getFechaCreacionToken()); // Verifica que la fecha de creación del token sea nula
        assertNull(suscriptor.getFechaExpiracionToken()); // Verifica que la fecha de expiración del token sea nula
    }

    @Test
    @DisplayName("Suscriptor - Constructor con parámetros")
    public void testConstructorConParametros() {
        String email = "test@example.com";
        Suscriptor suscriptor = new Suscriptor(email);

        assertNotNull(suscriptor); // Verifica que la instancia no sea nula
        assertNull(suscriptor.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertEquals(email, suscriptor.getEmail()); // Verifica que el email se haya establecido correctamente
        assertFalse(suscriptor.isVerificado()); // Verifica que verificado sea false por defecto
        assertNotNull(suscriptor.getTokenVerificacion()); // Verifica que se haya generado un token de verificación
        assertNull(suscriptor.getTokenDesuscripcion()); // Verifica que el token de desuscripción sea nulo (no se ha generado)
        assertNotNull(suscriptor.getFechaCreacionToken()); // Verifica que la fecha de creación del token no sea nula
        assertNotNull(suscriptor.getFechaExpiracionToken()); // Verifica que la fecha de expiración del token no sea nula
    }

    @Test
    @DisplayName("Suscriptor - Getters y Setters")
    public void testGettersYSetters() {
        Suscriptor suscriptor = new Suscriptor();
        String email = "test@example.com";
        suscriptor.setEmail(email);
        suscriptor.verificar();
        suscriptor.regenerarTokenVerificacion();

        assertEquals(email, suscriptor.getEmail());
        assertTrue(suscriptor.isVerificado());
        assertNotNull(suscriptor.getTokenVerificacion());
        assertNotNull(suscriptor.getTokenDesuscripcion());
        assertNotNull(suscriptor.getFechaCreacionToken());
        assertNotNull(suscriptor.getFechaExpiracionToken());
    }
}