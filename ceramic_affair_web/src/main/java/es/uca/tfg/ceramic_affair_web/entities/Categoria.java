package es.uca.tfg.ceramic_affair_web.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 * Clase que representa una categoría de productos en el sistema
 * 
 * @version 1.0
 */

 @Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos = new ArrayList<>();

    /**
     * Constructor vacío para JPA
     */
    public Categoria() {
    }

    /**
     * Constructor con parámetros para crear una categoría.
     * 
     * @param nombre el nombre de la categoría
     */
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método para obtener el ID de la categoría.
     * 
     * @return el ID de la categoría
     */
    public Long getId() {
        return id;
    }

    /**
     * Método para obtener el nombre de la categoría.
     * 
     * @return el nombre de la categoría
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para obtener la lista de productos de la categoría.
     * 
     * @return la lista de productos de la categoría
     */
    public List<Producto> getProductos() {
        return productos;
    }

    /**
     * Método para añadir un producto a la categoría.
     * 
     * @param producto el producto a añadir
     */
    public void addProducto(Producto producto) {
        productos.add(producto);
        producto.setCategoria(this); // Establece la categoría del producto
    }
}
