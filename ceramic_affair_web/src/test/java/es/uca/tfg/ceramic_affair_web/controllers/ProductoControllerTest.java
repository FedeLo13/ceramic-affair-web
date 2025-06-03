package es.uca.tfg.ceramic_affair_web.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import es.uca.tfg.ceramic_affair_web.entities.Producto;
import es.uca.tfg.ceramic_affair_web.exceptions.ProductoException;
import es.uca.tfg.ceramic_affair_web.services.ProductoService;

/**
 * Clase de prueba para el controlador ProductoController.
 * Proporciona pruebas de capa web para las operaciones CRUD expuestas en el controlador,
 * simulando peticiones HTTP sin interactuar con la base de datos.
 * 
 * @version 1.0
 */
@WebMvcTest(ProductoController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactivar la configuración de seguridad para las pruebas
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @Test
    @DisplayName("Controlador - Crear producto")
    void testCrearProducto() throws Exception {
        // Arrange: preparar valores y simulación del servicio
        String jsonBody = """
            {
                "nombre": "Taza",
                "descripcion": "Taza de cerámica",
                "precio": 10.00
            }
            """;

        when(productoService.insertarProducto("Taza", "Taza de cerámica", new BigDecimal("10.00"))).thenReturn(1L);

        // Act + Assert: realizar POST con JSON y verificar resultado
        mockMvc.perform(post("/api/productos/crear")
            .contentType("application/json")
            .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(content().string("1"));
    }   

    @Test
    @DisplayName("Controlador - Obtener producto por ID")
    void testObtenerProductoPorId() throws Exception {
        // Simular la obtención de un producto por ID
        Long id = 1L;
        Producto producto = new Producto("Taza", "Taza de cerámica", new BigDecimal("10.00"));

        when(productoService.obtenerPorId(id)).thenReturn(producto);

        // Realizar la petición GET al endpoint de obtención de producto por ID
        mockMvc.perform(get("/api/productos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value(producto.getNombre()))
                .andExpect(jsonPath("$.descripcion").value(producto.getDescripcion()));
            }

    @Test
    @DisplayName("Controlador - Filtrar productos")
    void testFiltrarProductos() throws Exception {
        List<Producto> productos = List.of(
            new Producto("Taza", "Taza de cerámica", new BigDecimal("10.00")),
            new Producto("Plato", "Plato de cerámica", new BigDecimal("15.00"))
        );
        when(productoService.filtrarProductos(null, null, null, null)).thenReturn(productos);

        // Realizar la petición GET al endpoint de filtrado de productos
        mockMvc.perform(get("/api/productos/filtrar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Taza"))
                .andExpect(jsonPath("$[1].nombre").value("Plato"));
    }

    @Test
    @DisplayName("Controlador - Eliminar producto")
    void testEliminarProducto() throws Exception {
        // Simular la eliminación de un producto por ID
        Long id = 1L;
        doNothing().when(productoService).eliminarProducto(id);

        // Realizar la petición DELETE al endpoint de eliminación de producto
        mockMvc.perform(delete("/api/productos/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Controlador - Obtener todos los productos")
    void testObtenerTodosLosProductos() throws Exception {
        // Simular la obtención de todos los productos
        List<Producto> productos = List.of(
            new Producto("Taza", "Taza de cerámica", new BigDecimal("10.00")),
            new Producto("Plato", "Plato de cerámica", new BigDecimal("15.00"))
        );
        when(productoService.obtenerTodos()).thenReturn(productos);

        // Realizar la petición GET al endpoint de obtención de todos los productos
        mockMvc.perform(get("/api/productos/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Taza"))
                .andExpect(jsonPath("$[1].nombre").value("Plato"));
    }

    @Test
    @DisplayName("Controlador - Eliminar todos los productos")
    void testEliminarTodosLosProductos() throws Exception {
        // Simular la eliminación de todos los productos
        doNothing().when(productoService).eliminarProducto(null);

        // Realizar la petición DELETE al endpoint de eliminación de todos los productos
        mockMvc.perform(delete("/api/productos/eliminarTodos"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Controlador - Obtener producto por ID (excepción no encontrado)")
    void testObtenerProductoPorIdNoEncontrado() throws Exception {
        // Simular la obtención de un producto por ID que no existe
        Long id = 999L;
        when(productoService.obtenerPorId(id)).thenThrow(new ProductoException.NoEncontrado(id));

        // Realizar la petición GET al endpoint de obtención de producto por ID
        mockMvc.perform(get("/api/productos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Producto no encontrado con id: " + id));
    }

    @Test
    @DisplayName("Controlador - Eliminar producto (excepción no encontrado)")
    void testEliminarProductoNoEncontrado() throws Exception {
        // Simular la eliminación de un producto por ID que no existe
        Long id = 999L;
        doThrow(new ProductoException.NoEncontrado(id)).when(productoService).eliminarProducto(id);

        // Realizar la petición DELETE al endpoint de eliminación de producto
        mockMvc.perform(delete("/api/productos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Producto no encontrado con id: " + id));
    }
}
