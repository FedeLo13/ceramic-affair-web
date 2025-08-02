package es.uca.tfg.ceramic_affair_web.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.uca.tfg.ceramic_affair_web.entities.FindMePost;

/**
 * Clase de prueba para el repositorio FindMePostRepo.
 * Proporciona pruebas de integración para las operaciones CRUD en la entidad FindMePost.
 * 
 * @version 1.0
 */
@DataJpaTest
public class FindMePostRepoTest {

    @Autowired
    private FindMePostRepo findMePostRepo;

    @Test
    @DisplayName("Repositorio - Guardar y cargar publicación 'Encuéntrame'")
    void testGuardarYCargarFindMePost() {
        FindMePost findMePost = new FindMePost("Título de prueba", "Descripción de prueba", 
                                                LocalDateTime.of(2023, 10, 1, 0, 0), LocalDateTime.of(2023, 10, 31, 23, 59, 59), 
                                                36.5, -6.3);
        findMePostRepo.save(findMePost);
        
        Optional<FindMePost> encontrado = findMePostRepo.findById(findMePost.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Título de prueba", encontrado.get().getTitulo());
        assertEquals("Descripción de prueba", encontrado.get().getDescripcion());
        assertEquals(LocalDateTime.of(2023, 10, 1, 0, 0), encontrado.get().getFechaInicio());
        assertEquals(LocalDateTime.of(2023, 10, 31, 23, 59, 59), encontrado.get().getFechaFin());
        assertEquals(36.5, encontrado.get().getLatitud());
        assertEquals(-6.3, encontrado.get().getLongitud());
    }

    @Test
    @DisplayName("Repositorio - Eliminar publicación 'Encuéntrame'")
    void testEliminarFindMePost() {
        FindMePost findMePost = new FindMePost("Título de prueba", "Descripción de prueba", 
                                                LocalDateTime.of(2023, 10, 1, 0, 0), LocalDateTime.of(2023, 10, 31, 23, 59, 59), 
                                                36.5, -6.3);
        findMePostRepo.save(findMePost);
        
        findMePostRepo.delete(findMePost);
        
        Optional<FindMePost> encontrado = findMePostRepo.findById(findMePost.getId());
        assertFalse(encontrado.isPresent());
    }

    @Test
    @DisplayName("Repositorio - Buscar todas las publicaciones 'Encuéntrame' ordenadas por fecha de inicio")
    void testBuscarTodasLasFindMePostsOrdenadasPorFechaInicio() {
        FindMePost findMePost1 = new FindMePost("Título 1", "Descripción 1", 
                                                LocalDateTime.of(2023, 10, 1, 0, 0), LocalDateTime.of(2023, 10, 31, 23, 59, 59), 
                                                36.5, -6.3);
        FindMePost findMePost2 = new FindMePost("Título 2", "Descripción 2", 
                                                LocalDateTime.of(2023, 9, 1, 0, 0), LocalDateTime.of(2023, 9, 30, 23, 59, 59), 
                                                37.0, -6.5);
        findMePostRepo.save(findMePost1);
        findMePostRepo.save(findMePost2);

        List<FindMePost> publicaciones = findMePostRepo.findAllByOrderByFechaInicioDesc();
        assertEquals(2, publicaciones.size());
        assertEquals("Título 1", publicaciones.get(0).getTitulo());
        assertEquals("Título 2", publicaciones.get(1).getTitulo());
    }
}
