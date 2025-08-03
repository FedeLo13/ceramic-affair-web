package es.uca.tfg.ceramic_affair_web.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.uca.tfg.ceramic_affair_web.DTOs.ProductoDTO;
import es.uca.tfg.ceramic_affair_web.DTOs.ProductoMapper;
import es.uca.tfg.ceramic_affair_web.entities.Producto;
import es.uca.tfg.ceramic_affair_web.payload.ApiResponseType;
import es.uca.tfg.ceramic_affair_web.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador para los endpoints públicos relacionados con la entidad Producto.
 * Proporciona endpoints para obtener y filtrar productos.
 * 
 * @version 1.1
 */
@RestController
@RequestMapping("/api/public/productos")
@Tag(name = "Productos Public", description = "Controlador para la gestión de productos")
@CrossOrigin(origins = "http://localhost:5173")
// TODO: Cambiar la URL de origen a la de producción cuando esté disponible
public class ProductoPublicController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por su ID", description = "Devuelve el producto correspondiente al ID proporcionado", tags = { "Productos Public" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<ProductoDTO>> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerPorId(id);
        ProductoDTO dto = ProductoMapper.toDTO(producto);
        return ResponseEntity.ok(new ApiResponseType<>(true, "Producto encontrado", dto));
    }

    @GetMapping("/filtrar")
    @Operation(summary = "Filtrar productos", description = "Devuelve una lista de productos según los filtros proporcionados", tags = { "Productos Public" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de productos encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<Page<ProductoDTO>>> filtrarProductos(@RequestParam(required = false) String nombre,
                                                           @RequestParam(required = false) Long categoria,
                                                           @RequestParam(required = false) Boolean soloEnStock,
                                                           @RequestParam(required = false) String orden,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productos = productoService.filtrarProductos(nombre, categoria, soloEnStock, orden, pageable);

        // Mapeamos la lista de productos a DTOs
        Page<ProductoDTO> productoDTOs = productos.map(ProductoMapper::toDTO);

        return ResponseEntity.ok(new ApiResponseType<>(true, "Lista de productos encontrada", productoDTOs));
    }

    @GetMapping("/todos")
    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista de todos los productos", tags = { "Productos Public" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de productos encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<Page<ProductoDTO>>> obtenerTodosLosProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productos = productoService.obtenerTodos(pageable);

        // Mapeamos la lista de productos a DTOs
        Page<ProductoDTO> productoDTOs = productos.map(ProductoMapper::toDTO);

        return ResponseEntity.ok(new ApiResponseType<>(true, "Lista de productos encontrada", productoDTOs));
    }
}
