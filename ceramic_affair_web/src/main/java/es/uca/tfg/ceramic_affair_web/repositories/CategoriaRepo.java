package es.uca.tfg.ceramic_affair_web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.tfg.ceramic_affair_web.entities.Categoria;

/**
 * Repositorio para la entidad Categoria.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos.
 * 
 * @version 1.0
 */
@Repository
public interface CategoriaRepo extends JpaRepository<Categoria, Long> {

    boolean existsByNombre(String nombre); // Verifica si existe una categoría con el nombre dado
    
}
