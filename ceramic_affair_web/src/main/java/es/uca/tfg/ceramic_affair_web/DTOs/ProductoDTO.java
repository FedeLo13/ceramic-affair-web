package es.uca.tfg.ceramic_affair_web.DTOs;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.*;

/**
 * DTO para la entidad Producto.
 * Este DTO se utiliza para transferir datos de productos entre la capa de presentación y la capa de servicio.
 * 
 * @version 1.1
 */
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre del producto es obligatiorio")
    private String nombre;

    private Long idCategoria;

    private String descripcion;

    @NotNull(message = "La altura del producto es obligatoria")
    @PositiveOrZero(message = "La altura no puede ser negativa")
    private Float altura;

    @NotNull(message = "La anchura del producto es obligatoria")
    @PositiveOrZero(message = "La anchura no puede ser negativa")
    private Float anchura;

    @NotNull(message = "El diámetro del producto es obligatorio")
    @PositiveOrZero(message = "El diámetro no puede ser negativo")
    private Float diametro;

    @NotNull(message = "El precio del producto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que cero")
    private BigDecimal precio;

    @NotNull(message = "El campo 'soldOut' no puede ser nulo")
    private Boolean soldOut;
    
    private List<Long> idsImagenes;

    public ProductoDTO() {
        // Constructor por defecto
    }

    // Constructor para crear un ProductoDTO con ID, usar este constructor para crear un productoDTO a partir de un Producto existente
    public ProductoDTO(Long id, String nombre, Long idCategoria, String descripcion, float altura, float anchura,
                       float diametro, BigDecimal precio, Boolean soldOut, List<Long> idsImagenes) {
        this.id = id;
        this.nombre = nombre;
        this.idCategoria = idCategoria;
        this.descripcion = descripcion;
        this.altura = altura;
        this.anchura = anchura;
        this.diametro = diametro;
        this.precio = precio;
        this.soldOut = soldOut;
        this.idsImagenes = idsImagenes;
    }

    // Constructor para crear un ProductoDTO sin ID, usar este constructor para crear un productoDTO nuevo con el objetivo
    // de insertar un nuevo producto en la base de datos
    public ProductoDTO(String nombre, Long idCategoria, String descripcion, float altura, float anchura,
                       float diametro, BigDecimal precio, Boolean soldOut, List<Long> idsImagenes) {
        this.id = null; // ID se asignará automáticamente al crear el producto (usar el otro constructor para crear un productoDTO con ID)
        this.nombre = nombre;
        this.idCategoria = idCategoria;
        this.descripcion = descripcion;
        this.altura = altura;
        this.anchura = anchura;
        this.diametro = diametro;
        this.precio = precio;
        this.soldOut = soldOut;
        this.idsImagenes = idsImagenes;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public float getAnchura() {
        return anchura;
    }

    public void setAnchura(float anchura) {
        this.anchura = anchura;
    }

    public float getDiametro() {
        return diametro;
    }

    public void setDiametro(float diametro) {
        this.diametro = diametro;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public boolean isSoldOut() {
        return soldOut;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
    }

    public List<Long> getIdsImagenes() {
        return idsImagenes;
    }

    public void setIdsImagenes(List<Long> idsImagenes) {
        this.idsImagenes = idsImagenes;
    }
}
