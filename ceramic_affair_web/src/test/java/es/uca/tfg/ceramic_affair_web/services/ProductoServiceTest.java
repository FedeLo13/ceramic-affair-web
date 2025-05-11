package es.uca.tfg.ceramic_affair_web.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.uca.tfg.ceramic_affair_web.entities.Categoria;
import es.uca.tfg.ceramic_affair_web.entities.Producto;
import es.uca.tfg.ceramic_affair_web.exceptions.ProductoException;
import es.uca.tfg.ceramic_affair_web.repositories.CategoriaRepo;
import es.uca.tfg.ceramic_affair_web.repositories.ProductoRepo;

/**
 * Clase de prueba para el servicio ProductoService.
 * Proporciona pruebas de integración para las operaciones CRUD en la entidad Producto.
 * 
 * @version 1.0
 */
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductoServiceTest {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private CategoriaRepo categoriaRepo;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada prueba
        productoRepo.deleteAll();
        categoriaRepo.deleteAll();
    }

    @Test
    @DisplayName("Servicio - Insertar Producto")
    void testInsertarProducto() {
        // Insertar un nuevo producto
        Long id = productoService.insertarProducto("Taza", "Taza de cerámica", BigDecimal.valueOf(10.99));

        // Obtener el producto por su id
        Producto producto = productoService.obtenerPorId(id);

        // Verificar que el producto se ha insertado correctamente
        assertNotNull(producto);
        assertEquals("Taza", producto.getNombre());
        assertEquals("Taza de cerámica", producto.getDescripcion());
        assertEquals(BigDecimal.valueOf(10.99), producto.getPrecio());
        assertFalse(producto.isSoldOut());
    }

    @Test
    @DisplayName("Servicio - Obtener producto por ID")
    void testObtenerPorId() {
        // Insertar un nuevo producto
        Long id = productoService.insertarProducto("Taza", "Taza de cerámica", BigDecimal.valueOf(10.99));

        // Obtener el producto por su id
        Producto producto = productoService.obtenerPorId(id);

        // Verificar que el producto se ha obtenido correctamente
        assertNotNull(producto);
        assertEquals(id, producto.getId());
    }

    @Test
    @DisplayName("Servicio - Obtener producto por ID (excepción)")
    void testObtenerPorIdInexistente() {
        assertThatThrownBy(() -> {
            productoService.obtenerPorId(999L); // ID que no existe
        }).isInstanceOf(ProductoException.NoEncontrado.class)
          .hasMessageContaining("Producto no encontrado con id: 999");
    }

    @Test
    @DisplayName("Servicio - Filtrar productos con todos los filtros")
    void testFiltrarProductosConTodosLosFiltros() {
        // Categoría
        Categoria categoria = new Categoria("Cerámica");
        categoriaRepo.save(categoria);

        // Productos
        Producto producto1 = new Producto("Taza", "Taza de cerámica", BigDecimal.valueOf(10.99));
        producto1.setCategoria(categoria);
        producto1.setSoldOut(false);
        productoRepo.save(producto1);

        Producto producto2 = new Producto("Plato", "Plato de cerámica", BigDecimal.valueOf(15.99));
        producto2.setCategoria(categoria);
        producto2.setSoldOut(true);
        productoRepo.save(producto2);

        // Filtro: nombre = "Taza", categoría = "Cerámica", soloEnStock = true, orden = "viejos"
        List<Producto> productosFiltrados = productoService.filtrarProductos("Taza", categoria.getId(), true, "viejos");

        // Verificar que se han filtrado correctamente los productos
        assertNotNull(productosFiltrados);
        assertEquals(1, productosFiltrados.size());
        assertEquals("Taza", productosFiltrados.get(0).getNombre());
    }

    @Test
    @DisplayName("Servicio - Filtrar productos por nombre y categoría")
    void testFiltrarProductosPorNombreYCategoria() {
        // Categoría
        Categoria categoria = new Categoria("Cerámica");
        categoriaRepo.save(categoria);

        // Productos
        Producto producto1 = new Producto("Botella azul", "Botella de cerámica", BigDecimal.valueOf(10.99));
        producto1.setCategoria(categoria);
        productoRepo.save(producto1);

        Producto producto2 = new Producto("Botella roja", "Botella histórica de cerámica", BigDecimal.valueOf(15.99));
        producto2.setCategoria(categoria);
        productoRepo.save(producto2);

        Producto producto3 = new Producto("Cuenco", "Cuenco de cerámica", BigDecimal.valueOf(12.99));
        producto3.setCategoria(categoria);
        productoRepo.save(producto3);

        // Filtro: nombre = "Botella", categoría = "Cerámica", soloEnStock = null, orden = null
        List<Producto> productosFiltrados = productoService.filtrarProductos("Botella", categoria.getId(), null, null);

        // Verificar que se han filtrado correctamente los productos
        assertNotNull(productosFiltrados);
        assertEquals(2, productosFiltrados.size());
        assertThat(productosFiltrados).extracting(Producto::getNombre).containsExactlyInAnyOrder("Botella azul", "Botella roja");
    }

    @Test
    @DisplayName("Servicio - Filtrar productos por nombre y ordenar por fecha")
    void testFiltrarProductosPorNombreYOrden() {
        // Productos
        Producto producto1 = new Producto("Botella azul", "Botella de cerámica", BigDecimal.valueOf(10.99));
        productoRepo.save(producto1);

        // Pequeña pausa para asegurar el orden por fecha
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Producto producto2 = new Producto("Botella roja", "Botella histórica de cerámica", BigDecimal.valueOf(15.99));
        productoRepo.save(producto2);

        // Filtro: nombre = "Botella", categoría = null, soloEnStock = null, orden = "recientes"
        List<Producto> productosFiltrados = productoService.filtrarProductos("Botella", null, null, "recientes");

        // Verificar que se han filtrado correctamente los productos
        assertNotNull(productosFiltrados);
        assertEquals(2, productosFiltrados.size());
        assertEquals(producto2.getId(), productosFiltrados.get(0).getId()); // Producto más reciente primero
    }

    @Test
    @DisplayName("Servicio - Filtrar productos por categoría y stock")
    void testFiltrarProductosPorCategoriaYStock() {
        // Categoría
        Categoria categoria = new Categoria("Cerámica");
        categoriaRepo.save(categoria);

        // Productos
        Producto producto1 = new Producto("Taza", "Taza de cerámica", BigDecimal.valueOf(10.99));
        producto1.setCategoria(categoria);
        producto1.setSoldOut(false);
        productoRepo.save(producto1);

        Producto producto2 = new Producto("Plato", "Plato de cerámica", BigDecimal.valueOf(15.99));
        producto2.setCategoria(categoria);
        producto2.setSoldOut(true);
        productoRepo.save(producto2);

        // Filtro: nombre = null, categoría = "Cerámica", soloEnStock = true, orden = null
        List<Producto> productosFiltrados = productoService.filtrarProductos(null, categoria.getId(), true, null);

        // Verificar que se han filtrado correctamente los productos
        assertNotNull(productosFiltrados);
        assertEquals(1, productosFiltrados.size());
        assertEquals("Taza", productosFiltrados.get(0).getNombre());
    }

    @Test
    @DisplayName("Servicio - Filtrar productos (filtro vacío)")
    void testFiltrarProductosSinFiltros() {
        // Productos
        Producto producto1 = new Producto("Botella azul", "Botella de cerámica", BigDecimal.valueOf(10.99));
        productoRepo.save(producto1);

        Producto producto2 = new Producto("Botella roja", "Botella histórica de cerámica", BigDecimal.valueOf(15.99));
        productoRepo.save(producto2);

        // Filtro: nombre = null, categoría = null, soloEnStock = null, orden = null
        List<Producto> productosFiltrados = productoService.filtrarProductos(null, null, null, null);

        // Verificar que se han filtrado correctamente los productos
        assertNotNull(productosFiltrados);
        assertEquals(2, productosFiltrados.size());
    }

    @Test
    @DisplayName("Servicio - Eliminar producto por ID")
    void testEliminarProducto() {
        // Insertar un nuevo producto
        Long id = productoService.insertarProducto("Taza", "Taza de cerámica", BigDecimal.valueOf(10.99));

        // Eliminar el producto
        productoService.eliminarProducto(id);

        // Verificar que el producto ha sido eliminado
        assertThat(productoRepo.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("Servicio - Eliminar producto por ID (excepción)")
    void testEliminarProductoInexistente() {
        // Intentar eliminar un producto que no existe
        assertThatThrownBy(() -> {
            productoService.eliminarProducto(999L); // ID que no existe
        }).isInstanceOf(ProductoException.NoEncontrado.class)
          .hasMessageContaining("Producto no encontrado con id: 999");
    }

    @Test
    @DisplayName("Servicio - Obtener todos los productos")
    void testObtenerTodos() {
        // Insertar varios productos
        productoService.insertarProducto("Taza", "Taza de cerámica", BigDecimal.valueOf(10.99));
        productoService.insertarProducto("Plato", "Plato de cerámica", BigDecimal.valueOf(15.99));
        productoService.insertarProducto("Cuenco", "Cuenco de cerámica", BigDecimal.valueOf(12.99));

        // Obtener todos los productos
        List<Producto> productos = productoService.obtenerTodos();

        // Verificar que se han obtenido todos los productos
        assertNotNull(productos);
        assertEquals(3, productos.size());
    }

    @Test
    @DisplayName("Servicio - Eliminar todos los productos")
    void testEliminarTodos() {
        // Insertar varios productos
        productoService.insertarProducto("Taza", "Taza de cerámica", BigDecimal.valueOf(10.99));
        productoService.insertarProducto("Plato", "Plato de cerámica", BigDecimal.valueOf(15.99));
        productoService.insertarProducto("Cuenco", "Cuenco de cerámica", BigDecimal.valueOf(12.99));

        // Eliminar todos los productos
        productoService.eliminarTodos();

        // Verificar que no quedan productos en la base de datos
        assertTrue(productoRepo.findAll().isEmpty());
    }
}
