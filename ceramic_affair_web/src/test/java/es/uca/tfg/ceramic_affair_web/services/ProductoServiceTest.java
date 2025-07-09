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

import es.uca.tfg.ceramic_affair_web.DTOs.ProductoDTO;
import es.uca.tfg.ceramic_affair_web.entities.Categoria;
import es.uca.tfg.ceramic_affair_web.entities.Imagen;
import es.uca.tfg.ceramic_affair_web.entities.Producto;
import es.uca.tfg.ceramic_affair_web.exceptions.CategoriaException;
import es.uca.tfg.ceramic_affair_web.exceptions.ProductoException;
import es.uca.tfg.ceramic_affair_web.repositories.CategoriaRepo;
import es.uca.tfg.ceramic_affair_web.repositories.ImagenRepo;
import es.uca.tfg.ceramic_affair_web.repositories.ProductoRepo;

/**
 * Clase de prueba para el servicio ProductoService.
 * Proporciona pruebas de integración para las operaciones CRUD en la entidad Producto.
 * 
 * @version 1.1
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

    @Autowired
    private ImagenRepo imagenRepo;

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
        Categoria categoria = categoriaRepo.save(new Categoria("Cerámica"));
        Imagen imagen1 = imagenRepo.save(new Imagen("ruta/imagen1.jpg", "jpg", 1024, 800, 600));
        List<Long> idsImagenes = List.of(imagen1.getId());
        ProductoDTO productoDTO = new ProductoDTO("Taza", categoria.getId(), "Taza de cerámica", 10.0f, 8.0f, 0.0f,
                BigDecimal.valueOf(10.99), false, idsImagenes);
        Long id = productoService.insertarProducto(productoDTO);

        // Obtener el producto por su id
        Producto producto = productoService.obtenerPorId(id);

        // Verificar que el producto se ha insertado correctamente
        assertNotNull(producto);
        assertEquals("Taza", producto.getNombre());
        assertEquals(categoria.getId(), producto.getCategoria().getId());
        assertEquals("Taza de cerámica", producto.getDescripcion());
        assertEquals(10.0f, producto.getAltura());
        assertEquals(8.0f, producto.getAnchura());
        assertEquals(0.0f, producto.getDiametro());
        assertEquals(BigDecimal.valueOf(10.99), producto.getPrecio());
        assertFalse(producto.isSoldOut());
        assertNotNull(producto.getImagenes());
        assertEquals(1, producto.getImagenes().size());
        assertEquals(imagen1.getId(), producto.getImagenes().get(0).getId());
    }

    @Test
    @DisplayName("Servicio - Insertar Producto (excepción categoría no encontrada)")
    void testInsertarProductoCategoriaNoEncontrada() {
        // Intentar insertar un producto con una categoría que no existe
        ProductoDTO productoDTO = new ProductoDTO("Taza", 999L, "Taza de cerámica", 10.0f, 8.0f, 0.0f,
                BigDecimal.valueOf(10.99), false, List.of());

        assertThatThrownBy(() -> {
            productoService.insertarProducto(productoDTO);
        }).isInstanceOf(CategoriaException.NoEncontrada.class)
          .hasMessageContaining("Categoría no encontrada con id: 999");
    }

    @Test
    @DisplayName("Servicio - Modificar Producto")
    void testModificarProducto() {
        // Categoría
        Categoria categoria = categoriaRepo.save(new Categoria("Cerámica"));

        // Insertar un nuevo producto
        Long id = productoService.insertarProducto(new ProductoDTO("Taza", categoria.getId(), "Taza de cerámica", 10.0f, 8.0f, 0.0f,
                BigDecimal.valueOf(10.99), false, List.of()));

        // Modificar el producto
        ProductoDTO productoModificado = new ProductoDTO("Taza Grande", categoria.getId(), "Taza de cerámica grande", 12.0f, 9.0f, 0.0f,
                BigDecimal.valueOf(12.99), true, List.of());
        productoService.modificarProducto(id, productoModificado);

        // Obtener el producto modificado por su id
        Producto producto = productoService.obtenerPorId(id);

        // Verificar que el producto se ha modificado correctamente
        assertNotNull(producto);
        assertEquals("Taza Grande", producto.getNombre());
        assertEquals("Taza de cerámica grande", producto.getDescripcion());
        assertEquals(12.0f, producto.getAltura());
        assertEquals(9.0f, producto.getAnchura());
        assertEquals(BigDecimal.valueOf(12.99), producto.getPrecio());
        assertTrue(producto.isSoldOut());
    }

    @Test
    @DisplayName("Servicio - Modificar Producto (excepción no encontrado)")
    void testModificarProductoNoEncontrado() {
        // Intentar modificar un producto que no existe
        ProductoDTO productoModificado = new ProductoDTO("Taza Modificada", 1L, "Taza de cerámica modificada", 10.0f, 8.0f, 0.0f,
                BigDecimal.valueOf(10.99), false, List.of());

        assertThatThrownBy(() -> {
            productoService.modificarProducto(999L, productoModificado); // ID que no existe
        }).isInstanceOf(ProductoException.NoEncontrado.class)
          .hasMessageContaining("Producto no encontrado con id: 999");
    }

    @Test
    @DisplayName("Servicio - Modificar Producto (excepción categoría no encontrada)")
    void testModificarProductoCategoriaNoEncontrada() {
        Categoria categoria = categoriaRepo.save(new Categoria("Cerámica"));
        Long id = productoService.insertarProducto(new ProductoDTO("Taza", categoria.getId(), "Taza de cerámica", 10.0f, 8.0f, 0.0f,
                BigDecimal.valueOf(10.99), false, List.of()));

        ProductoDTO dtoConCategoriaInexistente = new ProductoDTO("Taza Modificada", 999L, "Taza de cerámica modificada", 10.0f, 8.0f, 0.0f,
                BigDecimal.valueOf(10.99), false, List.of());

        assertThatThrownBy(() -> {
            productoService.modificarProducto(id, dtoConCategoriaInexistente); // ID de categoría que no existe
        }).isInstanceOf(CategoriaException.NoEncontrada.class)
          .hasMessageContaining("Categoría no encontrada con id: 999");
    }

    @Test
    @DisplayName("Servicio - Establecer stock de producto")
    void testEstablecerStock() {
        // Categoría
        Categoria categoria = categoriaRepo.save(new Categoria("Cerámica"));

        // Insertar un nuevo producto
        Long id = productoService.insertarProducto(new ProductoDTO("Taza", categoria.getId(), "Taza de cerámica", 10.0f, 8.0f, 0.0f,
                BigDecimal.valueOf(10.99), false, List.of()));

        // Establecer el stock del producto a agotado (soldOut = true)
        productoService.establecerStock(id, true);

        // Obtener el producto por su id
        Producto producto = productoService.obtenerPorId(id);

        // Verificar que el stock se ha actualizado correctamente
        assertNotNull(producto);
        assertTrue(producto.isSoldOut());
    }

    @Test
    @DisplayName("Servicio - Establecer stock de producto (excepción no encontrado)")
    void testEstablecerStockNoEncontrado() {
        // Intentar establecer el stock de un producto que no existe
        assertThatThrownBy(() -> {
            productoService.establecerStock(999L, true); // ID que no existe
        }).isInstanceOf(ProductoException.NoEncontrado.class)
          .hasMessageContaining("Producto no encontrado con id: 999");
    }

    @Test
    @DisplayName("Servicio - Obtener producto por ID")
    void testObtenerPorId() {
        // Categoría
        Categoria categoria = categoriaRepo.save(new Categoria("Cerámica"));

        // Insertar un nuevo producto
        ProductoDTO productoDTO = new ProductoDTO("Taza", categoria.getId(), "Taza de cerámica", 10.0f, 8.0f, 0.0f,
                BigDecimal.valueOf(10.99), false, List.of());
        Long id = productoService.insertarProducto(productoDTO);

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
        Categoria categoria = categoriaRepo.save(new Categoria("Cerámica"));

        // Productos
        ProductoDTO productoDTO1 = new ProductoDTO("Taza", categoria.getId(), "Taza de cerámica", 10.0f, 8.0f, 0.0f,
                BigDecimal.valueOf(10.99), false, List.of());
        ProductoDTO productoDTO2 = new ProductoDTO("Plato", categoria.getId(), "Plato de cerámica", 12.0f, 10.0f, 0.0f,
                BigDecimal.valueOf(15.99), true, List.of());
        productoService.insertarProducto(productoDTO1);
        productoService.insertarProducto(productoDTO2);

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
        Categoria categoria = categoriaRepo.save(new Categoria("Cerámica"));

        // Productos
        productoService.insertarProducto(new ProductoDTO("Botella azul", categoria.getId(), "Botella de cerámica", 10f, 8f, 0f, BigDecimal.valueOf(10.99), false, List.of()));
        productoService.insertarProducto(new ProductoDTO("Botella roja", categoria.getId(), "Botella histórica", 10f, 8f, 0f, BigDecimal.valueOf(15.99), false, List.of()));
        productoService.insertarProducto(new ProductoDTO("Cuenco", categoria.getId(), "Cuenco", 10f, 8f, 0f, BigDecimal.valueOf(12.99), false, List.of()));

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
        // Categoría
        Categoria categoria = categoriaRepo.save(new Categoria("Cerámica"));

        // Productos
        productoService.insertarProducto(new ProductoDTO("Botella azul", categoria.getId(), "Botella de cerámica", 10f, 8f, 0f, BigDecimal.valueOf(10.99), false, List.of()));

        // Pequeña pausa para asegurar el orden por fecha
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        productoService.insertarProducto(new ProductoDTO("Botella roja", categoria.getId(), "Botella histórica de cerámica", 10f, 8f, 0f, BigDecimal.valueOf(15.99), false, List.of()));

        // Filtro: nombre = "Botella", categoría = null, soloEnStock = null, orden = "recientes"
        List<Producto> productosFiltrados = productoService.filtrarProductos("Botella", null, null, "recientes");

        // Verificar que se han filtrado correctamente los productos
        assertNotNull(productosFiltrados);
        assertEquals(2, productosFiltrados.size());
        assertEquals("Botella roja", productosFiltrados.get(0).getNombre());
    }

    @Test
    @DisplayName("Servicio - Filtrar productos por categoría y stock")
    void testFiltrarProductosPorCategoriaYStock() {
        // Categoría
        Categoria categoria = categoriaRepo.save(new Categoria("Cerámica"));

        // Productos
        productoService.insertarProducto(new ProductoDTO("Taza", categoria.getId(), "Taza de cerámica", 10f, 8f, 0f, BigDecimal.valueOf(10.99), false, List.of()));
        productoService.insertarProducto(new ProductoDTO("Plato", categoria.getId(), "Plato de cerámica", 12f, 10f, 0f, BigDecimal.valueOf(15.99), true, List.of()));

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
        // Categoría
        Categoria categoria = categoriaRepo.save(new Categoria("Cerámica"));

        // Productos
        productoService.insertarProducto(new ProductoDTO("Taza", categoria.getId(), "Taza de cerámica", 10f, 8f, 0f, BigDecimal.valueOf(10.99), false, List.of()));
        productoService.insertarProducto(new ProductoDTO("Plato", categoria.getId(), "Plato de cerámica", 12f, 10f, 0f, BigDecimal.valueOf(15.99), true, List.of()));

        // Filtro: nombre = null, categoría = null, soloEnStock = null, orden = null
        List<Producto> productosFiltrados = productoService.filtrarProductos(null, null, null, null);

        // Verificar que se han filtrado correctamente los productos
        assertNotNull(productosFiltrados);
        assertEquals(2, productosFiltrados.size());
    }

    @Test
    @DisplayName("Servicio - Eliminar producto por ID")
    void testEliminarProducto() {
        // Categoría
        Categoria categoria = categoriaRepo.save(new Categoria("Cerámica"));

        // Insertar un nuevo producto
        Long id = productoService.insertarProducto(new ProductoDTO("Taza", categoria.getId(), "Taza de cerámica", 10.0f, 8.0f, 0.0f,
                BigDecimal.valueOf(10.99), false, List.of()));

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
        // Categoría
        Categoria categoria = categoriaRepo.save(new Categoria("Cerámica"));

        // Insertar varios productos
        productoService.insertarProducto(new ProductoDTO("Taza", categoria.getId(), "Taza de cerámica", 10.0f, 8.0f, 0.0f,
                BigDecimal.valueOf(10.99), false, List.of()));
        productoService.insertarProducto(new ProductoDTO("Plato", categoria.getId(), "Plato de cerámica", 12.0f, 10.0f, 0.0f,
                BigDecimal.valueOf(15.99), true, List.of()));
        productoService.insertarProducto(new ProductoDTO("Cuenco", categoria.getId(), "Cuenco de cerámica", 8.0f, 6.0f, 0.0f,
                BigDecimal.valueOf(8.99), false, List.of()));

        // Obtener todos los productos
        List<Producto> productos = productoService.obtenerTodos();

        // Verificar que se han obtenido todos los productos
        assertNotNull(productos);
        assertEquals(3, productos.size());
    }
}
