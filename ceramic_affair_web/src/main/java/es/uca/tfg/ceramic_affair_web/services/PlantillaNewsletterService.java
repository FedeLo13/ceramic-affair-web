package es.uca.tfg.ceramic_affair_web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.tfg.ceramic_affair_web.DTOs.NewsletterDTO;
import es.uca.tfg.ceramic_affair_web.entities.PlantillaNewsletter;
import es.uca.tfg.ceramic_affair_web.repositories.PlantillaNewsletterRepo;
import jakarta.transaction.Transactional;

/**
 * Servicio para la entidad PlantillaNewsletter.
 * 
 * @version 1.0
 */
@Service
public class PlantillaNewsletterService {

    @Autowired
    private PlantillaNewsletterRepo plantillaNewsletterRepo;

    /**
     * Método para obtener la única plantilla de newsletter.
     * Si no existe, se crea una plantilla por defecto con asunto y contenido vacíos.
     * 
     * @return la plantilla de newsletter
     */
    private PlantillaNewsletter obtenerEntidadPlantillaNewsletter() {
        return plantillaNewsletterRepo.findById(1L)
                .orElseGet(() -> {
                    PlantillaNewsletter plantilla = new PlantillaNewsletter("", "");
                    return plantillaNewsletterRepo.save(plantilla);
                });
    }

    /**
     * Método para obtener la plantilla de newsletter como un DTO.
     * 
     * @return un DTO con el asunto y contenido de la plantilla
     */
    public NewsletterDTO obtenerPlantillaNewsletter() {
        PlantillaNewsletter plantilla = obtenerEntidadPlantillaNewsletter();
        return new NewsletterDTO(plantilla.getAsunto(), plantilla.getContenido());
    }

    /**
     * Método para modificar/guardar la plantilla de newsletter.
     */
    @Transactional
    public void modificarPlantillaNewsletter(NewsletterDTO newsletterDTO) {
        PlantillaNewsletter plantilla = plantillaNewsletterRepo.findById(1L)
                .orElse(new PlantillaNewsletter());

        plantilla.setAsunto(newsletterDTO.getAsunto());
        plantilla.setContenido(newsletterDTO.getMensaje());

        plantillaNewsletterRepo.save(plantilla);
    }
}
