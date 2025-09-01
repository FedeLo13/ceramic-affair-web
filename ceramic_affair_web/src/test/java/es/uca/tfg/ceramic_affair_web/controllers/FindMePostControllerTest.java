package es.uca.tfg.ceramic_affair_web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.uca.tfg.ceramic_affair_web.DTOs.FindMePostDTO;
import es.uca.tfg.ceramic_affair_web.controllers.admin.FindMePostAdminController;
import es.uca.tfg.ceramic_affair_web.controllers.common.FindMePostPublicController;
import es.uca.tfg.ceramic_affair_web.exceptions.FindMePostException;
import es.uca.tfg.ceramic_affair_web.security.JwtAuthFilter;
import es.uca.tfg.ceramic_affair_web.security.JwtUtils;
import es.uca.tfg.ceramic_affair_web.services.FindMePostService;

/**
 * Controlador para las pruebas de la entidad FindMePost.
 * Este controlador se utiliza para realizar pruebas unitarias y de integración
 * de las funcionalidades relacionadas con las publicaciones "Encuéntrame".
 * 
 * @version 1.0
 */
@WebMvcTest(controllers = {FindMePostPublicController.class, FindMePostAdminController.class})
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad para pruebas
public class FindMePostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FindMePostService findMePostService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    @DisplayName("Controlador - Crear publicación 'Encuéntrame'")
    public void testCrearPublicacionEncuentrame() throws Exception {
        FindMePostDTO findMePostDTO = new FindMePostDTO("Título de prueba", 
                                                        "Descripción de prueba", 
                                                        LocalDateTime.of(2023, 10, 1, 12, 0),
                                                        LocalDateTime.of(2023, 10, 31, 12, 0),
                                                        70.0,
                                                        -30.0);

        // Convertir el DTO a JSON y realizar la petición POST
        String jsonRequest = objectMapper.writeValueAsString(findMePostDTO);

        // Simular inserción y devolver id simulado
        when(findMePostService.insertarFindMePost(any(FindMePostDTO.class)))
            .thenReturn(1L); // Simula que se ha insertado correctamente y devuelve un ID

        mockMvc.perform(post("/api/admin/find-me-posts/crear")
                    .contentType("application/json")
                    .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Publicación 'Encuéntrame' creada con éxito"))
                .andExpect(jsonPath("$.data").value(1L)); // Verifica que el ID devuelto es 1
    }

    @Test
    @DisplayName("Controlador - Obtener publicación 'Encuéntrame' por ID")
    public void testObtenerPublicacionEncuentramePorId() throws Exception {
        // Simular el servicio para devolver un objeto FindMePostDTO
        FindMePostDTO findMePostDTO = new FindMePostDTO("Título de prueba", 
                                                        "Descripción de prueba", 
                                                        LocalDateTime.of(2023, 10, 1, 12, 0),
                                                        LocalDateTime.of(2023, 10, 31, 12, 0),
                                                        70.0,
                                                        -30.0);
        findMePostDTO.setId(1L); // Asignar un ID para la prueba

        when(findMePostService.obtenerPorId(1L)).thenReturn(findMePostDTO);

        mockMvc.perform(get("/api/public/find-me-posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Publicación 'Encuéntrame' encontrada"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.titulo").value("Título de prueba"))
                .andExpect(jsonPath("$.data.descripcion").value("Descripción de prueba"))
                .andExpect(jsonPath("$.data.fechaInicio").value("2023-10-01T12:00:00"))
                .andExpect(jsonPath("$.data.fechaFin").value("2023-10-31T12:00:00"))
                .andExpect(jsonPath("$.data.latitud").value(70.0))
                .andExpect(jsonPath("$.data.longitud").value(-30.0));
    }

    @Test
    @DisplayName("Controlador - Obtener todas las publicaciones 'Encuéntrame'")
    public void testObtenerTodasLasPublicacionesEncuentrame() throws Exception {
        // Simular el servicio para devolver una lista de FindMePostDTO
        FindMePostDTO findMePostDTO1 = new FindMePostDTO("Título 1", "Descripción 1", 
                                                         LocalDateTime.of(2023, 10, 1, 12, 0),
                                                         LocalDateTime.of(2023, 10, 31, 12, 0),
                                                         70.0, -30.0);
        FindMePostDTO findMePostDTO2 = new FindMePostDTO("Título 2", "Descripción 2", 
                                                         LocalDateTime.of(2023, 11, 1, 12, 0),
                                                         LocalDateTime.of(2023, 11, 30, 12, 0),
                                                         75.0, -35.0);
        when(findMePostService.obtenerTodas()).thenReturn(List.of(findMePostDTO1, findMePostDTO2));

        mockMvc.perform(get("/api/public/find-me-posts/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lista de publicaciones 'Encuéntrame' encontrada"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].titulo").value("Título 1"))
                .andExpect(jsonPath("$.data[1].titulo").value("Título 2"));
    }

    @Test
    @DisplayName("Controlador - Actualizar publicación 'Encuéntrame' por ID")
    public void testActualizarPublicacionEncuentramePorId() throws Exception {
        // Simular el servicio para devolver un objeto FindMePostDTO
        FindMePostDTO findMePostDTO = new FindMePostDTO("Título de prueba", 
                                                        "Descripción de prueba", 
                                                        LocalDateTime.of(2023, 10, 1, 12, 0),
                                                        LocalDateTime.of(2023, 10, 31, 12, 0),
                                                        70.0,
                                                        -30.0);
        when(findMePostService.obtenerPorId(1L)).thenReturn(findMePostDTO);

        // Crear un objeto JSON para la solicitud de actualización
        String jsonRequest = "{ \"titulo\": \"Título actualizado\", \"descripcion\": \"Descripción actualizada\", \"fechaInicio\": \"2023-10-01T12:00:00\", \"fechaFin\": \"2023-10-31T12:00:00\", \"latitud\": 70.0, \"longitud\": -30.0 }";

        mockMvc.perform(put("/api/admin/find-me-posts/1")
                    .contentType("application/json")
                    .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Publicación 'Encuéntrame' actualizada con éxito"))
                .andExpect(jsonPath("$.data").doesNotExist()); // Verifica que no hay datos en la respuesta
    }

    @Test
    @DisplayName("Controlador - Eliminar publicación 'Encuéntrame' por ID")
    public void testEliminarPublicacionEncuentramePorId() throws Exception {
        // Simular el servicio para eliminar una publicación
        doNothing().when(findMePostService).eliminarFindMePost(1L);

        mockMvc.perform(delete("/api/admin/find-me-posts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Controlador - Obtener publicación 'Encuéntrame' (excepción no encontrada)")
    public void testObtenerPublicacionEncuentramePorIdNotFound() throws Exception {
        // Simular el servicio para lanzar una excepción cuando no se encuentra la publicación
        when(findMePostService.obtenerPorId(999L)).thenThrow(new FindMePostException.NoEncontrado(999L));

        mockMvc.perform(get("/api/public/find-me-posts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Business exception"))
                .andExpect(jsonPath("$.message").value("Find Me Post not found with ID 999"))
                .andExpect(jsonPath("$.path").value("/api/public/find-me-posts/999"));
    }

    @Test
    @DisplayName("Controlador - Actualizar publicación 'Encuéntrame' (excepción no encontrada)")
    public void testActualizarPublicacionEncuentramePorIdNotFound() throws Exception {
        // Simular el servicio para lanzar una excepción cuando no se encuentra la publicación
        doThrow(new FindMePostException.NoEncontrado(999L)).when(findMePostService).modificarFindMePost(any(Long.class), any(FindMePostDTO.class));

        // Crear un objeto JSON para la solicitud de actualización
        String jsonRequest = "{ \"titulo\": \"Título actualizado\", \"descripcion\": \"Descripción actualizada\", \"fechaInicio\": \"2023-10-01T12:00:00\", \"fechaFin\": \"2023-10-31T12:00:00\", \"latitud\": 70.0, \"longitud\": -30.0 }";

        mockMvc.perform(put("/api/admin/find-me-posts/999")
                    .contentType("application/json")
                    .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Business exception"))
                .andExpect(jsonPath("$.message").value("Find Me Post not found with ID 999"))
                .andExpect(jsonPath("$.path").value("/api/admin/find-me-posts/999"));
    }

    @Test
    @DisplayName("Controlador - Eliminar publicación 'Encuéntrame' (excepción no encontrada)")
    public void testEliminarPublicacionEncuentramePorIdNotFound() throws Exception {
        // Simular el servicio para lanzar una excepción cuando no se encuentra la publicación
        doThrow(new FindMePostException.NoEncontrado(999L)).when(findMePostService).eliminarFindMePost(999L);

        mockMvc.perform(delete("/api/admin/find-me-posts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Business exception"))
                .andExpect(jsonPath("$.message").value("Find Me Post not found with ID 999"))
                .andExpect(jsonPath("$.path").value("/api/admin/find-me-posts/999"));
    }
}
