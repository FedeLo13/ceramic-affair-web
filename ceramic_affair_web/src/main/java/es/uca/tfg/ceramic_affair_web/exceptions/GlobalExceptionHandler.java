package es.uca.tfg.ceramic_affair_web.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Clase para manejar excepciones globales en la aplicación.
 * Utiliza @RestControllerAdvice para manejar excepciones de forma centralizada.
 * 
 * @version 1.1
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

    /**
     * Maneja la excepción de imagen no encontrada.
     * @param ex
     * @return
     */
    @ExceptionHandler(ImagenException.NoEncontrada.class)
    public ResponseEntity<String> handleImagenNoEncontrada(ImagenException.NoEncontrada ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ex.getMessage());
    }

    /**
     * Maneja la excepción de imagen no válida.
     * @param ex la excepción lanzada
     * @return una respuesta con el mensaje de error y el estado HTTP 400 (Bad Request)
     */
    @ExceptionHandler(ImagenException.NoValida.class)
    public ResponseEntity<String> handleImagenNoValida(ImagenException.NoValida ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.getMessage());
    }

    /**
     * Maneja la excepción de límite de tamaño de archivo excedido.
     * @param ex la excepción lanzada
     * @return una respuesta con el mensaje de error y el estado HTTP 413 (Payload Too Large)
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        return ResponseEntity
            .status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body("El archivo es demasiado grande. Por favor, suba un archivo más pequeño.");
    }

    /**
     * Maneja las excepciones de respuesta de estado.
     * @param ex la excepción lanzada
     * @return una respuesta con el mensaje de error y el estado HTTP correspondiente
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity
            .status(ex.getStatusCode())
            .body(ex.getReason());
    }

    /**
     * Maneja las excepciones relacionadas con las validaciones
     * 
     * @param ex la excepción lanzada
     * @return una respuesta con un mapa de errores de validación y el estado HTTP 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errors);
    }
}   
