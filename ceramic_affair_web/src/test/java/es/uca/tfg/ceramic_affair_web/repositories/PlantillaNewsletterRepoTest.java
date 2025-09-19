package es.uca.tfg.ceramic_affair_web.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.uca.tfg.ceramic_affair_web.entities.PlantillaNewsletter;

/**
 * Clase de prueba para el repositorio PlantillaNewsletterRepo.
 * Proporciona pruebas de integración para las operaciones CRUD en la entidad PlantillaNewsletter.
 * 
 * @version 1.0
 */
@DataJpaTest
public class PlantillaNewsletterRepoTest {

    @Autowired
    private PlantillaNewsletterRepo plantillaNewsletterRepo;

    @Test
    @DisplayName("Repositorio - Guardar y cargar plantilla de newsletter")
    void testGuardarYCargarPlantillaNewsletter() {
        // Crear y guardar una nueva plantilla de newsletter
        PlantillaNewsletter plantilla = new PlantillaNewsletter("Plantilla 1", "Contenido de la plantilla 1");
        plantillaNewsletterRepo.save(plantilla);

        // Cargar la plantilla de newsletter por ID
        PlantillaNewsletter plantillaCargada = plantillaNewsletterRepo.findById(plantilla.getId()).orElse(null);
        assertNotNull(plantillaCargada);
        assertEquals(plantilla.getAsunto(), plantillaCargada.getAsunto());
        assertEquals(plantilla.getContenido(), plantillaCargada.getContenido());
    }

    @Test
    @DisplayName("Repositorio - Eliminar plantilla de newsletter")
    void testEliminarPlantillaNewsletter() {
        // Crear y guardar una nueva plantilla de newsletter
        PlantillaNewsletter plantilla = new PlantillaNewsletter("Plantilla 2", "Contenido de la plantilla 2");
        plantillaNewsletterRepo.save(plantilla);

        // Eliminar la plantilla de newsletter
        plantillaNewsletterRepo.delete(plantilla);

        // Verificar que la plantilla ya no esté presente en la base de datos
        PlantillaNewsletter plantillaEliminada = plantillaNewsletterRepo.findById(plantilla.getId()).orElse(null);
        assertNull(plantillaEliminada);
    }

    @Test
    @DisplayName("Repositorio - Unicidad de plantilla de newsletter")
    void testUnicidadPlantillaNewsletter() {
        // Crear y guardar una nueva plantilla de newsletter
        PlantillaNewsletter plantilla1 = new PlantillaNewsletter("Plantilla 3", "Contenido de la plantilla 3");
        plantillaNewsletterRepo.save(plantilla1);

        // Intentar guardar otra plantilla con el mismo nombre
        PlantillaNewsletter plantillaDuplicada = new PlantillaNewsletter("Plantilla 4", "Contenido de la plantilla 4");
        plantillaNewsletterRepo.save(plantillaDuplicada);

        // Verificar que no se haya creado una nueva plantilla y que los datos son los de la última guardada
        long count = plantillaNewsletterRepo.count();
        assertEquals(1, count);
        PlantillaNewsletter plantillaCargada = plantillaNewsletterRepo.findById(plantilla1.getId()).orElse(null);
        assertNotNull(plantillaCargada);
        assertEquals(plantillaDuplicada.getAsunto(), plantillaCargada.getAsunto());
        assertEquals(plantillaDuplicada.getContenido(), plantillaCargada.getContenido());
    }
}
