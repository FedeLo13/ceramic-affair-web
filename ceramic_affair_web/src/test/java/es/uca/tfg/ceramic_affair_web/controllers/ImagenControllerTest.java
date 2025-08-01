package es.uca.tfg.ceramic_affair_web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import es.uca.tfg.ceramic_affair_web.controllers.admin.ImagenAdminController;
import es.uca.tfg.ceramic_affair_web.controllers.common.ImagenPublicController;
import es.uca.tfg.ceramic_affair_web.entities.Imagen;
import es.uca.tfg.ceramic_affair_web.exceptions.ImagenException;
import es.uca.tfg.ceramic_affair_web.security.JwtAuthFilter;
import es.uca.tfg.ceramic_affair_web.security.JwtUtils;
import es.uca.tfg.ceramic_affair_web.services.ImagenService;

/**
 * Clase de prueba para el controlador ImagenController.
 * Proporciona pruebas de integración para los endpoints relacionados con la entidad Imagen,
 * simulando peticiones HTTP sin interactuar con la base de datos.
 * 
 * @version 1.0
 */
@WebMvcTest(controllers = { ImagenPublicController.class, ImagenAdminController.class })
@AutoConfigureMockMvc(addFilters = false) // Desactivar la configuración de seguridad para las pruebas
public class ImagenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ImagenService imagenService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    @DisplayName("Controlador - Crear imagen")
    public void testCrearImagen() throws Exception {
        MockMultipartFile archivo = new MockMultipartFile(
            "archivo",
            "imagen.jpg",
            "image/jpeg",
            "Contenido de la imagen".getBytes()
        );

        Imagen imagenMock = new Imagen("imagen.jpg", "jpg", 12345f, 100f, 100f);
        when(imagenService.insertarImagen(archivo)).thenReturn(imagenMock);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/imagenes/crear")
            .file(archivo))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Imagen creada con éxito"))
            .andExpect(jsonPath("$.data.ruta").value("imagen.jpg"))
            .andExpect(jsonPath("$.data.formato").value("jpg"));
    }

    @Test
    @DisplayName("Controlador - Obtener imagen por ID")
    public void testObtenerImagen() throws Exception {
        Imagen imagen = new Imagen("imagen.jpg", "jpg", 12345f, 100f, 100f);
        when(imagenService.obtenerPorId(1L)).thenReturn(imagen);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/public/imagenes/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Imagen encontrada"))
            .andExpect(jsonPath("$.data.ruta").value("imagen.jpg"))
            .andExpect(jsonPath("$.data.formato").value("jpg"));
    }

    @Test
    @DisplayName("Controlador - Eliminar imagen por ID")
    public void testEliminarImagen() throws Exception {
        doNothing().when(imagenService).eliminarImagen(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/imagenes/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Controlador - Crear imagen (excepción no válida)")
    public void testCrearImagenExcepcionNoValida() throws Exception {
        MockMultipartFile archivo = new MockMultipartFile(
            "archivo",
            "imagen.txt",
            "text/plain",
            "Contenido de la imagen".getBytes()
        );

        when(imagenService.insertarImagen(any())).thenThrow(new ImagenException.NoValida("El archivo no es válido."));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/imagenes/crear")
            .file(archivo))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Excepción de negocio"))
            .andExpect(jsonPath("$.message").value("El archivo no es válido."))
            .andExpect(jsonPath("$.path").value("/api/admin/imagenes/crear"));
    }

    @Test
    @DisplayName("Controlador - Obtener imagen por ID (excepción no encontrada)")
    public void testObtenerImagenExcepcionNoEncontrada() throws Exception {
        when(imagenService.obtenerPorId(1L)).thenThrow(new ImagenException.NoEncontrada(1L));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/public/imagenes/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Excepción de negocio"))
            .andExpect(jsonPath("$.message").value("Imagen no encontrada con ID: 1"))
            .andExpect(jsonPath("$.path").value("/api/public/imagenes/1"));
    }

    @Test
    @DisplayName("Controlador - Eliminar imagen por ID (excepción no encontrada)")
    public void testEliminarImagenExcepcionNoEncontrada() throws Exception {
        doThrow(new ImagenException.NoEncontrada(1L)).when(imagenService).eliminarImagen(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/imagenes/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Excepción de negocio"))
            .andExpect(jsonPath("$.message").value("Imagen no encontrada con ID: 1"))
            .andExpect(jsonPath("$.path").value("/api/admin/imagenes/1"));
    }

    @Test
    @DisplayName("Controlador - Eliminar imagen por ID (excepción IO)")
    public void testEliminarImagenExcepcionIO() throws Exception {
        doThrow(new IOException("Error al borrar")).when(imagenService).eliminarImagen(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/imagenes/1"))
            .andExpect(status().isInternalServerError());
    }
}
