package es.uca.tfg.ceramic_affair_web.exceptions;

/**
 * Clase para manejar excepciones relacionadas con el reCAPTCHA.
 */
public class RecaptchaException {

    private RecaptchaException() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static class Invalido extends RuntimeException {
        /**
         * Constructor de la excepción.
         * @param mensaje Mensaje de error personalizado.
         */
        public Invalido() {
            super("The reCAPTCHA token is invalid or has expired. Please try again.");
        }
    }
}
