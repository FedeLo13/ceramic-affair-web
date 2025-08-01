package es.uca.tfg.ceramic_affair_web.exceptions;

import org.springframework.http.HttpStatus;

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
    public static class NoEncontrada extends BusinessException {
        /**
         * Constructor de la excepción.
         * @param id
         */
        public NoEncontrada(Long id) {
            super("Categoría no encontrada con ID: " + id, HttpStatus.NOT_FOUND);
        }

        /**
         * Constructor overloaded para mensajes personalizados.
         * @param mensaje
         */
        public NoEncontrada(String mensaje) {
            super(mensaje, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Excepción lanzada cuando ya existe una categoría con el mismo nombre.
     */
    public static class YaExistente extends BusinessException {
        /**
         * Constructor de la excepción.
         * @param nombre
         */
        public YaExistente(String nombre) {
            super("Ya existe una categoría con el nombre: " + nombre, HttpStatus.CONFLICT);
        }
    }
}
