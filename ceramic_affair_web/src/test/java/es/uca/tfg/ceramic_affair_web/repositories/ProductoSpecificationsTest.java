package es.uca.tfg.ceramic_affair_web.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;

import es.uca.tfg.ceramic_affair_web.entities.Categoria;
import es.uca.tfg.ceramic_affair_web.entities.Producto;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Clase de prueba para las especificaciones del repositorio Producto.
 * Proporciona pruebas de integración para las operaciones de búsqueda y filtrado en la entidad Producto.
 * 
 * @version 1.0
 */
@DataJpaTest
public class ProductoSpecificationsTest {

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private TestEntityManager entityManager;

    private Categoria categoria1, categoria2;
    private Producto producto1, producto2, producto3;

    @BeforeEach
    void setUp() {
        // Crear categorías
        categoria1 = new Categoria("Jarrones");
        categoria2 = new Categoria("Tazas");

        // Crear productos
        producto1 = new Producto("Jarrón de barro", categoria1, "Un jarrón de barro hecho a mano", 0, 0, 0, BigDecimal.valueOf(12.99), false, null);

        producto2 = new Producto("Taza de cerámica", categoria2, "Una taza de cerámica pintada a mano", 0, 0, 0, BigDecimal.valueOf(5.49), false, null);

        producto3 = new Producto("Cuenco de cerámica", categoria1, "Un cuenco de cerámica esmaltado", 0, 0, 0, BigDecimal.valueOf(7.99), false, null);

        // Guardar categorías y productos en la base de datos
        entityManager.persist(categoria1);
        entityManager.persist(categoria2);
        entityManager.persist(producto1);
        entityManager.persist(producto2);
        entityManager.persist(producto3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Especificación - Filtrar por categoría")
    void testFiltrarPorCategoria() {
        // Filtrar productos por categoría
        Specification<Producto> spec = ProductoSpecifications.conCategoria(categoria1.getId());
        List<Producto> productos = productoRepo.findAll(spec);

        // Verificar que solo se devuelven los productos de la categoría especificada
        assertThat(productos).hasSize(2);
        assertThat(productos.get(0).getNombre()).isEqualTo("Jarrón de barro");
        assertThat(productos.get(1).getNombre()).isEqualTo("Cuenco de cerámica");
    }

    @Test
    @DisplayName("Especificación - Filtrar por stock")
    void testFiltrarEnStock() {
        // Crear un producto en stock
        producto2.setSoldOut(true);
        entityManager.persist(producto2);
        entityManager.flush();

        // Filtrar productos en stock
        Specification<Producto> spec = ProductoSpecifications.enStock(true);
        List<Producto> productos = productoRepo.findAll(spec);

        // Verificar que solo se devuelven los productos en stock
        assertThat(productos).hasSize(2);
        assertThat(productos.get(0).getNombre()).isEqualTo("Jarrón de barro");
        assertThat(productos.get(1).getNombre()).isEqualTo("Cuenco de cerámica");
    }

    @Test
    @DisplayName("Especificación - Ordenar por fecha")
    void testOrdenarPorFecha() {
        // Ordenar productos por fecha de creación
        List<Producto> recientes = productoRepo.findAll(ProductoSpecifications.ordenarPorFecha("recientes"));
        List<Producto> viejos = productoRepo.findAll(ProductoSpecifications.ordenarPorFecha("viejos"));

        // Verificar que los productos están ordenados correctamente
        assertThat(recientes).hasSize(3);
        assertThat(viejos).hasSize(3);
        
        assertThat(recientes.get(0).getFechaCreacion()).isAfter(recientes.get(1).getFechaCreacion());
        assertThat(recientes.get(1).getFechaCreacion()).isAfter(recientes.get(2).getFechaCreacion());

        assertThat(viejos.get(0).getFechaCreacion()).isBefore(viejos.get(1).getFechaCreacion());
        assertThat(viejos.get(1).getFechaCreacion()).isBefore(viejos.get(2).getFechaCreacion());
    }

    @Test
    @DisplayName("Especificación - Filtrar por nombre")
    void testNombreLike() {
        // Filtrar productos por nombre
        Specification<Producto> spec = ProductoSpecifications.nombreLike("cerámica");
        List<Producto> productos = productoRepo.findAll(spec);

        // Verificar que solo se devuelven los productos que contienen "cerámica" en el nombre
        assertThat(productos).hasSize(2);
        assertThat(productos.get(0).getNombre()).isEqualTo("Taza de cerámica");
        assertThat(productos.get(1).getNombre()).isEqualTo("Cuenco de cerámica");
    }
}
