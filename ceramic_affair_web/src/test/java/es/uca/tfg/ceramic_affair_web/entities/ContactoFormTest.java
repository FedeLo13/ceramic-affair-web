package es.uca.tfg.ceramic_affair_web.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Clase de prueba para la entidad ContactoForm.
 * Proporciona pruebas unitarias para los métodos de la clase ContactoForm.
 * 
 * @version 1.0
 */
public class ContactoFormTest {

    @Test
    @DisplayName("ContactoForm - Constructor vacío")
    public void testConstructorVacio() {
        ContactoForm contactoForm = new ContactoForm();

        assertNotNull(contactoForm); // Verifica que la instancia no sea nula
        assertNull(contactoForm.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertNull(contactoForm.getNombre()); // Verifica que el nombre sea nulo (no se ha establecido)
        assertNull(contactoForm.getApellidos()); // Verifica que los apellidos sean nulos (no se han establecido)
        assertNull(contactoForm.getEmail()); // Verifica que el email sea nulo (no se ha establecido)
        assertNull(contactoForm.getAsunto()); // Verifica que el asunto sea nulo (no se ha establecido)
        assertNull(contactoForm.getMensaje()); // Verifica que el mensaje sea nulo (no se ha establecido)
        assertNull(contactoForm.getFechaCreacion()); // Verifica que la fecha de creación sea nula (aún no persiste en la base de datos)
    }

    @Test
    @DisplayName("ContactoForm - Constructor con parámetros")
    public void testConstructorConParametros() {
        String nombre = "Juan";
        String apellidos = "Pérez";
        String email = "juan.perez@example.com";
        String asunto = "Consulta";
        String mensaje = "Tengo una pregunta sobre su producto.";
        ContactoForm contactoForm = new ContactoForm(nombre, apellidos, email, asunto, mensaje);

        assertNotNull(contactoForm); // Verifica que la instancia no sea nula
        assertNull(contactoForm.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertEquals(nombre, contactoForm.getNombre()); // Verifica que el nombre se haya establecido correctamente
        assertEquals(apellidos, contactoForm.getApellidos()); // Verifica que los apellidos se hayan establecido correctamente
        assertEquals(email, contactoForm.getEmail()); // Verifica que el email se haya establecido correctamente
        assertEquals(asunto, contactoForm.getAsunto()); // Verifica que el asunto se haya establecido correctamente
        assertEquals(mensaje, contactoForm.getMensaje()); // Verifica que el mensaje se haya establecido correctamente
        assertNull(contactoForm.getFechaCreacion()); // Verifica que la fecha de creación sea nula (aún no persiste en la base de datos)
    }

    @Test
    @DisplayName("ContactoForm - PrePersist establece fecha de creación")
    public void testPrePersistEstableceFechaCreacion() {
        ContactoForm contactoForm = new ContactoForm();
        contactoForm.prePersist();

        assertNotNull(contactoForm.getFechaCreacion()); // Verifica que la fecha de creación no sea nula
    }

    @Test
    @DisplayName("ContactoForm - Getters y Setters")
    public void testGettersYSetters() {
        ContactoForm contactoForm = new ContactoForm();
        Long id = 1L;
        String nombre = "Ana";
        String apellidos = "Gómez";
        String email = "ana.gomez@example.com";
        String asunto = "Consulta";
        String mensaje = "Tengo una pregunta sobre su producto.";

        contactoForm.setId(id);
        contactoForm.setNombre(nombre);
        contactoForm.setApellidos(apellidos);
        contactoForm.setEmail(email);
        contactoForm.setAsunto(asunto);
        contactoForm.setMensaje(mensaje);

        assertEquals(id, contactoForm.getId());
        assertEquals(nombre, contactoForm.getNombre());
        assertEquals(apellidos, contactoForm.getApellidos());
        assertEquals(email, contactoForm.getEmail());
        assertEquals(asunto, contactoForm.getAsunto());
        assertEquals(mensaje, contactoForm.getMensaje());
    }
}
