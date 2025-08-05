package es.uca.tfg.ceramic_affair_web.controllers.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uca.tfg.ceramic_affair_web.DTOs.FindMePostDTO;
import es.uca.tfg.ceramic_affair_web.payload.ApiResponseType;
import es.uca.tfg.ceramic_affair_web.services.FindMePostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador para los endpoints públicos relacionados con la entidad FindMePost.
 * Proporciona endpoints para obtener publicaciones "Encuéntrame".
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/api/public/find-me-posts")
@Tag(name = "FindMePosts Public", description = "Controlador para la gestión de publicaciones 'Encuéntrame'")
public class FindMePostPublicController {

    @Autowired
    private FindMePostService findMePostService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una publicación 'Encuéntrame' por su ID",
               description = "Devuelve la publicación 'Encuéntrame' correspondiente al ID proporcionado",
               tags = { "FindMePosts Public" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Publicación 'Encuéntrame' encontrada"),
        @ApiResponse(responseCode = "404", description = "Publicación 'Encuéntrame' no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<FindMePostDTO>> obtenerPublicacionPorId(@PathVariable Long id) {
        FindMePostDTO findMePostDTO = findMePostService.obtenerPorId(id);
        return ResponseEntity.ok(new ApiResponseType<>(true, "Publicación 'Encuéntrame' encontrada", findMePostDTO));
    }

    @GetMapping("/todos")
    @Operation(summary = "Obtener todas las publicaciones 'Encuéntrame'",
               description = "Devuelve una lista de todas las publicaciones 'Encuéntrame'",
               tags = { "FindMePosts Public" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de publicaciones 'Encuéntrame' encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<List<FindMePostDTO>>> obtenerTodasLasPublicaciones() {
        List<FindMePostDTO> publicaciones = findMePostService.obtenerTodas();
        return ResponseEntity.ok(new ApiResponseType<>(true, "Lista de publicaciones 'Encuéntrame' encontrada", publicaciones));
    }
}
