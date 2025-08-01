package es.uca.tfg.ceramic_affair_web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Clase para manejar excepciones relacionadas con la entidad Imagen.
 */
public class ImagenException {

    /**
     * Constructor privado para evitar la instanciación de esta clase.
     */
    private ImagenException() {
        throw new UnsupportedOperationException("Esta clase no debe ser instanciada.");
    }

    /**
     * Excepción lanzada cuando no se encuentra una imagen.
     */
    public static class NoEncontrada extends BusinessException {
        /**
         * Constructor de la excepción.
         * @param id el id de la imagen no encontrada
         */
        public NoEncontrada(Long id) {
            super("Imagen no encontrada con ID: " + id, HttpStatus.NOT_FOUND);
        }

        /**
         * Constructor overloaded para mensajes personalizados.
         * @param mensaje mensaje personalizado
         */
        public NoEncontrada(String mensaje) {
            super(mensaje, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Excepción lanzada cuando una imagen no es válida o no se puede procesar.
     */
    public static class NoValida extends BusinessException {
        /**
         * Constructor de la excepción.
         * @param mensaje mensaje de error
         */
        public NoValida(String mensaje) {
            super(mensaje, HttpStatus.BAD_REQUEST);
        }
    }
}
