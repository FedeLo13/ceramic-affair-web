package es.uca.tfg.ceramic_affair_web.repositories;

import es.uca.tfg.ceramic_affair_web.entities.ContactoForm;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Clase de prueba para el repositorio ContactoFormRepo.
 * Proporciona pruebas de integración para las operaciones CRUD en la entidad ContactoForm.
 * 
 * @version 1.0
 */
@DataJpaTest
public class ContactoFormRepoTest {

    @Autowired
    private ContactoFormRepo contactoFormRepo;

    @Test
    @DisplayName("Repositorio - Guardar y cargar formulario de contacto")
    void testGuardarYCargarFormularioContacto() {
        // Crear y guardar un nuevo formulario de contacto
        ContactoForm contactoForm = new ContactoForm("Juan", "Pérez", "juan.perez@example.com", "Consulta", "Tengo una pregunta sobre su producto.");
        contactoFormRepo.save(contactoForm);

        // Cargar el formulario de contacto por ID
        ContactoForm formularioCargado = contactoFormRepo.findById(contactoForm.getId()).orElse(null);

        // Verificar que el formulario cargado no sea nulo y que sus datos sean correctos
        assertNotNull(formularioCargado);
        assertEquals(contactoForm.getNombre(), formularioCargado.getNombre());
        assertEquals(contactoForm.getApellidos(), formularioCargado.getApellidos());
        assertEquals(contactoForm.getEmail(), formularioCargado.getEmail());
        assertEquals(contactoForm.getAsunto(), formularioCargado.getAsunto());
        assertEquals(contactoForm.getMensaje(), formularioCargado.getMensaje());
    }

    @Test
    @DisplayName("Repositorio - Eliminar formulario de contacto")
    void testEliminarFormularioContacto() {
        // Crear y guardar un nuevo formulario de contacto
        ContactoForm contactoForm = new ContactoForm("Ana", "Gómez", "ana.gomez@example.com", "Consulta", "Tengo una pregunta sobre su producto.");
        contactoFormRepo.save(contactoForm);

        // Eliminar el formulario de contacto
        contactoFormRepo.delete(contactoForm);

        // Verificar que el formulario de contacto ya no esté presente en la base de datos
        ContactoForm formularioEliminado = contactoFormRepo.findById(contactoForm.getId()).orElse(null);
        assertNull(formularioEliminado);
    }
}
