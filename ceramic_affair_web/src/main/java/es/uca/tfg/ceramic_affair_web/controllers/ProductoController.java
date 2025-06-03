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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.uca.tfg.ceramic_affair_web.DTOs.ProductoCreateDTO;
import es.uca.tfg.ceramic_affair_web.entities.Producto;
import es.uca.tfg.ceramic_affair_web.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Controlador para la gestión de productos")
@CrossOrigin(origins = "http://localhost:5173")
// TODO: Cambiar la URL de origen a la de producción cuando esté disponible
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/crear")
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto con el nombre, descripción y precio proporcionados", tags = { "Productos" })
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Long> crearProducto(@RequestBody ProductoCreateDTO dto) {
        Long id = productoService.insertarProducto(dto.getNombre(), dto.getDescripcion(), dto.getPrecio());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por su ID", description = "Devuelve el producto correspondiente al ID proporcionado", tags = { "Productos" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/filtrar")
    @Operation(summary = "Filtrar productos", description = "Devuelve una lista de productos según los filtros proporcionados", tags = { "Productos" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de productos encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> filtrarProductos(@RequestParam(required = false) String nombre,
                                                           @RequestParam(required = false) Long categoria,
                                                           @RequestParam(required = false) Boolean soloEnStock,
                                                           @RequestParam(required = false) String orden) {
        List<Producto> productos = productoService.filtrarProductos(nombre, categoria, soloEnStock, orden);
        return ResponseEntity.ok(productos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto por su ID", description = "Elimina el producto correspondiente al ID proporcionado", tags = { "Productos" })
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado con éxito"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/todos")
    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista de todos los productos", tags = { "Productos" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de productos encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Producto>> obtenerTodosLosProductos() {
        List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }

    @DeleteMapping("/eliminarTodos")
    @Operation(summary = "Eliminar todos los productos", description = "Elimina todos los productos de la base de datos", tags = { "Productos" })
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Todos los productos eliminados con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarTodosLosProductos() {
        productoService.eliminarTodos();
        return ResponseEntity.noContent().build();
    }
}
