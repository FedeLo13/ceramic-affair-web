package es.uca.tfg.ceramic_affair_web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.tfg.ceramic_affair_web.entities.PlantillaNewsletter;

/**
 * Interfaz del repositorio para gestionar las plantillas de newsletter.
 * Esta interfaz extiende de JpaRepository para proporcionar métodos CRUD básicos.
 * 
 * @version 1.0
 */
public interface PlantillaNewsletterRepo extends JpaRepository<PlantillaNewsletter, Long> {
    // No se necesitan métodos adicionales por ahora, JpaRepository ya proporciona los necesarios
    // como save, findById, findAll, deleteById, etc.
}
