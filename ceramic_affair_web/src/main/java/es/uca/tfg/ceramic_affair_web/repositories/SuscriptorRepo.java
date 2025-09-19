package es.uca.tfg.ceramic_affair_web.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.tfg.ceramic_affair_web.entities.Suscriptor;

/**
 * Repositorio para la entidad Suscriptor.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos.
 * 
 * @version 1.0
 */
@Repository
public interface SuscriptorRepo extends JpaRepository<Suscriptor, Long> {
    
    /**
     * Busca un suscriptor por su token de verificación.
     * 
     * @param tokenVerificacion El token de verificación del suscriptor.
     * @return Un Optional que contiene el suscriptor si se encuentra, o vacío si no existe.
     */
    Optional<Suscriptor> findByTokenVerificacion(String tokenVerificacion);

    /**
     * Busca un suscriptor por su token de desuscripción.
     * 
     * @param tokenDesuscripcion El token de desuscripción del suscriptor.
     * @return Un Optional que contiene el suscriptor si se encuentra, o vacío si no existe.
     */
    Optional<Suscriptor> findByTokenDesuscripcion(String tokenDesuscripcion);

    /**
     * Busca un suscriptor por su correo electrónico.
     * 
     * @param email El correo electrónico del suscriptor.
     * @return Un Optional que contiene el suscriptor si se encuentra, o vacío si no existe.
     */
    Optional<Suscriptor> findByEmail(String email);

    /**
     * Busca los suscriptores que están verificados.
     * 
     * @return Una lista de suscriptores verificados.
     */
    List<Suscriptor> findByVerificadoTrue();

    /**
     * Elimina los suscriptores no verificados que han expirado.
     * 
     * @param fecha la fecha actual para comparar con la fecha de expiración del token.
     */
    void deleteByVerificadoFalseAndFechaExpiracionTokenBefore(LocalDateTime fecha);
}
