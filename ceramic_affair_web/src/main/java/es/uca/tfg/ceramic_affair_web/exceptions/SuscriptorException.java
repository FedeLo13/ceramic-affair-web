package es.uca.tfg.ceramic_affair_web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Clase para manejar excepciones relacionadas con los suscriptores.
 */
public class SuscriptorException {

    /**
     * Constructor privado para evitar la instanciación de esta clase.
     */
    private SuscriptorException() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    /**
     * Excepción lanzada cuando no se encuentra un suscriptor.
     */
    public static class NoEncontrado extends BusinessException {
        public NoEncontrado() {
            super("Subscriber not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Excepción lanzada cuando el suscriptor ya está verificado.
     */
    public static class YaVerificado extends BusinessException {
        public YaVerificado(String email) {
            super("The subscriber with email: " + email + " is already subscribed", HttpStatus.CONFLICT);
        }
    }

    /**
     * Excepción lanzada cuando el token de verificación ha expirado.
     */
    public static class TokenExpirado extends BusinessException {
        public TokenExpirado() {
            super("The verification link has expired", HttpStatus.GONE);
        }
    }

    /**
     * Excepción lanzada cuando la verificación del suscriptor está pendiente.
     */
    public static class VerificacionPendiente extends BusinessException {
        public VerificacionPendiente(String email) {
            super("The verification of the subscriber with email: " + email + " is pending", HttpStatus.BAD_REQUEST);
        }
    }
}
