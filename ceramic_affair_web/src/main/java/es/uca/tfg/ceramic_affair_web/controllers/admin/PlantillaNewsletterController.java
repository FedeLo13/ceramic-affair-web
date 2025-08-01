package es.uca.tfg.ceramic_affair_web.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uca.tfg.ceramic_affair_web.DTOs.NewsletterDTO;
import es.uca.tfg.ceramic_affair_web.payload.ApiResponseType;
import es.uca.tfg.ceramic_affair_web.services.PlantillaNewsletterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador para la entidad PlantillaNewsletter en el panel de administración.
 * Proporciona endpoints para obtener y modificar la plantilla de newsletter.
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/plantilla")
@Tag(name = "Plantilla Newsletter Admin", description = "Controlador para la gestión de la plantilla de newsletter en el panel de administración")
@CrossOrigin(origins = "http://localhost:5173")
// TODO: Cambiar la URL de origen a la de producción cuando esté disponible
public class PlantillaNewsletterController {

    @Autowired
    private PlantillaNewsletterService plantillaNewsletterService;

    @GetMapping("/obtener")
    @Operation(summary = "Obtener la plantilla de newsletter", description = "Obtiene la única plantilla de newsletter del sistema", tags = { "Plantilla Newsletter Admin" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Plantilla obtenida con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<NewsletterDTO>> obtenerPlantillaNewsletter() {
        NewsletterDTO newsletterDTO = plantillaNewsletterService.obtenerPlantillaNewsletter();
        return ResponseEntity.ok(new ApiResponseType<>(true, "Plantilla obtenida con éxito", newsletterDTO));
    }

    @PutMapping("/modificar")
    @Operation(summary = "Modificar la plantilla de newsletter", description = "Modifica la plantilla de newsletter con los datos proporcionados en el DTO", tags = { "Plantilla Newsletter Admin" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Plantilla modificada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<Void>> modificarPlantillaNewsletter(@Valid @RequestBody NewsletterDTO newsletterDTO) {
        plantillaNewsletterService.modificarPlantillaNewsletter(newsletterDTO);
        return ResponseEntity.ok(new ApiResponseType<>(true, "Plantilla modificada con éxito", null));
    }
}
