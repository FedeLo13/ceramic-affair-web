package es.uca.tfg.ceramic_affair_web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import es.uca.tfg.ceramic_affair_web.entities.Producto;


/**
 * Repositorio para la entidad Producto.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos.
 * 
 * @version 1.0
 */
@Repository
public interface ProductoRepo extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
    // No se necesitan métodos adicionales por ahora, ya que JpaRepository proporciona
    // todos los métodos CRUD necesarios.
}
