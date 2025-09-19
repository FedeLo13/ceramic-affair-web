package es.uca.tfg.ceramic_affair_web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.tfg.ceramic_affair_web.entities.Newsletter;

/**
 * Repositorio para la entidad Newsletter.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos.
 * 
 * @version 1.0
 */
@Repository
public interface NewsletterRepo extends JpaRepository<Newsletter, Long> {
    // No se necesitan métodos adicionales por ahora, JpaRepository ya proporciona los necesarios
    // como save, findById, findAll, deleteById, etc.
}
