package es.uca.tfg.ceramic_affair_web.repositories;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import es.uca.tfg.ceramic_affair_web.entities.Imagen;

/**
 * Clase de prueba para el repositorio ImagenRepo.
 * Proporciona pruebas de integración para las operaciones CRUD en la entidad Imagen.
 * 
 * @version 1.0
 */
@DataJpaTest
public class ImagenRepoTest {

    @Autowired
    private ImagenRepo imagenRepo;

    @Test
    @DisplayName("Repositorio - Guardar y cargar imagen")
    void testGuardaryCargarImagen() {
        // Crear y guardar una nueva imagen
        Imagen imagen = new Imagen("ruta/a/la/imagen.jpg", "jpg", 1024.0f, 800.0f, 600.0f);
        imagenRepo.save(imagen);

        // Obtener la imagen guardada por su ID
        Optional<Imagen> encontrado = imagenRepo.findById(imagen.getId());

        // Verificar que la imagen se ha guardado correctamente
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getRuta()).isEqualTo("ruta/a/la/imagen.jpg");
        assertThat(encontrado.get().getFormato()).isEqualTo("jpg");
        assertThat(encontrado.get().getTamano()).isEqualTo(1024.0f);
        assertThat(encontrado.get().getAncho()).isEqualTo(800.0f);
        assertThat(encontrado.get().getAlto()).isEqualTo(600.0f);
    }

    @Test
    @DisplayName("Repositorio - Eliminar imagen")
    void testEliminarImagen() {
        // Crear y guardar una nueva imagen
        Imagen imagen = new Imagen("ruta/a/la/imagen.jpg", "jpg", 1024.0f, 800.0f, 600.0f);
        imagenRepo.save(imagen);

        // Eliminar la imagen
        imagenRepo.delete(imagen);

        // Verificar que la imagen ya no existe
        Optional<Imagen> eliminado = imagenRepo.findById(imagen.getId());
        assertThat(eliminado).isNotPresent();
    }
}
