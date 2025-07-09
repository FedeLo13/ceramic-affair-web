package es.uca.tfg.ceramic_affair_web.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import es.uca.tfg.ceramic_affair_web.entities.Categoria;

/**
 * Clase de prueba para el repositorio CategoriaRepo.
 * Proporciona pruebas de integración para las operaciones CRUD en la entidad Categoria.
 * 
 * @version 1.0
 */
@DataJpaTest
public class CategoriaRepoTest {

    @Autowired
    private CategoriaRepo categoriaRepo;

    @Test
    @DisplayName("Repositorio - Guardar y cargar categoría")
    void testGuardarYCargarCategoria() {
        // Crear y guardar una nueva categoría
        Categoria categoria = new Categoria("Cerámica");
        categoriaRepo.save(categoria);

        // Obtener la categoría guardada por su ID
        Optional<Categoria> encontrada = categoriaRepo.findById(categoria.getId());

        // Verificar que la categoría se ha guardado correctamente
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getNombre()).isEqualTo("Cerámica");
    }

    @Test
    @DisplayName("Repositorio - Eliminar categoría")
    void testEliminarCategoria() {
        // Crear y guardar una nueva categoría
        Categoria categoria = new Categoria("Platos");
        categoriaRepo.save(categoria);

        // Eliminar la categoría
        categoriaRepo.delete(categoria);

        // Verificar que la categoría no existe más
        boolean existe = categoriaRepo.existsByNombre("Platos");
        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Repositorio - Categoría existente")
    void testExistsByNombreExistencia() {
        // Crear y guardar una nueva categoría
        Categoria categoria = new Categoria("Tazas");
        categoriaRepo.save(categoria);

        // Verificar que la categoría existe por su nombre
        boolean existe = categoriaRepo.existsByNombre("Tazas");
        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Repositorio - Categoría no existente")
    void testExistsByNombreNoExistencia() {
        // Verificar que la categoría no existe por su nombre
        boolean existe = categoriaRepo.existsByNombre("NoExiste");
        assertThat(existe).isFalse();
    }
}