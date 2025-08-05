package es.uca.tfg.ceramic_affair_web.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uca.tfg.ceramic_affair_web.DTOs.CategoriaDTO;
import es.uca.tfg.ceramic_affair_web.payload.ApiResponseType;
import es.uca.tfg.ceramic_affair_web.services.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


/**
 * Controlador para la gestión de categorías en el panel de administración.
 * Proporciona endpoints para crear, actualizar y eliminar categorías.
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/categorias")
@Tag(name = "Categorias Admin", description = "Controlador para la gestión de categorías en el panel de administración")
public class CategoriaAdminController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/crear")
    @Operation(summary = "Crear una nueva categoría", description = "Crea una nueva categoría con el nombre proporcionado", tags = { "Categorias Admin" })
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Categoría creada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
        @ApiResponse(responseCode = "409", description = "Categoría ya existente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<Long>> crearCategoria(@Valid @RequestBody CategoriaDTO dto) {
        Long id = categoriaService.insertarCategoria(dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseType<>(true, "Categoría creada con éxito", id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar una categoría existente", description = "Modifica el nombre de una categoría existente", tags = { "Categorias Admin" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría modificada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "409", description = "Nombre de categoría ya existente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<Void>> modificarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaDTO dto) {
        categoriaService.modificarCategoria(id, dto.getNombre());
        return ResponseEntity.ok().body(new ApiResponseType<>(true, "Categoría modificada con éxito", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría por su ID", description = "Elimina la categoría correspondiente al ID proporcionado", tags = { "Categorias Admin" })
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Categoría eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<Void>> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
