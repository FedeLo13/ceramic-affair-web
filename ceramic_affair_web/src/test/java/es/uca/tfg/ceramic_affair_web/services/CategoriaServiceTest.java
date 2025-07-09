package es.uca.tfg.ceramic_affair_web.services;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.uca.tfg.ceramic_affair_web.entities.Categoria;
import es.uca.tfg.ceramic_affair_web.entities.Producto;
import es.uca.tfg.ceramic_affair_web.exceptions.CategoriaException;
import es.uca.tfg.ceramic_affair_web.repositories.CategoriaRepo;
import es.uca.tfg.ceramic_affair_web.repositories.ProductoRepo;

/**
 * Clase de prueba para el servicio CategoriaService.
 * Proporciona pruebas de integración para las operaciones CRUD en la entidad Categoria.
 * 
 * @version 1.1
 */
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoriaServiceTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepo categoriaRepo;

    @Autowired
    private ProductoRepo productoRepo;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada prueba
        categoriaRepo.deleteAll();
        productoRepo.deleteAll();
    }

    @Test
    @DisplayName("Servicio - Insertar categoría")
    void testInsertarCategoria() {
        // Insertar una nueva categoría
        Long id = categoriaService.insertarCategoria("Cerámica");

        // Obtener la categoría por su id
        Categoria categoria = categoriaService.obtenerPorId(id);

        // Verificar que la categoría se ha insertado correctamente
        assertNotNull(categoria);
        assertEquals("Cerámica", categoria.getNombre());
    }

    @Test
    @DisplayName("Servicio - Insertar categoría (excepción)")
    void testInsertarCategoriaExistente() {
        // Insertar una nueva categoría
        categoriaService.insertarCategoria("Cerámica");

        // Intentar insertar la misma categoría de nuevo
        assertThatThrownBy(() -> {
            categoriaService.insertarCategoria("Cerámica");
        }).isInstanceOf(CategoriaException.YaExistente.class)
        .hasMessageContaining("Ya existe una categoría con el nombre: Cerámica");
    }

    @Test
    @DisplayName("Servicio - Obtener categoría por ID")
    void testObtenerPorId() {
        // Insertar una nueva categoria
        Long id = categoriaService.insertarCategoria("Cerámica");


        // Obtener la categoría por su id
        Categoria categoriaObtenida = categoriaService.obtenerPorId(id);

        // Verificar que la categoría obtenida es la misma que la insertada
        assertNotNull(categoriaObtenida);
        assertEquals(id, categoriaObtenida.getId());
        assertEquals("Cerámica", categoriaObtenida.getNombre());
    }

    @Test
    @DisplayName("Servicio - Obtener categoría por ID (excepción)")
    void testObtenerPorIdInexistente() {
        // Intentar obtener una categoría que no existe
        assertThatThrownBy(() -> {
            categoriaService.obtenerPorId(999L);
        }).isInstanceOf(CategoriaException.NoEncontrada.class)
        .hasMessageContaining("Categoría no encontrada con id: 999");
    }

    @Test
    @DisplayName("Servicio - Eliminar categoría")
    void testEliminarCategoria() {
        // Insertar una nueva categoría y producto
        Long idCategoria = categoriaService.insertarCategoria("Cerámica");
        Producto producto = new Producto("Taza", null, "Taza de cerámica", 0, 0, 0, BigDecimal.valueOf(10.0), false, null);
        producto.setCategoria(categoriaService.obtenerPorId(idCategoria));
        productoRepo.save(producto);

        // Eliminar la categoría
        categoriaService.eliminarCategoria(idCategoria);

        // Verificar que la categoría ha sido eliminada
        Producto productoObtenido = productoRepo.findById(producto.getId()).orElse(null);
        assertNull(productoObtenido.getCategoria());
        assertThat(categoriaRepo.findById(idCategoria)).isEmpty();
    }

    @Test
    @DisplayName("Servicio - Eliminar categoría (excepción)")
    void testEliminarCategoriaInexistente() {
        // Intentar eliminar una categoría que no existe
        assertThatThrownBy(() -> {
            categoriaService.eliminarCategoria(999L);
        }).isInstanceOf(CategoriaException.NoEncontrada.class)
        .hasMessageContaining("Categoría no encontrada con id: 999");
    }

    @Test
    @DisplayName("Servicio - Obtener todas las categorías")
    void testObtenerTodas() {
        // Insertar varias categorías
        categoriaService.insertarCategoria("Cerámica");
        categoriaService.insertarCategoria("Vidrio");

        // Obtener todas las categorías y verificar que se han recuperado correctamente
        List<Categoria> categorias = categoriaService.obtenerTodas();
        assertNotNull(categorias);
        assertEquals(2, categorias.size());
    }
}
