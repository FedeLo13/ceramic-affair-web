package es.uca.tfg.ceramic_affair_web.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import es.uca.tfg.ceramic_affair_web.DTOs.FindMePostDTO;
import es.uca.tfg.ceramic_affair_web.exceptions.FindMePostException;
import es.uca.tfg.ceramic_affair_web.repositories.FindMePostRepo;

/**
 * Servicio para pruebas unitarias de FindMePostService.
 * Este servicio se utiliza para realizar pruebas de integración sobre la lógica de negocio relacionada con las publicaciones "Encuéntrame".
 * 
 * @version 1.0
 */
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FindMePostServiceTest {

    @Autowired
    private FindMePostService findMePostService;

    @Autowired
    private FindMePostRepo findMePostRepo;

    @MockitoBean
    private GmailEmailService gmailEmailService;

    @MockitoBean
    private RecaptchaService recaptchaService;

    @BeforeEach
    void setUp() {
        //Limpiar la base de datos antes de cada prueba
        findMePostRepo.deleteAll();
    }

    @Test
    @DisplayName("Servicio - Insertar publicación 'Encuéntrame'")
    void testInsertarPublicacionEncuentrame() {
        FindMePostDTO findMePostDTO = new FindMePostDTO("Título de prueba", 
                                                        "Descripción de prueba", 
                                                        LocalDateTime.of(2023, 10, 1, 0, 0), 
                                                        LocalDateTime.of(2023, 10, 31, 0, 0), 
                                                        36.5, 
                                                        -6.3);
        Long id = findMePostService.insertarFindMePost(findMePostDTO);

        FindMePostDTO insertedPost = findMePostService.obtenerPorId(id);

        assertNotNull(insertedPost);
        assertEquals(findMePostDTO.getTitulo(), insertedPost.getTitulo());
        assertEquals(findMePostDTO.getDescripcion(), insertedPost.getDescripcion());
        assertEquals(findMePostDTO.getFechaInicio(), insertedPost.getFechaInicio());
        assertEquals(findMePostDTO.getFechaFin(), insertedPost.getFechaFin());
        assertEquals(findMePostDTO.getLatitud(), insertedPost.getLatitud());
        assertEquals(findMePostDTO.getLongitud(), insertedPost.getLongitud());
        assertEquals(id, insertedPost.getId());
    }

    @Test
    @DisplayName("Servicio - Modificar publicación 'Encuéntrame'")
    void testModificarPublicacionEncuentrame() {
        FindMePostDTO findMePostDTO = new FindMePostDTO("Título original", 
                                                        "Descripción original", 
                                                        LocalDateTime.of(2023, 10, 1, 0, 0), 
                                                        LocalDateTime.of(2023, 10, 31, 0, 0), 
                                                        36.5, 
                                                        -6.3);
        Long id = findMePostService.insertarFindMePost(findMePostDTO);

        FindMePostDTO updatedPostDTO = new FindMePostDTO("Título actualizado", 
                                                         "Descripción actualizada", 
                                                         LocalDateTime.of(2023, 11, 1, 0, 0), 
                                                         LocalDateTime.of(2023, 11, 30, 0, 0), 
                                                         37.0, 
                                                         -6.5);
        findMePostService.modificarFindMePost(id, updatedPostDTO);

        FindMePostDTO updatedPost = findMePostService.obtenerPorId(id);

        assertNotNull(updatedPost);
        assertEquals(updatedPostDTO.getTitulo(), updatedPost.getTitulo());
        assertEquals(updatedPostDTO.getDescripcion(), updatedPost.getDescripcion());
        assertEquals(updatedPostDTO.getFechaInicio(), updatedPost.getFechaInicio());
        assertEquals(updatedPostDTO.getFechaFin(), updatedPost.getFechaFin());
        assertEquals(updatedPostDTO.getLatitud(), updatedPost.getLatitud());
        assertEquals(updatedPostDTO.getLongitud(), updatedPost.getLongitud());
    }

    @Test
    @DisplayName("Servicio - Modificar publicación 'Encuéntrame' (excepción no encontrada)")
    void testModificarPublicacionEncuentrameNotFound() {
        FindMePostDTO updatedPostDTO = new FindMePostDTO("Título actualizado", 
                                                         "Descripción actualizada", 
                                                         LocalDateTime.of(2023, 11, 1, 0, 0), 
                                                         LocalDateTime.of(2023, 11, 30, 0, 0), 
                                                         37.0, 
                                                         -6.5);
        assertThrows(FindMePostException.NoEncontrado.class, () -> {
            findMePostService.modificarFindMePost(999L, updatedPostDTO);
        });
    }

    @Test
    @DisplayName("Servicio - Obtener publicación 'Encuéntrame' por ID")
    void testObtenerPublicacionEncuentramePorId() {
        FindMePostDTO findMePostDTO = new FindMePostDTO("Título de prueba", 
                                                        "Descripción de prueba", 
                                                        LocalDateTime.of(2023, 10, 1, 0, 0), 
                                                        LocalDateTime.of(2023, 10, 31, 0, 0), 
                                                        36.5, 
                                                        -6.3);
        Long id = findMePostService.insertarFindMePost(findMePostDTO);

        FindMePostDTO foundPost = findMePostService.obtenerPorId(id);

        assertNotNull(foundPost);
        assertEquals(findMePostDTO.getTitulo(), foundPost.getTitulo());
        assertEquals(findMePostDTO.getDescripcion(), foundPost.getDescripcion());
        assertEquals(findMePostDTO.getFechaInicio(), foundPost.getFechaInicio());
        assertEquals(findMePostDTO.getFechaFin(), foundPost.getFechaFin());
        assertEquals(findMePostDTO.getLatitud(), foundPost.getLatitud());
        assertEquals(findMePostDTO.getLongitud(), foundPost.getLongitud());
    }

    @Test
    @DisplayName("Servicio - Obtener publicación 'Encuéntrame' por ID (excepción no encontrada)")
    void testObtenerPublicacionEncuentramePorIdNotFound() {
        assertThrows(FindMePostException.NoEncontrado.class, () -> {
            findMePostService.obtenerPorId(999L);
        });
    }

    @Test
    @DisplayName("Servicio - Eliminar publicación 'Encuéntrame'")
    void testEliminarPublicacionEncuentrame() {
        FindMePostDTO findMePostDTO = new FindMePostDTO("Título de prueba", 
                                                        "Descripción de prueba", 
                                                        LocalDateTime.of(2023, 10, 1, 0, 0), 
                                                        LocalDateTime.of(2023, 10, 31, 0, 0), 
                                                        36.5, 
                                                        -6.3);
        Long id = findMePostService.insertarFindMePost(findMePostDTO);

        findMePostService.eliminarFindMePost(id);

        assertThrows(FindMePostException.NoEncontrado.class, () -> {
            findMePostService.obtenerPorId(id);
        });
    }

    @Test
    @DisplayName("Servicio - Eliminar publicación 'Encuéntrame' (excepción no encontrada)")
    void testEliminarPublicacionEncuentrameNotFound() {
        assertThrows(FindMePostException.NoEncontrado.class, () -> {
            findMePostService.eliminarFindMePost(999L);
        });
    }

    @Test
    @DisplayName("Servicio - Obtener todas las publicaciones 'Encuéntrame'")
    void testObtenerTodasLasPublicacionesEncuentrame() {
        FindMePostDTO findMePostDTO1 = new FindMePostDTO("Título 1", 
                                                         "Descripción 1", 
                                                         LocalDateTime.of(2023, 10, 1, 0, 0), 
                                                         LocalDateTime.of(2023, 10, 31, 0, 0), 
                                                         36.5, 
                                                         -6.3);
        FindMePostDTO findMePostDTO2 = new FindMePostDTO("Título 2", 
                                                         "Descripción 2", 
                                                         LocalDateTime.of(2023, 11, 1, 0, 0), 
                                                         LocalDateTime.of(2023, 11, 30, 0, 0), 
                                                         37.0, 
                                                         -6.5);
        findMePostService.insertarFindMePost(findMePostDTO1);
        findMePostService.insertarFindMePost(findMePostDTO2);

        assertEquals(2, findMePostService.obtenerTodas().size());
    }
}
