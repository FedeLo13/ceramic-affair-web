package es.uca.tfg.ceramic_affair_web.controllers;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import es.uca.tfg.ceramic_affair_web.controllers.admin.CategoriaAdminController;
import es.uca.tfg.ceramic_affair_web.controllers.common.CategoriaPublicController;
import es.uca.tfg.ceramic_affair_web.entities.Categoria;
import es.uca.tfg.ceramic_affair_web.exceptions.CategoriaException;
import es.uca.tfg.ceramic_affair_web.security.JwtAuthFilter;
import es.uca.tfg.ceramic_affair_web.security.JwtUtils;
import es.uca.tfg.ceramic_affair_web.services.CategoriaService;

/**
 * Clase de prueba para los endpoints relacionados con la entidad Categoria.
 * Proporciona pruebas de capa web para las operaciones CRUD expuestas en el controlador,
 * simulando peticiones HTTP sin interactuar con la base de datos.
 * 
 * @version 1.1
 */
@WebMvcTest(controllers = { CategoriaPublicController.class, CategoriaAdminController.class })
@AutoConfigureMockMvc(addFilters = false) // Desactivar la configuración de seguridad para las pruebas
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    @DisplayName("Controlador - Crear categoría")
    void testCrearCategoria() throws Exception {
        // Simula que se crea una categoría con éxito
        when(categoriaService.insertarCategoria("Decoración")).thenReturn(42L);

        // Construye el JSON de entrada
        String jsonBody = """
            {
                "nombre": "Decoración"
            }
            """;

        // Llama al endpoint con JSON en el body
        mockMvc.perform(post("/api/admin/categorias/crear")
            .contentType("application/json")
            .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(content().string("42"));
    }

    @Test
    @DisplayName("Controlador - Obtener categoría por ID")
    void testObtenerCategoriaPorId() throws Exception {
        // Simular la obtención de una categoría por ID
        Categoria categoria = new Categoria("Cerámica");
        Long id = 1L;

        when(categoriaService.obtenerPorId(id)).thenReturn(categoria);

        // Realizar la petición GET al endpoint de obtención de categoría por ID
        mockMvc.perform(get("/api/public/categorias/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value(categoria.getNombre()));
    }

    @Test
    @DisplayName("Controlador - Modificar categoría")
    void testModificarCategoria() throws Exception {
        // Simular la modificación de una categoría existente
        Long id = 1L;
        doNothing().when(categoriaService).modificarCategoria(id, "Cerámica Moderna");

        // Crear el JSON que representa el cuerpo de la petición
        String jsonBody = """
            {
                "nombre": "Cerámica Moderna"
            }
            """;

        // Realizar la petición PUT al endpoint de modificación de categoría
        mockMvc.perform(put("/api/admin/categorias/{id}", id)
            .contentType("application/json")
            .content(jsonBody))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Controlador - Eliminar categoría")
    void testEliminarCategoria() throws Exception {
        // Simular la eliminación de una categoría por ID
        Long id = 1L;
        doNothing().when(categoriaService).eliminarCategoria(id);

        // Realizar la petición DELETE al endpoint de eliminación de categoría
        mockMvc.perform(delete("/api/admin/categorias/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Controlador - Obtener todas las categorías")
    void testObtenerTodasLasCategorias() throws Exception {
        // Simular la obtención de todas las categorías
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(new Categoria("Cerámica"));
        categorias.add(new Categoria("Porcelana"));

        when(categoriaService.obtenerTodas()).thenReturn(categorias);

        // Realizar la petición GET al endpoint de obtención de todas las categorías
        mockMvc.perform(get("/api/public/categorias/todas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Cerámica"))
                .andExpect(jsonPath("$[1].nombre").value("Porcelana"));
    }

    @Test
    @DisplayName("Controlador - Crear categoría (excepción ya existente)")
    void testCrearCategoriaYaExistente() throws Exception {
        // Simular que se lanza una excepción de categoría ya existente
        when(categoriaService.insertarCategoria("Cerámica"))
            .thenThrow(new CategoriaException.YaExistente("Cerámica"));

        // Crear el JSON que representa el cuerpo de la petición
        String jsonBody = """
            {
                "nombre": "Cerámica"
            }
            """;

        // Ejecutar la petición POST con JSON y verificar el conflicto
        mockMvc.perform(post("/api/admin/categorias/crear")
            .contentType("application/json")
            .content(jsonBody))
            .andExpect(status().isConflict())
            .andExpect(content().string("Ya existe una categoría con el nombre: Cerámica"));
    }

    @Test
    @DisplayName("Controlador - Obtener categoría por ID (excepción no encontrada)")
    void testObtenerCategoriaPorIdNoEncontrada() throws Exception {
        // Simular la obtención de una categoría por ID que no existe
        Long id = 999L;
        when(categoriaService.obtenerPorId(id))
            .thenThrow(new CategoriaException.NoEncontrada("Categoría no encontrada"));

        // Realizar la petición GET al endpoint de obtención de categoría por ID
        mockMvc.perform(get("/api/public/categorias/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Categoría no encontrada"));
    }

    @Test
    @DisplayName("Controlador - Eliminar categoría (excepción no encontrada)")
    void testEliminarCategoriaNoEncontrada() throws Exception {
        // Simular la eliminación de una categoría que no existe
        Long id = 999L;
        doThrow(new CategoriaException.NoEncontrada("Categoría no encontrada"))
            .when(categoriaService).eliminarCategoria(id);

        // Realizar la petición DELETE al endpoint de eliminación de categoría
        mockMvc.perform(delete("/api/admin/categorias/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Categoría no encontrada"));
    }

    @Test
    @DisplayName("Controlador - Modificar categoría (excepción no encontrada)")
    void testModificarCategoriaNoEncontrada() throws Exception {
        // Simular la modificación de una categoría que no existe
        Long id = 999L;
        doThrow(new CategoriaException.NoEncontrada("Categoría no encontrada"))
            .when(categoriaService).modificarCategoria(id, "Cerámica Moderna");

        // Crear el JSON que representa el cuerpo de la petición
        String jsonBody = """
            {
                "nombre": "Cerámica Moderna"
            }
            """;

        // Realizar la petición PUT al endpoint de modificación de categoría
        mockMvc.perform(put("/api/admin/categorias/{id}", id)
            .contentType("application/json")
            .content(jsonBody))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Categoría no encontrada"));
    }

    @Test
    @DisplayName("Controlador - Modificar categoría (excepción ya existente)")
    void testModificarCategoriaYaExistente() throws Exception {
        // Simular la modificación de una categoría con un nombre ya existente
        Long id = 1L;
        doThrow(new CategoriaException.YaExistente("Cerámica Moderna"))
            .when(categoriaService).modificarCategoria(id, "Cerámica Moderna");

        // Crear el JSON que representa el cuerpo de la petición
        String jsonBody = """
            {
                "nombre": "Cerámica Moderna"
            }
            """;

        // Realizar la petición PUT al endpoint de modificación de categoría
        mockMvc.perform(put("/api/admin/categorias/{id}", id)
            .contentType("application/json")
            .content(jsonBody))
            .andExpect(status().isConflict())
            .andExpect(content().string("Ya existe una categoría con el nombre: Cerámica Moderna"));
    }
}
