package es.uca.tfg.ceramic_affair_web.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.uca.tfg.ceramic_affair_web.entities.Newsletter;

/**
 * Clase de prueba para el repositorio NewsletterRepo.
 * Actualmente no contiene pruebas, pero se puede extender en el futuro para incluir pruebas de integración
 * 
 * @version 1.0
 */
@DataJpaTest
public class NewsletterRepoTest {

    @Autowired
    private NewsletterRepo newsletterRepo;

    @Test
    @DisplayName("Repositorio - Guardar y cargar newsletter")
    void testGuardarYCargarNewsletter() {
        // Crear y guardar un nuevo newsletter
        Newsletter newsletter = new Newsletter("Asunto 1", "Contenido del newsletter 1");
        newsletterRepo.save(newsletter);

        // Cargar el newsletter por ID
        Newsletter newsletterCargado = newsletterRepo.findById(newsletter.getId()).orElse(null);
        assertNotNull(newsletterCargado);
        assertEquals(newsletter.getAsunto(), newsletterCargado.getAsunto());
        assertEquals(newsletter.getContenido(), newsletterCargado.getContenido());
    }

    @Test
    @DisplayName("Repositorio - Eliminar newsletter")
    void testEliminarNewsletter() {
        // Crear y guardar un nuevo newsletter
        Newsletter newsletter = new Newsletter("Asunto 2", "Contenido del newsletter 2");
        newsletterRepo.save(newsletter);

        // Eliminar el newsletter
        newsletterRepo.delete(newsletter);

        // Verificar que el newsletter ya no esté presente en la base de datos
        Newsletter newsletterEliminado = newsletterRepo.findById(newsletter.getId()).orElse(null);
        assertNull(newsletterEliminado);
    }
}
