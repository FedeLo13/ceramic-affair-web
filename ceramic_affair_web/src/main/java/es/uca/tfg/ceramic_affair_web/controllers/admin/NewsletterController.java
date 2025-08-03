package es.uca.tfg.ceramic_affair_web.controllers.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uca.tfg.ceramic_affair_web.DTOs.NewsletterDTO;
import es.uca.tfg.ceramic_affair_web.entities.Newsletter;
import es.uca.tfg.ceramic_affair_web.entities.Suscriptor;
import es.uca.tfg.ceramic_affair_web.payload.ApiResponseType;
import es.uca.tfg.ceramic_affair_web.repositories.NewsletterRepo;
import es.uca.tfg.ceramic_affair_web.repositories.SuscriptorRepo;
import es.uca.tfg.ceramic_affair_web.services.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador para la entidad Newsletter en el panel de administración.
 * Proporciona endpoints para enviar newsletters a los suscriptores.
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/newsletter")
@Tag(name = "Newsletter Admin", description = "Controlador para la gestión de newsletters en el panel de administración")
@CrossOrigin(origins = "http://localhost:5173")
// TODO: Cambiar la URL de origen a la de producción cuando esté disponible
public class NewsletterController {

    @Autowired
    private NewsletterRepo newsletterRepo;

    @Autowired
    private SuscriptorRepo suscriptorRepo;

    @Autowired
    private EmailService emailService;

    @PostMapping("/enviar")
    @Operation(summary = "Enviar newsletter", description = "Envía una newsletter a todos los suscriptores registrados", tags = { "Newsletter Admin" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Newsletter enviada correctamente"),
        @ApiResponse(responseCode = "400", description = "Error en los datos de la newsletter"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor o al enviar el correo electrónico")
    })
    public ResponseEntity<ApiResponseType<String>> enviarNewsletter(@Valid @RequestBody NewsletterDTO newsletterDTO) {
        // Guardar la newsletter en la base de datos
        Newsletter newsletter = new Newsletter(newsletterDTO.getAsunto(), newsletterDTO.getMensaje());
        newsletterRepo.save(newsletter);

        // Obtener lista de suscriptores verificados
        List<Suscriptor> suscriptores = suscriptorRepo.findByVerificadoTrue();

        // Enviar la newsletter a cada suscriptor
        for (Suscriptor suscriptor : suscriptores) {
            String enlaceDesuscripcion = "http://localhost:8080/api/public/suscriptores/desuscribir?token=" + suscriptor.getTokenDesuscripcion();

            String mensaje = "<html><body>"
                + "<div>" + newsletterDTO.getMensaje() + "</div>"
                + "<hr>"
                + "<p style='font-size:12px;color:gray;'>"
                + "Si no deseas recibir más newsletters, "
                + "<a href=\"" + enlaceDesuscripcion + "\">haz clic aquí para darte de baja</a>."
                + "</p>"
                + "</body></html>";
            
            emailService.sendEmail(suscriptor.getEmail(), newsletterDTO.getAsunto(), mensaje);
        }

        return ResponseEntity.ok(new ApiResponseType<>(true, "Newsletter enviada correctamente", "Newsletter enviada a " + suscriptores.size() + " suscriptores."));
    }
}
