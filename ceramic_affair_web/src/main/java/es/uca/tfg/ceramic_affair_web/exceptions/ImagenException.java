package es.uca.tfg.ceramic_affair_web.exceptions;

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
    public static class NoEncontrada extends RuntimeException {
        /**
         * Constructor de la excepción.
         * @param id el id de la imagen no encontrada
         */
        public NoEncontrada(Long id) {
            super("Imagen no encontrada con id: " + id);
        }

        /**
         * Constructor overloaded para mensajes personalizados.
         * @param mensaje mensaje personalizado
         */
        public NoEncontrada(String mensaje) {
            super(mensaje);
        }
    }

    /**
     * Excepción lanzada cuando una imagen no es válida o no se puede procesar.
     */
    public static class NoValida extends RuntimeException {
        /**
         * Constructor de la excepción.
         * @param mensaje mensaje de error
         */
        public NoValida(String mensaje) {
            super(mensaje);
        }
    }
}
