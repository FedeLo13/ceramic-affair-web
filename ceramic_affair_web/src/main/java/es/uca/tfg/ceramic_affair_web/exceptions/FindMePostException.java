package es.uca.tfg.ceramic_affair_web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Clase para manejar excepciones relacionadas con la entidad FindMePost.
 */
public class FindMePostException {

    /**
     * Constructor privado para evitar la instanciación de esta clase.
     */
    private FindMePostException() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    /**
     * Excepción lanzada cuando no se encuentra una publicación "Encuéntrame".
     */
    public static class NoEncontrado extends BusinessException {
        /**
         * Constructor de la excepción.
         * @param id el id de la publicación no encontrada
         */
        public NoEncontrado(Long id) {
            super("Find Me Post not found with ID " + id, HttpStatus.NOT_FOUND);
        }

        /**
         * Constructor sobrecargado para mensajes personalizados.
         * @param mensaje mensaje personalizado de la excepción
         */
        public NoEncontrado(String mensaje) {
            super(mensaje, HttpStatus.NOT_FOUND);
        }
    }
}
