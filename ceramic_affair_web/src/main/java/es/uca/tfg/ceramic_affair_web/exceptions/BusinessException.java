package es.uca.tfg.ceramic_affair_web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Clase base para manejar excepciones de negocio en la aplicación.
 * Extiende RuntimeException para permitir excepciones no verificadas.
 * 
 * @version 1.0
 */
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
