package es.uca.tfg.ceramic_affair_web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.tfg.ceramic_affair_web.entities.ContactoForm;

/**
 * Repositorio para la entidad ContactoForm.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos.
 * 
 * @version 1.0
 */
@Repository
public interface ContactoFormRepo extends JpaRepository<ContactoForm, Long> {
    // No se necesitan métodos adicionales por ahora, ya que JpaRepository proporciona
    // todos los métodos CRUD necesarios.
}
