package es.uca.tfg.ceramic_affair_web.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.uca.tfg.ceramic_affair_web.entities.Imagen;
import es.uca.tfg.ceramic_affair_web.services.ImagenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;

/**
 * Controlador para la entidad Imagen.
 * Proporciona endpoints para la gestión de imágenes, como la inserción y recuperación de imágenes
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/api/imagenes")
@Tag(name = "Imágenes", description = "Controlador para la gestión de imágenes")
@CrossOrigin(origins = "http://localhost:5173")
//TODO: Cambiar la URL de origen a la de producción cuando esté disponible
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Crear una nueva imagen", description = "Inserta una nueva imagen en el sistema a partir de un archivo", tags = { "Imágenes" })
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Imagen creada con éxito"),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, el archivo no es una imagen válida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Imagen> crearImagen(@RequestParam("archivo") MultipartFile archivo) throws IOException {
        Imagen imagen = imagenService.insertarImagen(archivo);
        return new ResponseEntity<>(imagen, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una imagen por su ID", description = "Devuelve la imagen correspondiente al ID proporcionado", tags = { "Imágenes" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Imagen encontrada"),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Imagen> obtenerImagen(@PathVariable Long id) {
        Imagen imagen = imagenService.obtenerPorId(id);
        return ResponseEntity.ok(imagen);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una imagen por su ID", description = "Elimina la imagen correspondiente al ID proporcionado", tags = { "Imágenes" })
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Imagen eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarImagen(@PathVariable Long id) throws IOException{
        imagenService.eliminarImagen(id);
        return ResponseEntity.noContent().build();
    }
}
