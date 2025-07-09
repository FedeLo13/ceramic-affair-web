package es.uca.tfg.ceramic_affair_web.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import es.uca.tfg.ceramic_affair_web.entities.Categoria;
import es.uca.tfg.ceramic_affair_web.entities.Producto;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Clase de prueba para el repositorio ProductoRepo.
 * Proporciona pruebas de integración para las operaciones CRUD en la entidad Producto.
 * 
 * @version 1.1
 */
@DataJpaTest
public class ProductoRepoTest {

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private CategoriaRepo categoriaRepo;

    @Test
    @DisplayName("Repositorio - Guardar y cargar producto")
    void testGuardarYCargarProducto() {
        // Crear y guardar un nuevo producto
        Producto producto = new Producto("Taza", null, "Taza de barro", 0, 0, 0, BigDecimal.valueOf(.99), false, null);
        productoRepo.save(producto);

        // Obtener el producto guardado por su ID
        Optional<Producto> encontrado = productoRepo.findById(producto.getId());

        // Verificar que el producto se ha guardado correctamente
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNombre()).isEqualTo("Taza");
        assertThat(encontrado.get().getDescripcion()).isEqualTo("Taza de barro");
        assertThat(encontrado.get().getPrecio()).isEqualTo(BigDecimal.valueOf(.99));
        assertTrue(!encontrado.get().isSoldOut());
        assertThat(encontrado.get().getCategoria()).isNull();
        assertThat(encontrado.get().getAltura()).isEqualTo(0);
        assertThat(encontrado.get().getAnchura()).isEqualTo(0);
        assertThat(encontrado.get().getDiametro()).isEqualTo(0);
        assertThat(encontrado.get().getFechaCreacion()).isNotNull();
        assertThat(encontrado.get().getImagenes()).isEmpty();
    }

    @Test
    @DisplayName("Repositorio - Eliminar producto")
    void testEliminarProducto() {
        // Crear y guardar un nuevo producto
        Producto producto = new Producto("Plato", null, "Plato de cerámica", 0, 0, 0, BigDecimal.valueOf(1.99), false, null);
        productoRepo.save(producto);

        // Eliminar el producto
        productoRepo.delete(producto);

        // Verificar que el producto no existe
        Optional<Producto> encontrado = productoRepo.findById(producto.getId());
        assertThat(encontrado).isNotPresent();
    }

    @Test
    @DisplayName("Repositorio - Relación con categoría")
    void testRelacionConCategoria() {
        // Crear y guardar una nueva categoría y un nuevo producto
        Categoria categoria = new Categoria("Cerámica");
        Producto producto = new Producto("Jarrón", null, "Jarrón de barro", 0, 0, 0, BigDecimal.valueOf(2.99), false, null);
        categoriaRepo.save(categoria);

        // Ahora sí, establecemos la relación entre el producto y la categoría
        producto.setCategoria(categoria);
        productoRepo.save(producto);

        // Verificar que la categoría del producto es la correcta
        Optional<Producto> encontrado = productoRepo.findById(producto.getId());
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getCategoria()).isEqualTo(categoria);

        // Verificar que el producto está en la lista de productos de la categoría
        Optional<Categoria> categoriaEncontrada = categoriaRepo.findById(categoria.getId());
        assertThat(categoriaEncontrada).isPresent();
        assertThat(categoriaEncontrada.get().getProductos()).contains(producto);
    }
}
