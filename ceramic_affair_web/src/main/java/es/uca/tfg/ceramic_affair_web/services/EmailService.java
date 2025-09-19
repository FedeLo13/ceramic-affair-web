package es.uca.tfg.ceramic_affair_web.services;

/**
 * Interfaz para el servicio de envío de correos electrónicos.
 * 
 * @version 1.0
 */
public interface EmailService {

    /**
     * Envía un correo electrónico.
     * 
     * @param to      destinatario del correo
     * @param subject asunto del correo
     * @param body    cuerpo del correo
     */
    void sendEmail(String to, String subject, String body);
}
