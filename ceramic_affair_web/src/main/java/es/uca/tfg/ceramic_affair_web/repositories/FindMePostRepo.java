package es.uca.tfg.ceramic_affair_web.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.tfg.ceramic_affair_web.entities.FindMePost;

/**
 * Repositorio para la entidad FindMePost.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos.
 * 
 * @version 1.0
 */
@Repository
public interface FindMePostRepo extends JpaRepository<FindMePost, Long> {
    /**
     * Busca todas las publicaciones "Encuéntrame" ordenadas por fecha de inicio de forma descendente.
     *
     * @return una lista de publicaciones "Encuéntrame" ordenadas por fecha de inicio
     */
    List<FindMePost> findAllByOrderByFechaInicioDesc();
}
