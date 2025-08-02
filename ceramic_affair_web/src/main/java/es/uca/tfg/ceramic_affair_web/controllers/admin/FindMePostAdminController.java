package es.uca.tfg.ceramic_affair_web.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uca.tfg.ceramic_affair_web.DTOs.FindMePostDTO;
import es.uca.tfg.ceramic_affair_web.payload.ApiResponseType;
import es.uca.tfg.ceramic_affair_web.services.FindMePostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador para la entidad FindMePost en el panel de administración.
 * Proporciona endpoints para crear, actualizar y eliminar publicaciones "Encuéntrame".
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/find-me-posts")
@Tag(name = "FindMePosts Admin", description = "Operaciones administrativas para las publicaciones 'Encuéntrame'")
@CrossOrigin(origins = "http://localhost:5173")
// TODO: Cambiar la URL de origen a la de producción cuando esté disponible
public class FindMePostAdminController {

    @Autowired
    private FindMePostService findMePostService;

    @PostMapping("/crear")
    @Operation(summary = "Crear una nueva publicación 'Encuéntrame'", description = "Crea una nueva publicación 'Encuéntrame' con los datos proporcionados en el DTO", tags = { "FindMePosts Admin" })
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Publicación 'Encuéntrame' creada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<Long>> crearPublicacion(@Valid @RequestBody FindMePostDTO findMePostDTO) {
        Long id = findMePostService.insertarFindMePost(findMePostDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseType<>(true, "Publicación 'Encuéntrame' creada con éxito", id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una publicación 'Encuéntrame' por su ID",
               description = "Actualiza la publicación 'Encuéntrame' correspondiente al ID proporcionado con los datos del DTO",
               tags = { "FindMePosts Admin" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Publicación 'Encuéntrame' actualizada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
        @ApiResponse(responseCode = "404", description = "Publicación 'Encuéntrame' no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<Void>> actualizarPublicacion(@PathVariable Long id,
                                                                       @Valid @RequestBody FindMePostDTO findMePostDTO) {
        findMePostService.modificarFindMePost(id, findMePostDTO);
        return ResponseEntity.ok()
                .body(new ApiResponseType<>(true, "Publicación 'Encuéntrame' actualizada con éxito", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una publicación 'Encuéntrame' por su ID",
               description = "Elimina la publicación 'Encuéntrame' correspondiente al ID proporcionado",
               tags = { "FindMePosts Admin" })
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Publicación 'Encuéntrame' eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Publicación 'Encuéntrame' no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<Void>> eliminarPublicacion(@PathVariable Long id) {
        findMePostService.eliminarFindMePost(id);
        return ResponseEntity.noContent()
                .build();
    }
}