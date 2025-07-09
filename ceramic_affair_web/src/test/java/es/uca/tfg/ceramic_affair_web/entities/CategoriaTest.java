package es.uca.tfg.ceramic_affair_web.entities;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Clase de prueba para la entidad Categoria.
 * Proporciona pruebas unitarias para verificar el correcto funcionamiento de los métodos de la clase Categoria.
 * 
 * @version 1.1
 */
public class CategoriaTest {

    @Test
    @DisplayName("Categoria - Constructor vacío")
    public void testConstructorVacio() {
        Categoria categoria =  new Categoria();

        assertNotNull(categoria); // Verifica que la instancia no sea nula
        assertNull(categoria.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertNull(categoria.getNombre()); // Verifica que el nombre sea nulo (no se ha establecido)
        assertNotNull(categoria.getProductos()); // Verifica que la lista de productos no sea nula
        assertTrue(categoria.getProductos().isEmpty()); // Verifica que la lista de productos esté vacía
    }

    @Test
    @DisplayName("Categoria - Constructor con parámetros")
    public void testConstructorConParametros() {
        String nombre = "Cerámica";
        Categoria categoria = new Categoria(nombre);

        assertNotNull(categoria); // Verifica que la instancia no sea nula
        assertNull(categoria.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertNotNull(categoria.getNombre()); // Verifica que el nombre no sea nulo
        assertTrue(categoria.getNombre().equals(nombre)); // Verifica que el nombre sea el esperado
        assertNotNull(categoria.getProductos()); // Verifica que la lista de productos no sea nula
        assertTrue(categoria.getProductos().isEmpty()); // Verifica que la lista de productos esté vacía
    }
}
