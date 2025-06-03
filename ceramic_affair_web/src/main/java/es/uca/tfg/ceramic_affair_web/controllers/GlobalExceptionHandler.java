package es.uca.tfg.ceramic_affair_web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import es.uca.tfg.ceramic_affair_web.exceptions.CategoriaException;
import es.uca.tfg.ceramic_affair_web.exceptions.ProductoException;

/**
 * Clase para manejar excepciones globales en la aplicación.
 * Utiliza @RestControllerAdvice para manejar excepciones de forma centralizada.
 * 
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja cualquier excepción no controlada en la aplicación.
     * @param ex
     * @return una respuesta con el mensaje de error y el estado HTTP 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error interno del servidor. Por favor, inténtelo de nuevo más tarde o contacte con el soporte técnico.");
    }

    /**
     * Maneja la excepción de categoría no encontrada.
     * 
     * @param ex la excepción lanzada
     * @return una respuesta con el mensaje de error y el estado HTTP 404 (Not Found)
     */
    @ExceptionHandler(CategoriaException.NoEncontrada.class)
    public ResponseEntity<String> handleCategoriaNoEncontrada(CategoriaException.NoEncontrada ex) {
        return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
    }

    /**
     * Maneja la excepción de categoría ya existente.
     * 
     * @param ex la excepción lanzada
     * @return una respuesta con el mensaje de error y el estado HTTP 409 (Conflict)
     */
    @ExceptionHandler(CategoriaException.YaExistente.class)
    public ResponseEntity<String> handleCategoriaYaExistente(CategoriaException.YaExistente ex) {
        return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ex.getMessage());
    }

    /**
     * Maneja la excepción de producto no encontrado.
     * 
     * @param ex la excepción lanzada
     * @return una respuesta con el mensaje de error y el estado HTTP 404 (Not Found)
     */
    @ExceptionHandler(ProductoException.NoEncontrado.class)
    public ResponseEntity<String> handleProductoNoEncontrado(ProductoException.NoEncontrado ex) {
        return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
    }
}
