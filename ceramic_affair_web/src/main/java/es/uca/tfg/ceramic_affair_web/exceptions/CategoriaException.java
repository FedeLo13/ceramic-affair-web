package es.uca.tfg.ceramic_affair_web.exceptions;

/**
 * Clase para manejar excepciones relacionadas con la entidad Categoria.
 */
public class CategoriaException {

    /**
     * Constructor privado para evitar la instanciación de esta clase.
     */
    private CategoriaException() {
        throw new UnsupportedOperationException("Esta clase no debe ser instanciada.");
    }

    /**
     * Excepción lanzada cuando no se encuentra una categoría.
     */
    public static class NoEncontrada extends RuntimeException {
        /**
         * Constructor de la excepción.
         * @param id
         */
        public NoEncontrada(Long id) {
            super("Categoría no encontrada con id: " + id);
        }

        /**
         * Constructor overloaded para mensajes personalizados.
         * @param mensaje
         */
        public NoEncontrada(String mensaje) {
            super(mensaje);
        }
    }
}
