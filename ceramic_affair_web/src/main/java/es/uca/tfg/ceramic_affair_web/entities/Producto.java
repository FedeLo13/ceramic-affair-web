package es.uca.tfg.ceramic_affair_web.entities;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;

/**
 * Clase que representa un producto en el sistema
 * 
 * @version 1.1
 */

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Lob
    private String descripcion;

    @Column(nullable = false)
    private BigDecimal precio;
    
    @Column(nullable = false)
    private boolean soldOut;

    @ManyToOne
    private Categoria categoria;

    private float altura;
    private float anchura;
    private float diametro;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "producto_id")
    private List<Imagen> imagenes = new ArrayList<>();

    /**
     * Constructor vacío para JPA
     */
    public Producto() {
    }

    /**
     * Constructor con parámetros para crear un producto.
     * 
     * @param nombre        el nombre del producto
     * @param categoria    la categoría del producto
     * @param descripcion   la descripción del producto
     * @param altura       la altura del producto
     * @param anchura      la anchura del producto
     * @param diametro     el diámetro del producto
     * @param precio        el precio del producto
     * @param soldOut      indica si el producto está vendido o no
     * @param imagenes      la lista de imágenes del producto
     */
    public Producto(String nombre, Categoria categoria, String descripcion, float altura, float anchura, float diametro,
            BigDecimal precio, boolean soldOut, List<Imagen> imagenes) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.altura = altura;
        this.anchura = anchura;
        this.diametro = diametro;
        this.precio = precio;
        this.soldOut = soldOut;
        this.imagenes = (imagenes != null) ? imagenes : new ArrayList<>();

        // Si la categoría no es nula, añadimos el producto a su lista de productos
        if (categoria != null && !categoria.getProductos().contains(this)) {
            categoria.getProductos().add(this);
        }
    }

    /**
     * Método para obtener el ID del producto.
     * 
     * @return el ID del producto
     */
    public Long getId() {
        return id;
    }

    /**
     * Método para obtener el nombre del producto.
     * 
     * @return el nombre del producto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para obtener la descripción del producto.
     * 
     * @return la descripción del producto
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Método para obtener el precio del producto.
     * 
     * @return el precio del producto
     */
    public BigDecimal getPrecio() {
        return precio;
    }

    /**
     * Método para obtener la categoría del producto.
     * 
     * @return la categoría del producto
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Método para obtener la fecha de creación del producto.
     * 
     * @return la fecha de creación del producto
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Método para obtener la lista de imágenes del producto.
     * 
     * @return la lista de imágenes del producto
     */
    public List<Imagen> getImagenes() {
        return imagenes;
    }

    /**
     * Método para obtener la altura del producto.
     * 
     * @return la altura del producto
     */
    public float getAltura() {
        return altura;
    }

    /**
     * Método para obtener la anchura del producto.
     * 
     * @return la anchura del producto
     */
    public float getAnchura() {
        return anchura;
    }

    /**
     * Método para obtener el diámetro del producto.
     * 
     * @return el diámetro del producto
     */
    public float getDiametro() {
        return diametro;
    }

    /**
     * Método para obtener si el producto está vendido o no.
     * 
     * @return true si el producto está vendido, false en caso contrario
     */
    public boolean isSoldOut() {
        return soldOut;
    }

    /**
     * Método para establecer el nombre del producto.
     * 
     * @param nombre el nuevo nombre del producto
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método para establecer la descripción del producto.
     * 
     * @param descripcion la nueva descripción del producto
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Método para establecer el precio del producto.
     * 
     * @param precio el nuevo precio del producto
     */
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    /**
     * Método para establecer la categoría del producto.
     * 
     * @param categoria la nueva categoría del producto
     */
    public void setCategoria(Categoria categoria) {
        // Si el producto ya tiene una categoría, la eliminamos de la lista de productos
        if(this.categoria != null) {
            this.categoria.getProductos().remove(this);
        }
        // Establecer la nueva categoría
        this.categoria = categoria;
        // Si la nueva categoría no es nula y no contiene al producto en su lista, lo añadimos
        if(categoria != null && !categoria.getProductos().contains(this)) {
            categoria.getProductos().add(this);
        }
    }

    /**
     * Método para establecer la altura del producto.
     * 
     * @param altura la nueva altura del producto
     */
    public void setAltura(float altura) {
        this.altura = altura;
    }

    /**
     * Método para establecer la anchura del producto.
     * 
     * @param anchura la nueva anchura del producto
     */
    public void setAnchura(float anchura) {
        this.anchura = anchura;
    }

    /**
     * Método para establecer el diámetro del producto.
     * 
     * @param diametro el nuevo diámetro del producto
     */
    public void setDiametro(float diametro) {
        this.diametro = diametro;
    }

    /**
     * Método para establecer si el producto está vendido o no.
     * 
     * @param soldOut true si el producto está vendido, false en caso contrario
     */
    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
    }

    /**
     * Método para establecer la lista de imágenes del producto.
     * 
     * @param imagenes la nueva lista de imágenes del producto
     */
    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes.clear();
        if (imagenes != null) {
            this.imagenes.addAll(imagenes);
        }
    }

    /**
     * Método para eliminar el producto de la lista de productos de la categoría antes de ser eliminado.
     */
    @PreRemove
    private void preRemove() {
        if(this.categoria != null) {
            this.categoria.getProductos().remove(this);
            this.categoria = null;
        }
    }
}
