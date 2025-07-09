package es.uca.tfg.ceramic_affair_web.services;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.uca.tfg.ceramic_affair_web.entities.Imagen;
import es.uca.tfg.ceramic_affair_web.exceptions.ImagenException;

/**
 * Clase de prueba para el servicio ImagenService.
 * Proporciona pruebas de integración para las operaciones CRUD en la entidad Imagen.
 * 
 * @version 1.0
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ImagenServiceTest {

    @Autowired
    private ImagenService imagenService;

    @Test
    @DisplayName("Servicio - Insertar imagen")
    void testInsertarImagenValida() throws Exception{
        InputStream is = new ClassPathResource("test-image.jpg").getInputStream();
        MockMultipartFile archivo = new MockMultipartFile(
            "archivo", 
            "test-image.jpg", 
            "image/jpeg", 
            IOUtils.toByteArray(is)
        );

        Imagen imagen = imagenService.insertarImagen(archivo);

        assertNotNull(imagen.getId());
        assertTrue(imagen.getRuta().endsWith(".jpg"));
        assertEquals("jpg", imagen.getFormato());
        assertTrue(imagen.getTamano() > 0);
        assertTrue(imagen.getAncho() > 0);
        assertTrue(imagen.getAlto() > 0);
    }

    @Test
    @DisplayName("Servicio - Insertar imagen (excepción imagen no válida - tipo MIME no válido)")
    void testInsertarImagenNoValida() {
        MockMultipartFile archivo = new MockMultipartFile(
            "archivo", 
            "test.txt", 
            "text/plain", 
            "Contenido no válido".getBytes()
        );

        assertThrows(ImagenException.NoValida.class, () -> {
            imagenService.insertarImagen(archivo);
        });
    }

    @Test
    @DisplayName("Servicio - Insertar imagen (excepción imagen no válida - formato no permitido)")
    void testInsertarImagenNoValidaFormato() {
        MockMultipartFile archivo = new MockMultipartFile(
            "archivo", 
            "malicioso.exe", 
            "image/jpeg", 
            "Contenido no válido".getBytes()
        );

        assertThrows(ImagenException.NoValida.class, () -> {
            imagenService.insertarImagen(archivo);
        });
    }

    @Test
    @DisplayName("Servicio - Insertar imagen (excepción imagen no válida - imagen corrupta)")
    void testInsertarImagenNoValidaCorrupta() {
        MockMultipartFile archivo = new MockMultipartFile(
            "archivo", 
            "corrupta.jpg", 
            "image/jpeg", 
            new byte[] { 0, 1, 2, 3, 4, 5 } // Datos corruptos
        );

        assertThrows(ImagenException.NoValida.class, () -> {
            imagenService.insertarImagen(archivo);
        });
    }

    @Test
    @DisplayName("Servicio - Obtener imagen por ID")
    void testObtenerImagenPorId() throws Exception {
        InputStream is = new ClassPathResource("test-image.jpg").getInputStream();
        MockMultipartFile archivo = new MockMultipartFile(
            "archivo", 
            "test-image.jpg", 
            "image/jpeg", 
            IOUtils.toByteArray(is)
        );

        Imagen imagen = imagenService.insertarImagen(archivo);
        Imagen imagenObtenida = imagenService.obtenerPorId(imagen.getId());

        assertNotNull(imagenObtenida);
        assertEquals(imagen.getId(), imagenObtenida.getId());
        assertEquals(imagen.getFormato(), imagenObtenida.getFormato());
    }

    @Test
    @DisplayName("Servicio - Obtener imagen por ID (excepción imagen no encontrada)")
    void testObtenerImagenPorIdNoEncontrada() {
        Long idInexistente = 999L; // ID que no existe en la base de datos

        assertThrows(ImagenException.NoEncontrada.class, () -> {
            imagenService.obtenerPorId(idInexistente);
        });
    }

    @Test
    @DisplayName("Servicio - Eliminar imagen")
    void testEliminarImagen() throws Exception {
        InputStream is = new ClassPathResource("test-image.jpg").getInputStream();
        MockMultipartFile archivo = new MockMultipartFile(
            "archivo", 
            "test-image.jpg", 
            "image/jpeg", 
            IOUtils.toByteArray(is)
        );

        Imagen imagen = imagenService.insertarImagen(archivo);
        Long idImagen = imagen.getId();

        imagenService.eliminarImagen(idImagen);

        assertThrows(ImagenException.NoEncontrada.class, () -> {
            imagenService.obtenerPorId(idImagen);
        });
    }

    @Test
    @DisplayName("Servicio - Eliminar imagen (excepción imagen no encontrada)")
    void testEliminarImagenNoEncontrada() {
        Long idInexistente = 999L; // ID que no existe en la base de datos

        assertThrows(ImagenException.NoEncontrada.class, () -> {
            imagenService.eliminarImagen(idInexistente);
        });
    }

    @AfterAll
    static void limpiarDirectorio() throws Exception {
        // Limpiar el directorio de imágenes después de todas las pruebas
        Path uploadsPath = Path.of("uploads-test");
        if(Files.exists(uploadsPath)) {
            Files.walk(uploadsPath)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }
}
