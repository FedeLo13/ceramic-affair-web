package es.uca.tfg.ceramic_affair_web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uca.tfg.ceramic_affair_web.DTOs.CategoriaCreateDTO;
import es.uca.tfg.ceramic_affair_web.entities.Categoria;
import es.uca.tfg.ceramic_affair_web.services.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador para la entidad Categoria.
 * Proporciona endpoints para crear y gestionar categorías.
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "Controlador para la gestión de categorías")
@CrossOrigin(origins = "http://localhost:3000")
// TODO: Cambiar la URL de origen a la de producción cuando esté disponible
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/crear")
    @Operation(summary = "Crear una nueva categoría", description = "Crea una nueva categoría con el nombre proporcionado", tags = { "Categorias" })
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Categoría creada con éxito"),
        @ApiResponse(responseCode = "409", description = "Categoría ya existente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Long> crearCategoria(@RequestBody CategoriaCreateDTO dto) {
        Long id = categoriaService.insertarCategoria(dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una categoría por su ID", description = "Devuelve la categoría correspondiente al ID proporcionado", tags = { "Categorias" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.obtenerPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría por su ID", description = "Elimina la categoría correspondiente al ID proporcionado", tags = { "Categorias" })
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Categoría eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/todas")
    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista de todas las categorías disponibles", tags = { "Categorias" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de categorías encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Categoria>> obtenerTodasLasCategorias() {
        List<Categoria> categorias = categoriaService.obtenerTodas();
        return ResponseEntity.ok(categorias);
    }

    @DeleteMapping("/eliminarTodas")
    @Operation(summary = "Eliminar todas las categorías", description = "Elimina todas las categorías de la base de datos", tags = { "Categorias" })
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Todas las categorías eliminadas con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarTodasLasCategorias() {
        categoriaService.eliminarTodas();
        return ResponseEntity.noContent().build();
    }
}
