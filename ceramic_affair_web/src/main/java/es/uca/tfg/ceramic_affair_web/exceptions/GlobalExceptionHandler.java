package es.uca.tfg.ceramic_affair_web.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

import es.uca.tfg.ceramic_affair_web.payload.ApiError;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Clase para manejar excepciones globales en la aplicación.
 * Utiliza @RestControllerAdvice para manejar excepciones de forma centralizada.
 * 
 * @version 1.1
 */
@RestControllerAdvice
@Order(2)
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());

    /**
     * Maneja cualquier excepción no controlada en la aplicación.
     * @param ex
     * @return una respuesta con el mensaje de error y el estado HTTP 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(Exception ex, HttpServletRequest request) {
        logger.error("Internal server error: ", ex);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal server error",
            "Please try again later or contact technical support.",
            request.getRequestURI(),
            null);
    }

    /**
     * Maneja todo tipo de excepción de negocio.
     * 
     * @param ex la excepción lanzada
     * @return una respuesta con el mensaje de error específico y el estado HTTP correspondiente
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        logger.warn("Business exception: {}", ex.getMessage());
        return createErrorResponse(ex.getStatus(),
            "Business exception",
            ex.getMessage(),
            request.getRequestURI(),
            null);
    }

    /**
     * Maneja la excepción de límite de tamaño de archivo excedido.
     * @param ex la excepción lanzada
     * @return una respuesta con el mensaje de error y el estado HTTP 413 (Payload Too Large)
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        logger.warn("File size exceeded: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE,
            "File size exceeded",
            ex.getMessage(),
            request.getRequestURI(),
            null);
    }

    /**
     * Maneja las excepciones de respuesta de estado.
     * @param ex la excepción lanzada
     * @return una respuesta con el mensaje de error y el estado HTTP correspondiente
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        logger.warn("Response status exception: {}", ex.getReason());
        return createErrorResponse(ex.getStatusCode(),
            "Response status exception",
            ex.getReason(),
            request.getRequestURI(),
            null);
    }

    /**
     * Maneja las excepciones de reCAPTCHA inválido.
     * @param ex la excepción lanzada
     * @return una respuesta con el mensaje de error y el estado HTTP 400 (Bad Request)
     */
    @ExceptionHandler(RecaptchaException.Invalido.class)
    public ResponseEntity<ApiError> handleRecaptchaInvalido(RecaptchaException.Invalido ex, HttpServletRequest request) {
        logger.warn("Invalid reCAPTCHA: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST,
            "Invalid reCAPTCHA",
            ex.getMessage(),
            request.getRequestURI(),
            null);
    }

    /**
     * Maneja las excepciones de envío de correo electrónico fallido.
     * @param ex la excepción lanzada
     * @return una respuesta con el mensaje de error y el estado HTTP 500 (Internal Server Error)
     */
    @ExceptionHandler(EmailException.EnvioFallido.class)
    public ResponseEntity<ApiError> handleEmailEnvioFallido(EmailException.EnvioFallido ex, HttpServletRequest request) {
        logger.error("Failed to send email: ", ex);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
            "Failed to send email",
            ex.getMessage(),
            request.getRequestURI(),
            null);
    }

    /**
     * Maneja la excepción de violación de la integridad de la base de datos.
     * @param ex la excepción lanzada
     * @return una respuesta con el mensaje de error y el estado HTTP 409 (Conflict)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        logger.warn("Data integrity violation: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.CONFLICT,
            "Data integrity violation",
            ex.getMessage(),
            request.getRequestURI(),
            null);
    }

    /**
     * Maneja las excepciones relacionadas con las validaciones
     * 
     * @param ex la excepción lanzada
     * @return una respuesta con un mapa de errores de validación y el estado HTTP 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        logger.warn("Validation errors: {}", errors);
        return createErrorResponse(HttpStatus.BAD_REQUEST,
            "Validation errors",
            "Please check the marked fields.",
            request.getRequestURI(),
            errors);
    }

    /**
     * Método helper para crear una respuesta de error genérica.
     * 
     * @param status el estado HTTP de la respuesta
     * @param error el mensaje de error
     * @param message el mensaje detallado del error
     * @param path la ruta de la solicitud que causó el error
     * @param validationErrors un mapa de errores de validación (opcional)
     * @return una instancia de ApiError con los detalles del error
     */
    private ResponseEntity<ApiError> createErrorResponse(HttpStatusCode status, String error, String message, String path, Map<String, String> validationErrors) {
        ApiError apiError = new ApiError(LocalDateTime.now(), status.value(), error, message, path);
        if (validationErrors != null && !validationErrors.isEmpty()) {
            apiError.setValidationErrors(validationErrors);
        }
        return ResponseEntity
            .status(status)
            .body(apiError);
    }
}
