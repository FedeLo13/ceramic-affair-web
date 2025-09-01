package es.uca.tfg.ceramic_affair_web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.uca.tfg.ceramic_affair_web.DTOs.ProductoDTO;
import es.uca.tfg.ceramic_affair_web.DTOs.ProductoStockDTO;
import es.uca.tfg.ceramic_affair_web.controllers.admin.ProductoAdminController;
import es.uca.tfg.ceramic_affair_web.controllers.common.ProductoPublicController;
import es.uca.tfg.ceramic_affair_web.entities.Categoria;
import es.uca.tfg.ceramic_affair_web.entities.Imagen;
import es.uca.tfg.ceramic_affair_web.entities.Producto;
import es.uca.tfg.ceramic_affair_web.exceptions.ProductoException;
import es.uca.tfg.ceramic_affair_web.security.JwtAuthFilter;
import es.uca.tfg.ceramic_affair_web.security.JwtUtils;
import es.uca.tfg.ceramic_affair_web.services.ProductoService;

/**
 * Clase de prueba para el controlador ProductoController.
 * Proporciona pruebas de capa web para las operaciones CRUD expuestas en el controlador,
 * simulando peticiones HTTP sin interactuar con la base de datos.
 * 
 * @version 1.1
 */
@WebMvcTest(controllers = {ProductoPublicController.class, ProductoAdminController.class})
@AutoConfigureMockMvc(addFilters = false) // Desactivar la configuración de seguridad para las pruebas
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    @DisplayName("Controlador - Crear producto")
    void testCrearProducto() throws Exception {
        // Arrange: Crear el DTO con los datos del producto
        ProductoDTO productoDTO = new ProductoDTO(
            "Taza", 
            1L, // ID de categoría simulado
            "Cerámica",
            "Taza de cerámica", 
            10.0f, 
            8.0f, 
            8.0f, 
            new BigDecimal("10.00"), 
            false, 
            List.of(100L) // IDs de imágenes simulados
        );

        // Convertir el DTO a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(productoDTO);

        // Simular la inserción del producto y devolver un ID simulado
        when(productoService.insertarProducto(any(ProductoDTO.class))).thenReturn(1L);

        // Act + Assert: realizar POST con JSON y verificar resultado
        mockMvc.perform(post("/api/admin/productos/crear")
            .contentType("application/json")
            .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Producto creado con éxito"))
            .andExpect(jsonPath("$.data").value(1L));
    }

    @Test
    @DisplayName("Controlador - Obtener producto por ID")
    void testObtenerProductoPorId() throws Exception {
        // Simular la obtención de un producto por ID
        Long id = 1L;
        Categoria categoria = new Categoria("Cerámica");
        Imagen imagen = new Imagen("Imagen1.jpg", "jpg", 0, 0, 0);
        Producto producto = new Producto("Taza", categoria, "Taza de cerámica", 10.0f, 8.0f, 8.0f, new BigDecimal("10.00"), false, List.of(imagen));

        when(productoService.obtenerPorId(id)).thenReturn(producto);

        // Realizar la petición GET al endpoint de obtención de producto por ID
        mockMvc.perform(get("/api/public/productos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Producto encontrado"))
                .andExpect(jsonPath("$.data.id").value(producto.getId()))
                .andExpect(jsonPath("$.data.nombre").value(producto.getNombre()))
                .andExpect(jsonPath("$.data.idCategoria").value(producto.getCategoria().getId()))
                .andExpect(jsonPath("$.data.descripcion").value(producto.getDescripcion()))
                .andExpect(jsonPath("$.data.precio").value(producto.getPrecio().doubleValue()))
                .andExpect(jsonPath("$.data.altura").value(producto.getAltura()))
                .andExpect(jsonPath("$.data.anchura").value(producto.getAnchura()))
                .andExpect(jsonPath("$.data.diametro").value(producto.getDiametro()))
                .andExpect(jsonPath("$.data.soldOut").value(producto.isSoldOut()));
    }

    @Test
    @DisplayName("Controlador - Filtrar productos")
    void testFiltrarProductos() throws Exception {
        // Simular la obtención de productos filtrados
        Categoria categoria = new Categoria("Cerámica");

        Producto producto1 = new Producto(
            "Taza", 
            categoria, 
            "Taza de cerámica", 
            10.0f, 
            8.0f, 
            8.0f, 
            new BigDecimal("10.00"), 
            false, 
            List.of(new Imagen("taza.jpg", "jpg", 0, 0, 0))
        );

        Producto producto2 = new Producto(
            "Plato", 
            categoria, 
            "Plato de cerámica", 
            15.0f, 
            15.0f, 
            15.0f, 
            new BigDecimal("15.00"), 
            false, 
            List.of(new Imagen("plato.jpg", "jpg", 0, 0, 0))
        );

        List<Producto> productos = List.of(producto1, producto2);

        // Página de paginación
        Page<Producto> productosPage = new PageImpl<>(productos, PageRequest.of(0, 10), productos.size());
        
        when(productoService.filtrarProductos(any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(productosPage);

        // Realizar la petición GET al endpoint de filtrado de productos
        mockMvc.perform(get("/api/public/productos/filtrar")
                    .param("page", "0")
                    .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lista de productos encontrada"))
                // Acceder al contenido de la página
                .andExpect(jsonPath("$.data.content.length()").value(2))

                .andExpect(jsonPath("$.data.content[0].nombre").value("Taza"))
                .andExpect(jsonPath("$.data.content[0].descripcion").value("Taza de cerámica"))
                .andExpect(jsonPath("$.data.content[0].precio").value(10.00))
                .andExpect(jsonPath("$.data.content[0].soldOut").value(false))
                .andExpect(jsonPath("$.data.content[0].idCategoria").value(categoria.getId()))

                .andExpect(jsonPath("$.data.content[1].nombre").value("Plato"))
                .andExpect(jsonPath("$.data.content[1].descripcion").value("Plato de cerámica"))
                .andExpect(jsonPath("$.data.content[1].precio").value(15.00))
                .andExpect(jsonPath("$.data.content[1].soldOut").value(false))
                .andExpect(jsonPath("$.data.content[1].idCategoria").value(categoria.getId()));
    }

    @Test
    @DisplayName("Controlador - Actualizar producto")
    void testActualizarProducto() throws Exception {
        // Simular la actualización de un producto
        Long id = 1L;
        ProductoDTO productoDTO = new ProductoDTO(
            "Taza Actualizada", 
            1L, // ID de categoría simulado
            "Cerámica Actualizada",
            "Taza de cerámica actualizada", 
            12.0f, 
            9.0f, 
            9.0f, 
            new BigDecimal("12.00"), 
            false, 
            List.of(100L) // IDs de imágenes simulados
        );

        // Convertir el DTO a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(productoDTO);

        // Simular la actualización del producto
        doNothing().when(productoService).modificarProducto(id, productoDTO);

        // Realizar la petición PUT al endpoint de actualización de producto
        mockMvc.perform(put("/api/admin/productos/{id}", id)
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Producto actualizado con éxito"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("Controlador - Actualizar stock de producto")
    void testActualizarStockProducto() throws Exception {
        // Simular la actualización del stock de un producto
        Long id = 1L;
        ProductoStockDTO stockDTO = new ProductoStockDTO(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(stockDTO);

        // Simular que no se lanza ninguna excepción al actualizar el stock
        doNothing().when(productoService).establecerStock(id, stockDTO.isSoldOut());

        // Realizar la petición PATCH al endpoint de actualización de stock de producto
        mockMvc.perform(patch("/api/admin/productos/{id}/stock", id)
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Stock del producto actualizado con éxito"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("Controlador - Eliminar producto")
    void testEliminarProducto() throws Exception {
        // Simular la eliminación de un producto por ID
        Long id = 1L;
        doNothing().when(productoService).eliminarProducto(id);

        // Realizar la petición DELETE al endpoint de eliminación de producto
        mockMvc.perform(delete("/api/admin/productos/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Controlador - Obtener todos los productos")
    void testObtenerTodosLosProductos() throws Exception {
        // Simular la obtención de todos los productos
        Categoria categoria = new Categoria("Cerámica");

        Producto producto1 = new Producto(
            "Taza", 
            categoria, 
            "Taza de cerámica", 
            10.0f, 
            8.0f, 
            8.0f, 
            new BigDecimal("10.00"), 
            false, 
            List.of(new Imagen("taza.jpg", "jpg", 0, 0, 0))
        );

        Producto producto2 = new Producto(
            "Plato", 
            categoria, 
            "Plato de cerámica", 
            15.0f, 
            15.0f, 
            15.0f, 
            new BigDecimal("15.00"), 
            false, 
            List.of(new Imagen("plato.jpg", "jpg", 0, 0, 0))
        );

        List<Producto> productos = List.of(producto1, producto2);

        // Página de paginación
        Page<Producto> productosPage = new PageImpl<>(productos, PageRequest.of(0, 10), productos.size());

        when(productoService.obtenerTodos(any(Pageable.class)))
                .thenReturn(productosPage);

        // Realizar la petición GET al endpoint de obtención de todos los productos
        mockMvc.perform(get("/api/public/productos/todos")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lista de productos encontrada"))
                // Acceder al contenido de la página
                .andExpect(jsonPath("$.data.content.length()").value(2))

                .andExpect(jsonPath("$.data.content[0].nombre").value("Taza"))
                .andExpect(jsonPath("$.data.content[0].descripcion").value("Taza de cerámica"))
                .andExpect(jsonPath("$.data.content[0].precio").value(10.00))
                .andExpect(jsonPath("$.data.content[0].soldOut").value(false))
                .andExpect(jsonPath("$.data.content[0].idCategoria").value(categoria.getId()))

                .andExpect(jsonPath("$.data.content[1].nombre").value("Plato"))
                .andExpect(jsonPath("$.data.content[1].descripcion").value("Plato de cerámica"))
                .andExpect(jsonPath("$.data.content[1].precio").value(15.00))
                .andExpect(jsonPath("$.data.content[1].soldOut").value(false))
                .andExpect(jsonPath("$.data.content[1].idCategoria").value(categoria.getId()));
    }

    @Test
    @DisplayName("Controlador - Obtener producto por ID (excepción no encontrado)")
    void testObtenerProductoPorIdNoEncontrado() throws Exception {
        // Simular la obtención de un producto por ID que no existe
        Long id = 999L;
        when(productoService.obtenerPorId(id)).thenThrow(new ProductoException.NoEncontrado(id));

        // Realizar la petición GET al endpoint de obtención de producto por ID
        mockMvc.perform(get("/api/public/productos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Business exception"))
                .andExpect(jsonPath("$.message").value("Product not found with ID " + id))
                .andExpect(jsonPath("$.path").value("/api/public/productos/" + id));
    }

    @Test
    @DisplayName("Controlador - Actualizar producto (excepción no encontrado)")
    void testActualizarProductoNoEncontrado() throws Exception {
        // Simular la actualización de un producto por ID que no existe
        Long id = 999L;
        ProductoDTO productoDTO = new ProductoDTO(
            "Taza Actualizada", 
            1L, 
            "Cerámica Actualizada",
            "Taza de cerámica actualizada", 
            12.0f, 
            9.0f, 
            9.0f, 
            new BigDecimal("12.00"), 
            false, 
            List.of(100L)
        );

        // Convertir el DTO a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(productoDTO);

        // Simular que se lanza una excepción al intentar actualizar un producto no encontrado
        doThrow(new ProductoException.NoEncontrado(id)).when(productoService).modificarProducto(eq(id), any(ProductoDTO.class));

        // Realizar la petición PUT al endpoint de actualización de producto
        mockMvc.perform(put("/api/admin/productos/{id}", id)
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Business exception"))
                .andExpect(jsonPath("$.message").value("Product not found with ID " + id))
                .andExpect(jsonPath("$.path").value("/api/admin/productos/" + id));

    }

    @Test
    @DisplayName("Controlador - Actualizar stock de producto (excepción no encontrado)")
    void testActualizarStockProductoNoEncontrado() throws Exception {
        // Simular la actualización del stock de un producto por ID que no existe
        Long id = 999L;
        ProductoStockDTO stockDTO = new ProductoStockDTO(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(stockDTO);

        // Simular que se lanza una excepción al intentar actualizar el stock de un producto no encontrado
        doThrow(new ProductoException.NoEncontrado(id)).when(productoService).establecerStock(eq(id), any(Boolean.class));

        // Realizar la petición PATCH al endpoint de actualización de stock de producto
        mockMvc.perform(patch("/api/admin/productos/{id}/stock", id)
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Business exception"))
                .andExpect(jsonPath("$.message").value("Product not found with ID " + id))
                .andExpect(jsonPath("$.path").value("/api/admin/productos/" + id + "/stock"));
    }

    @Test
    @DisplayName("Controlador - Eliminar producto (excepción no encontrado)")
    void testEliminarProductoNoEncontrado() throws Exception {
        // Simular la eliminación de un producto por ID que no existe
        Long id = 999L;
        doThrow(new ProductoException.NoEncontrado(id)).when(productoService).eliminarProducto(id);

        // Realizar la petición DELETE al endpoint de eliminación de producto
        mockMvc.perform(delete("/api/admin/productos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Business exception"))
                .andExpect(jsonPath("$.message").value("Product not found with ID " + id))
                .andExpect(jsonPath("$.path").value("/api/admin/productos/" + id));
    }
}
