package es.uca.tfg.ceramic_affair_web.controllers.common;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import es.uca.tfg.ceramic_affair_web.DTOs.SuscripcionDTO;
import es.uca.tfg.ceramic_affair_web.entities.Suscriptor;
import es.uca.tfg.ceramic_affair_web.exceptions.EmailException;
import es.uca.tfg.ceramic_affair_web.exceptions.RecaptchaException;
import es.uca.tfg.ceramic_affair_web.exceptions.SuscriptorException;
import es.uca.tfg.ceramic_affair_web.payload.ApiResponseType;
import es.uca.tfg.ceramic_affair_web.repositories.SuscriptorRepo;
import es.uca.tfg.ceramic_affair_web.services.EmailService;
import es.uca.tfg.ceramic_affair_web.services.RecaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador para los endpoints públicos relacionados con los suscriptores.
 * Proporciona endpoints para gestionar suscriptores, como la creación y verificación de suscriptores.
 * 
 * @version 1.1
 */
@RestController
@RequestMapping("/api/public/suscriptores")
@Tag(name = "Suscriptores", description = "Controlador para la gestión de suscriptores")
public class SuscriptorController {

    @Autowired
    private SuscriptorRepo suscriptorRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RecaptchaService recaptchaService;

    @Value("${app.backend.url}")
    private String backendUrl;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @PostMapping("/suscribir")
    @Operation(summary = "Suscribir un nuevo suscriptor", description = "Crea un nuevo suscriptor y envía un correo electrónico de verificación", tags = { "Suscriptores" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Suscriptor creado/encontrado y correo de verificación enviado"),
        @ApiResponse(responseCode = "400", description = "Error en los datos del formulario, reCAPTCHA inválido o verificación pendiente"),
        @ApiResponse(responseCode = "409", description = "El suscriptor ya está verificado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor o al enviar el correo electrónico")
    })
    public ResponseEntity<ApiResponseType<String>> suscribir(@Valid @RequestBody SuscripcionDTO dto) {
        // Validar el reCAPTCHA
        boolean isRecaptchaValid = recaptchaService.verifyRecaptcha(dto.getRecaptchaToken());
        if (!isRecaptchaValid) {
            throw new RecaptchaException.Invalido();
        }

        // Comprobar si el correo electrónico ya está suscrito
        Optional<Suscriptor> existingSuscriptor = suscriptorRepo.findByEmail(dto.getEmail());

        // Si está presente...
        if (existingSuscriptor.isPresent()) {
            Suscriptor sub = existingSuscriptor.get();

            // ... y está verificado, lanzar excepción
            if (sub.isVerificado()) {
                throw new SuscriptorException.YaVerificado(sub.getEmail());
            }

            // ... y no está verificado, pero el token de verificación aún no expiró, lanzar excepción
            if(LocalDateTime.now().isBefore(sub.getFechaExpiracionToken())) {
                throw new SuscriptorException.VerificacionPendiente(sub.getEmail());
            }

            // Si no se lanzó ninguna excepción, es porque el suscriptor no está verificado y el token ha expirado
            // Regenerar el token de verificación y reenviar el correo electrónico
            sub.regenerarTokenVerificacion();
            suscriptorRepo.save(sub);
            enviarCorreoVerificacion(sub);
            return ResponseEntity.ok(new ApiResponseType<>(true, "Suscriptor encontrado y correo de verificación reenviado", "Verification email has been resent"));
        }

        // Si no existe, crear un nuevo suscriptor
        Suscriptor nuevoSuscriptor = new Suscriptor(dto.getEmail());
        suscriptorRepo.save(nuevoSuscriptor);
        enviarCorreoVerificacion(nuevoSuscriptor);
        return ResponseEntity.ok(new ApiResponseType<>(true, "Suscriptor creado y correo de verificación enviado", "Verification email has been sent"));
    }

    @GetMapping("/verificar")
    @Operation(summary = "Verificar un suscriptor", description = "Verifica un suscriptor utilizando el token de verificación", tags = { "Suscriptores" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Suscriptor verificado correctamente"),
        @ApiResponse(responseCode = "404", description = "Suscriptor no encontrado"),
        @ApiResponse(responseCode = "410", description = "Token de verificación expirado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> verificarSuscriptor(@RequestParam String token) {
        // Buscar el suscriptor por el token de verificación
        Optional<Suscriptor> optionalSuscriptor = suscriptorRepo.findByTokenVerificacion(token);

        // Si no se encuentra el suscriptor, lanzar una excepción
        if (optionalSuscriptor.isEmpty()) {
            throw new SuscriptorException.NoEncontrado();
        }

        // Si el token ha expirado, lanzar una excepción
        Suscriptor suscriptor = optionalSuscriptor.get();
        if (LocalDateTime.now().isAfter(suscriptor.getFechaExpiracionToken())) {
            throw new SuscriptorException.TokenExpirado();
        }

        // Verificar el suscriptor
        suscriptor.verificar(); // Establece el suscriptor como verificado y genera un token de desuscripción
        suscriptorRepo.save(suscriptor);

        return redirectToFrontend("subscribed");
    }

    @GetMapping("/desuscribir")
    @Operation(summary = "Desuscribir un suscriptor", description = "Desuscribe a un suscriptor a partir de su token", tags = { "Suscriptores" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Suscriptor desuscrito correctamente"),
        @ApiResponse(responseCode = "404", description = "Suscriptor no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> desuscribirSuscriptor(@RequestParam String token) {
        // Buscar el suscriptor por el token de desuscripción
        Optional<Suscriptor> optionalSuscriptor = suscriptorRepo.findByTokenDesuscripcion(token);

        // Si no se encuentra el suscriptor, lanzar una excepción
        if (optionalSuscriptor.isEmpty()) {
            throw new SuscriptorException.NoEncontrado();
        }

        // Desuscribir al suscriptor
        suscriptorRepo.delete(optionalSuscriptor.get());

        return redirectToFrontend("unsubscribed");
    }

    private void enviarCorreoVerificacion(Suscriptor suscriptor) {
        String enlace = backendUrl + "/api/public/suscriptores/verificar?token=" + suscriptor.getTokenVerificacion();
        String cuerpo = "<p>Hello,</p>"
                      + "<p>Thank you for subscribing to our newsletter. To complete your subscription, please verify your email address by clicking the link below:</p>"
                      + "<p><a href=\"" + enlace + "\">Verify my email address</a></p>"
                      + "<p>If you did not subscribe, you can ignore this email.</p>"
                      + "<p>Best regards,</p>"
                      + "<p>The Ceramic Affair Team</p>";
        try {
            emailService.sendEmail(suscriptor.getEmail(), "Subscription Verification", cuerpo);
        } catch (Exception e) {
            throw new EmailException.EnvioFallido(e);
        }
    }

    private ResponseEntity<Void> redirectToFrontend(String status) {
        String enlace = frontendUrl + "/confirmation?status=" + status;
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", enlace)
                .build();
    }
}
