package es.uca.tfg.ceramic_affair_web.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Clase de prueba para la entidad Producto.
 * Proporciona pruebas unitarias para los métodos de la clase Producto.
 * 
 * @version 1.1
 */
public class ProductoTest {
    
    @Test
    @DisplayName("Producto - Constructor vacío")
    public void testConstructorVacio() {
        Producto producto = new Producto();

        assertNotNull(producto); // Verifica que la instancia no sea nula
        assertNull(producto.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertNull(producto.getNombre()); // Verifica que el nombre sea nulo (no se ha establecido)
        assertNull(producto.getDescripcion()); // Verifica que la descripción sea nula (no se ha establecido)
        assertNull(producto.getPrecio()); // Verifica que el precio sea nulo (no se ha establecido)
        assertFalse(producto.isSoldOut()); // Verifica que soldOut sea false por defecto
        assertNull(producto.getCategoria()); // Verifica que la categoría sea nula (no se ha establecido)
        assertEquals(0.0f, producto.getAltura()); // Verifica que la altura sea 0.0 por defecto
        assertEquals(0.0f, producto.getAnchura()); // Verifica que el ancho sea 0.0 por defecto
        assertEquals(0.0f, producto.getDiametro()); // Verifica que la profundidad sea 0.0 por defecto
        assertNull(producto.getFechaCreacion()); // Verifica que la fecha de creación sea nula (aún no persiste en la base de datos)
        assertNotNull(producto.getImagenes()); // Verifica que la lista de imágenes no sea nula
        assertTrue(producto.getImagenes().isEmpty()); // Verifica que la lista de imágenes esté vacía
    }

    @Test
    @DisplayName("Producto - Constructor con parámetros")
    public void testConstructorConParametros() {
        String nombre = "Taza";
        Categoria categoria = new Categoria("Cerámica");
        String descripcion = "Taza de cerámica";
        float altura = 10.0f;
        float anchura = 8.0f;
        float diametro = 5.0f;
        BigDecimal precio = new BigDecimal(10.99);
        boolean soldOut = true;

        Producto producto = new Producto(nombre, categoria, descripcion, altura, anchura, diametro, precio, soldOut, null);

        assertNotNull(producto); // Verifica que la instancia no sea nula
        assertNull(producto.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertNotNull(producto.getNombre()); // Verifica que el nombre no sea nulo
        assertEquals(nombre, producto.getNombre()); // Verifica que el nombre sea el esperado
        assertNotNull(producto.getCategoria()); // Verifica que la categoría no sea nula
        assertEquals(categoria, producto.getCategoria()); // Verifica que la categoría sea la esper
        assertNotNull(producto.getDescripcion()); // Verifica que la descripción no sea nula
        assertEquals(descripcion, producto.getDescripcion()); // Verifica que la descripción sea la esperada
        assertEquals(altura, producto.getAltura()); // Verifica que la altura sea la esperada
        assertEquals(anchura, producto.getAnchura()); // Verifica que el ancho sea el esperado
        assertEquals(diametro, producto.getDiametro()); // Verifica que la profundidad sea la esperada
        assertTrue(producto.isSoldOut()); // Verifica que soldOut sea true
        assertNotNull(producto.getPrecio()); // Verifica que el precio no sea nulo
        assertEquals(precio, producto.getPrecio()); // Verifica que el precio sea el esperado
    }

    @Test
    @DisplayName("Producto - Setters y Getters")
    public void testSettersYGetters() {
        String nombre = "Taza";
        Categoria categoria = new Categoria("Cerámica");
        String descripcion = "Taza de cerámica";
        float altura = 10.0f;
        float anchura = 8.0f;
        float diametro = 5.0f;
        BigDecimal precio = new BigDecimal(10.99);
        boolean soldOut = true;
        Imagen imagen = new Imagen();
        List<Imagen> imagenes = List.of(imagen);

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setCategoria(categoria);
        producto.setDescripcion(descripcion);
        producto.setAltura(altura);
        producto.setAnchura(anchura);
        producto.setDiametro(diametro);
        producto.setPrecio(precio);
        producto.setSoldOut(soldOut);
        producto.setImagenes(imagenes);


        assertNotNull(producto); // Verifica que la instancia no sea nula
        assertNotNull(producto.getNombre()); // Verifica que el nombre no sea nulo
        assertEquals(nombre, producto.getNombre()); // Verifica que el nombre sea el esperado
        assertNotNull(producto.getCategoria()); // Verifica que la categoría no sea nula
        assertEquals(categoria, producto.getCategoria()); // Verifica que la categoría sea la esperada
        assertNotNull(producto.getDescripcion()); // Verifica que la descripción no sea nula
        assertEquals(descripcion, producto.getDescripcion()); // Verifica que la descripción sea la esperada
        assertEquals(altura, producto.getAltura()); // Verifica que la altura sea la esperada
        assertEquals(anchura, producto.getAnchura()); // Verifica que el ancho sea el esperado
        assertEquals(diametro, producto.getDiametro()); // Verifica que la profundidad sea la esperada
        assertTrue(producto.isSoldOut()); // Verifica que soldOut sea true
        assertNotNull(producto.getPrecio()); // Verifica que el precio no sea nulo
        assertEquals(precio, producto.getPrecio()); // Verifica que el precio sea el esperado
        assertNotNull(producto.getImagenes()); // Verifica que la lista de imágenes no sea nula
        assertEquals(1, producto.getImagenes().size()); // Verifica que la lista de imágenes tenga un elemento
        assertEquals(imagen, producto.getImagenes().get(0)); // Verifica que el elemento en la lista sea el esperado
    }

    @Test
    @DisplayName("Producto - Relación con Categoria")
    public void testRelacionCategoria() {
        Categoria cat1 = new Categoria("Cerámica");
        Categoria cat2 = new Categoria("Porcelana");
        Producto producto = new Producto("Taza", cat1, "Taza de cerámica", 10.0f, 8.0f, 5.0f, new BigDecimal(10.99), false, null);

        assertEquals(cat1, producto.getCategoria()); // Verifica que la categoría sea la esperada
        assertTrue(cat1.getProductos().contains(producto)); // Verifica que el producto esté en la lista de productos de la categoría

        producto.setCategoria(cat2);
        assertEquals(cat2, producto.getCategoria()); // Verifica que la categoría sea la esperada
        assertTrue(cat2.getProductos().contains(producto)); // Verifica que el producto esté en la lista de productos de la categoría
        assertFalse(cat1.getProductos().contains(producto)); // Verifica que el producto no esté en la lista de productos de la categoría anterior
    }
}
