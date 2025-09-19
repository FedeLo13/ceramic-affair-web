package es.uca.tfg.ceramic_affair_web.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.uca.tfg.ceramic_affair_web.DTOs.NewsletterDTO;
import es.uca.tfg.ceramic_affair_web.controllers.admin.PlantillaNewsletterController;
import es.uca.tfg.ceramic_affair_web.security.JwtUtils;
import es.uca.tfg.ceramic_affair_web.services.PlantillaNewsletterService;

/**
 * Test para el controlador PlantillaNewsletterController.
 * Verifica el correcto funcionamiento de los endpoints relacionados con la plantilla de newsletter.
 * 
 * @version 1.0
 */
@WebMvcTest(controllers = PlantillaNewsletterController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad para pruebas
public class PlantillaNewsletterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PlantillaNewsletterService plantillaNewsletterService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Controlador - Obtener plantilla de newsletter")
    public void testObtenerPlantillaNewsletter() throws Exception {
        // Configurar el comportamiento del servicio
        NewsletterDTO newsletterDTO = new NewsletterDTO("Asunto de prueba", "Contenido de prueba");
        when(plantillaNewsletterService.obtenerPlantillaNewsletter()).thenReturn(newsletterDTO);

        // Realizar la petición GET al endpoint
        mockMvc.perform(get("/api/admin/plantilla/obtener"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Plantilla obtenida con éxito"))
                .andExpect(jsonPath("$.data.asunto").value("Asunto de prueba"))
                .andExpect(jsonPath("$.data.mensaje").value("Contenido de prueba"));
    }

    @Test
    @DisplayName("Controlador - Modificar plantilla de newsletter")
    public void testModificarPlantillaNewsletter() throws Exception {
        // Crear un DTO de prueba
        NewsletterDTO dto = new NewsletterDTO("Nuevo Asunto", "Nuevo Contenido");

        // Realizar la petición PUT al endpoint
        mockMvc.perform(put("/api/admin/plantilla/modificar")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Plantilla modificada con éxito"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}
