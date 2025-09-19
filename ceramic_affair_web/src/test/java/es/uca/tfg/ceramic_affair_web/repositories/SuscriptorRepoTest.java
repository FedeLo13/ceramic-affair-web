package es.uca.tfg.ceramic_affair_web.repositories;

import es.uca.tfg.ceramic_affair_web.entities.Suscriptor;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Clase de prueba para el repositorio SuscriptorRepo.
 * Proporciona pruebas de integración para las operaciones CRUD en la entidad Suscriptor.
 * 
 * @version 1.0
 */
@DataJpaTest
public class SuscriptorRepoTest {

    @Autowired
    private SuscriptorRepo suscriptorRepo;

    @Test
    @DisplayName("Repositorio - Guardar y cargar suscriptor")
    void testGuardarYCargarSuscriptor() {
        // Crear y guardar un nuevo suscriptor
        Suscriptor suscriptor = new Suscriptor("test@example.com");
        suscriptorRepo.save(suscriptor);

        // Cargar el suscriptor por ID
        Suscriptor suscriptorCargado = suscriptorRepo.findById(suscriptor.getId()).orElse(null);
        assertNotNull(suscriptorCargado);
        assertEquals(suscriptor.getEmail(), suscriptorCargado.getEmail());
    }

    @Test
    @DisplayName("Repositorio - Eliminar suscriptor")
    void testEliminarSuscriptor() {
        // Crear y guardar un nuevo suscriptor
        Suscriptor suscriptor = new Suscriptor("test@example.com");
        suscriptorRepo.save(suscriptor);

        // Eliminar el suscriptor
        suscriptorRepo.delete(suscriptor);

        // Verificar que el suscriptor ha sido eliminado
        Suscriptor suscriptorEliminado = suscriptorRepo.findById(suscriptor.getId()).orElse(null);
        assertNull(suscriptorEliminado);
    }

    @Test
    @DisplayName("Repositorio - Buscar suscriptor por token de verificación")
    void testBuscarSuscriptorPorTokenVerificacion() {
        // Crear y guardar un nuevo suscriptor con un token de verificación
        Suscriptor suscriptor = new Suscriptor("test@example.com");
        suscriptor.setTokenVerificacion("token123");
        suscriptorRepo.save(suscriptor);

        // Buscar el suscriptor por el token de verificación
        Optional<Suscriptor> suscriptorEncontrado = suscriptorRepo.findByTokenVerificacion("token123");
        assertTrue(suscriptorEncontrado.isPresent());
        assertEquals(suscriptor.getEmail(), suscriptorEncontrado.get().getEmail());
    }

    @Test
    @DisplayName("Repositorio - Buscar suscriptor por token de desuscripción")
    void testBuscarSuscriptorPorTokenDesuscripcion() {
        // Crear y guardar un nuevo suscriptor con un token de desuscripción
        Suscriptor suscriptor = new Suscriptor("test@example.com");
        suscriptor.setTokenDesuscripcion("tokenDesuscripcion");
        suscriptorRepo.save(suscriptor);

        // Buscar el suscriptor por el token de desuscripción
        Optional<Suscriptor> suscriptorEncontrado = suscriptorRepo.findByTokenDesuscripcion("tokenDesuscripcion");
        assertTrue(suscriptorEncontrado.isPresent());
        assertEquals(suscriptor.getEmail(), suscriptorEncontrado.get().getEmail());
    }

    @Test
    @DisplayName("Repositorio - Buscar suscriptor por email")
    void testBuscarSuscriptorPorEmail() {
        // Crear y guardar un nuevo suscriptor
        Suscriptor suscriptor = new Suscriptor("test@example.com");
        suscriptorRepo.save(suscriptor);

        // Buscar el suscriptor por email
        Optional<Suscriptor> suscriptorEncontrado = suscriptorRepo.findByEmail("test@example.com");
        assertTrue(suscriptorEncontrado.isPresent());
        assertEquals(suscriptor.getEmail(), suscriptorEncontrado.get().getEmail());
    }

    @Test
    @DisplayName("Repositorio - Buscar suscriptores verificados")
    void testBuscarSuscriptoresVerificados() {
        // Crear y guardar un suscriptor verificado y otro no verificado
        Suscriptor suscriptorVerificado = new Suscriptor("test@example.com");
        suscriptorVerificado.setVerificado(true);
        suscriptorRepo.save(suscriptorVerificado);

        Suscriptor suscriptorNoVerificado = new Suscriptor("test2@example.com");
        suscriptorNoVerificado.setVerificado(false);
        suscriptorRepo.save(suscriptorNoVerificado);

        // Buscar los suscriptores verificados
        List<Suscriptor> suscriptoresVerificados = suscriptorRepo.findByVerificadoTrue();
        assertNotNull(suscriptoresVerificados);
        assertTrue(suscriptoresVerificados.stream().allMatch(Suscriptor::isVerificado));
    }

    @Test
    @DisplayName("Repositorio - Eliminar suscriptores no verificados expirados")
    void testEliminarSuscriptoresNoVerificadosExpirados() {
        // Crear y guardar un suscriptor no verificado
        Suscriptor suscriptorNoVerificado = new Suscriptor("test2@example.com");
        suscriptorNoVerificado.setVerificado(false);
        suscriptorNoVerificado.setFechaExpiracionToken(LocalDateTime.now().minusDays(1)); // Establecer una fecha de expiración pasada
        suscriptorRepo.save(suscriptorNoVerificado);

        // Eliminar suscriptores no verificados expirados
        suscriptorRepo.deleteByVerificadoFalseAndFechaExpiracionTokenBefore(LocalDateTime.now());

        // Verificar que el suscriptor no verificado ha sido eliminado
        Suscriptor suscriptorEliminado = suscriptorRepo.findById(suscriptorNoVerificado.getId()).orElse(null);
        assertNull(suscriptorEliminado);
    }
}
