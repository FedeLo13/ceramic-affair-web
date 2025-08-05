package es.uca.tfg.ceramic_affair_web.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uca.tfg.ceramic_affair_web.entities.Imagen;
import es.uca.tfg.ceramic_affair_web.payload.ApiResponseType;
import es.uca.tfg.ceramic_affair_web.services.ImagenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador para los endpoints públicos relacionados con la entidad Imagen.
 * Proporciona endpoints para obtener imágenes.
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/api/public/imagenes")
@Tag(name = "Imágenes Public", description = "Controlador para la gestión de imágenes")
public class ImagenPublicController {

    @Autowired
    private ImagenService imagenService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una imagen por su ID", description = "Devuelve la imagen correspondiente al ID proporcionado", tags = { "Imágenes Public" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Imagen encontrada"),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<Imagen>> obtenerImagen(@PathVariable Long id) {
        Imagen imagen = imagenService.obtenerPorId(id);
        return ResponseEntity.ok(new ApiResponseType<>(true, "Imagen encontrada", imagen));
    }
}