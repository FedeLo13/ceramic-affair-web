package es.uca.tfg.ceramic_affair_web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Clase para manejar excepciones relacionadas con la entidad Producto.
 */
public class ProductoException {

    /**
     * Constructor privado para evitar la instanciación de esta clase.
     */
    private ProductoException() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    /**
     * Excepción lanzada cuando no se encuentra un producto.
     */
    public static class NoEncontrado extends BusinessException {
        /**
         * Constructor de la excepción.
         * @param id
         */
        public NoEncontrado(Long id) {
            super("Product not found with ID " + id, HttpStatus.NOT_FOUND);
        }

        /**
         * Constructor overloaded para mensajes personalizados.
         * @param mensaje
         */
        public NoEncontrado(String mensaje) {
            super(mensaje, HttpStatus.NOT_FOUND);
        }
    }
}
