package es.uca.tfg.ceramic_affair_web.exceptions;

/**
 * Clase para manejar excepciones relacionadas con el reCAPTCHA.
 */
public class RecaptchaException {

    private RecaptchaException() {
        throw new UnsupportedOperationException("Esta clase no debe ser instanciada.");
    }   

    public static class Invalido extends RuntimeException {
        /**
         * Constructor de la excepción.
         * @param mensaje Mensaje de error personalizado.
         */
        public Invalido() {
            super("El token de reCAPTCHA es inválido o ha expirado. Por favor, inténtelo de nuevo.");
        }
    }
}
