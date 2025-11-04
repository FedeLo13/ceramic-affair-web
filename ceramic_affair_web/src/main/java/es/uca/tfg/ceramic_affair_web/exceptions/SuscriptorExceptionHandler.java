package es.uca.tfg.ceramic_affair_web.exceptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import es.uca.tfg.ceramic_affair_web.controllers.common.SuscriptorController;

/**
 * Clase para manejar específicamente las excepciones relacionadas con los suscriptores.
 * 
 * @version 1.1
 */
@ControllerAdvice(assignableTypes = SuscriptorController.class)
@Order(1)
public class SuscriptorExceptionHandler {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @ExceptionHandler(SuscriptorException.NoEncontrado.class)
    private ResponseEntity<Void> handleNoEncontrado() {
        return redirectToFrontend("not_found");
    }

    @ExceptionHandler(SuscriptorException.TokenExpirado.class)
    private ResponseEntity<Void> handleTokenExpirado() {
        return redirectToFrontend("expired");
    }

    private ResponseEntity<Void> redirectToFrontend(String status) {
        String enlace = frontendUrl + "/confirmation?status=" + status;
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", enlace)
                .build();
    }
}
