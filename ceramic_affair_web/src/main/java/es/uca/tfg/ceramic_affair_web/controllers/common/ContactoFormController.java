package es.uca.tfg.ceramic_affair_web.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uca.tfg.ceramic_affair_web.DTOs.ContactoFormDTO;
import es.uca.tfg.ceramic_affair_web.entities.ContactoForm;
import es.uca.tfg.ceramic_affair_web.exceptions.EmailException;
import es.uca.tfg.ceramic_affair_web.exceptions.RecaptchaException;
import es.uca.tfg.ceramic_affair_web.payload.ApiResponseType;
import es.uca.tfg.ceramic_affair_web.repositories.ContactoFormRepo;
import es.uca.tfg.ceramic_affair_web.services.EmailService;
import es.uca.tfg.ceramic_affair_web.services.RecaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

/**
 * Controlador para los endpoints públicos relacionados con el formulario de contacto.
 * Proporciona endpoints para gestionar el formulario de contacto.
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/api/public/contacto")
@Tag(name = "Contacto Form", description = "Controlador para la gestión del formulario de contacto")
@CrossOrigin(origins = "http://localhost:5173")
// TODO: Cambiar la URL de origen a la de producción cuando esté disponible
public class ContactoFormController {

    @Autowired
    private ContactoFormRepo contactoFormRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RecaptchaService recaptchaService;

    @Value("${mail.to}")
    private String mailTo;

    @PostMapping("/enviar")
    @Operation(summary = "Enviar formulario de contacto", description = "Envía el formulario de contacto y envía un correo electrónico con los datos proporcionados", tags = { "Contacto Form" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Formulario enviado correctamente"),
        @ApiResponse(responseCode = "400", description = "Error en los datos del formulario o reCAPTCHA inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor o al enviar el correo electrónico")
    })
    public ResponseEntity<ApiResponseType<Void>> enviarFormulario(@Valid @RequestBody ContactoFormDTO dto) {
        // Validar el reCAPTCHA
        boolean isRecaptchaValid = recaptchaService.verifyRecaptcha(dto.getRecaptchaToken());
        if (!isRecaptchaValid) {
            throw new RecaptchaException.Invalido();
        }

        // Guardar el formulario de contacto en la base de datos
        ContactoForm form = new ContactoForm(
            dto.getNombre(),
            dto.getApellidos(),
            dto.getEmail(),
            dto.getAsunto(),
            dto.getMensaje()
        );
        contactoFormRepo.save(form);

        // Enviar el correo electrónico
        String cuerpo = "<p>Nuevo correo recebido desde el formulario de contacto:</p>"
                      + "<p><strong>Nombre:</strong> " + dto.getNombre() + "</p>"
                      + "<p><strong>Apellidos:</strong> " + dto.getApellidos() + "</p>"
                      + "<p><strong>Email:</strong> " + dto.getEmail() + "</p>"
                      + "<p><strong>Asunto:</strong> " + dto.getAsunto() + "</p>"
                      + "<p><strong>Mensaje:</strong> " + dto.getMensaje() + "</p>";

        try {
            emailService.sendEmail(mailTo, "Nuevo formulario de contacto", cuerpo);
        } catch (Exception e) {
            throw new EmailException.EnvioFallido(e);
        }

        // Retornar respuesta exitosa 
        return ResponseEntity.ok(new ApiResponseType<>(true, "Formulario enviado correctamente", null));
    }

}
