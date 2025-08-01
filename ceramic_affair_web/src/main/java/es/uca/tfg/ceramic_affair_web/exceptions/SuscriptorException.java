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
        throw new UnsupportedOperationException("Esta clase no debe ser instanciada");
    }

    /**
     * Excepción lanzada cuando no se encuentra un suscriptor.
     */
    public static class NoEncontrado extends BusinessException {
        public NoEncontrado() {
            super("Suscriptor no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Excepción lanzada cuando el suscriptor ya está verificado.
     */
    public static class YaVerificado extends BusinessException {
        public YaVerificado(String email) {
            super("El suscriptor con email: " + email + " ya está verificado", HttpStatus.CONFLICT);
        }
    }

    /**
     * Excepción lanzada cuando el token de verificación ha expirado.
     */
    public static class TokenExpirado extends BusinessException {
        public TokenExpirado() {
            super("El token de verificación ha expirado", HttpStatus.GONE);
        }
    }

    /**
     * Excepción lanzada cuando la verificación del suscriptor está pendiente.
     */
    public static class VerificacionPendiente extends BusinessException {
        public VerificacionPendiente(String email) {
            super("La verificación del suscriptor con email: " + email + " está pendiente", HttpStatus.BAD_REQUEST);
        }
    }
}
