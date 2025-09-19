package es.uca.tfg.ceramic_affair_web.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.tfg.ceramic_affair_web.entities.Usuario;

/**
 * Repositorio para la entidad Usuario.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos.
 * 
 * @version 1.0
 */
public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
    /**
     * Verifica si existe un usuario con el correo electrónico dado.
     * 
     * @param email El correo electrónico del usuario a verificar.
     * @return true si existe un usuario con el correo electrónico, false en caso contrario.
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca un usuario por su correo electrónico.
     * 
     * @param email El correo electrónico del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no existe.
     */
    Optional<Usuario> findByEmail(String email);
}
