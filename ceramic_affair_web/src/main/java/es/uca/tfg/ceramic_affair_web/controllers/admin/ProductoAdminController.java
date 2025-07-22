package es.uca.tfg.ceramic_affair_web.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uca.tfg.ceramic_affair_web.DTOs.ProductoDTO;
import es.uca.tfg.ceramic_affair_web.DTOs.ProductoStockDTO;
import es.uca.tfg.ceramic_affair_web.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador para la entidad Producto en el panel de administración.
 * Proporciona endpoints para crear, actualizar y eliminar productos.
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/productos")
@Tag(name = "Productos Admin", description = "Controlador para la gestión de productos en el panel de administración")
@CrossOrigin(origins = "http://localhost:5173")
// TODO: Cambiar la URL de origen a la de producción cuando esté disponible
public class ProductoAdminController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/crear")
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto con los datos proporcionados en el DTO", tags = { "Productos Admin" })
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Long> crearProducto(@Valid @RequestBody ProductoDTO dto) {
        Long id = productoService.insertarProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto por su ID", description = "Actualiza el producto correspondiente al ID proporcionado con los datos del DTO", tags = { "Productos Admin" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado con éxito"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoDTO dto) {
        productoService.modificarProducto(id, dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Actualizar el stock de un producto", description = "Actualiza el estado de stock del producto correspondiente al ID proporcionado", tags = { "Productos Admin" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock del producto actualizado con éxito"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> actualizarStockProducto(@PathVariable Long id, @Valid @RequestBody ProductoStockDTO dto) {
        productoService.establecerStock(id, dto.isSoldOut());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto por su ID", description = "Elimina el producto correspondiente al ID proporcionado", tags = { "Productos Admin" })
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado con éxito"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
