package es.uca.tfg.ceramic_affair_web.exceptions;

/**
 * Clase para manejar excepciones relacionadas con el correo electrónico.
 */
public class EmailException {

    private EmailException() {
        throw new UnsupportedOperationException("Esta clase no debe ser instanciada.");
    }

    /**
     * Excepción lanzada cuando hay un error al enviar un correo electrónico.
     */
    public static class EnvioFallido extends RuntimeException {
        public EnvioFallido(Throwable cause) {
            super("Error al enviar el correo electrónico", cause);
        }
    }
}
