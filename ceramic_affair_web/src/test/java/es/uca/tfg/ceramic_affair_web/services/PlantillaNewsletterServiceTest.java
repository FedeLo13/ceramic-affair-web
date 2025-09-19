package es.uca.tfg.ceramic_affair_web.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import es.uca.tfg.ceramic_affair_web.DTOs.NewsletterDTO;

/**
 * Clase de prueba para el servicio PlantillaNewsletterService.
 * Proporciona pruebas de integración para las operaciones CRUD de la plantilla de newsletter.
 * 
 * @version 1.0
 */
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PlantillaNewsletterServiceTest {

    @Autowired
    private PlantillaNewsletterService plantillaNewsletterService;

    @MockitoBean
    private GmailEmailService gmailEmailService;

    @MockitoBean
    private RecaptchaService recaptchaService;

    @Test
    @DisplayName("Servicio - Obtener plantilla de newsletter creada por defecto")
    public void testObtenerPlantillaNewsletterCreadaPorDefecto() {
        NewsletterDTO plantilla = plantillaNewsletterService.obtenerPlantillaNewsletter();
        assertNotNull(plantilla);
        assertEquals("", plantilla.getAsunto());
        assertEquals("", plantilla.getMensaje());
    }

    @Test
    @DisplayName("Servicio - Modificar plantilla de newsletter")
    public void testModificarPlantillaNewsletter() {
        NewsletterDTO plantilla = new NewsletterDTO("Nuevo Asunto", "Nuevo Contenido");
        plantillaNewsletterService.modificarPlantillaNewsletter(plantilla);

        NewsletterDTO plantillaModificada = plantillaNewsletterService.obtenerPlantillaNewsletter();
        assertNotNull(plantillaModificada);
        assertEquals("Nuevo Asunto", plantillaModificada.getAsunto());
        assertEquals("Nuevo Contenido", plantillaModificada.getMensaje());
    }
}
