package es.uca.tfg.ceramic_affair_web.DTOs;

import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) para la entidad ProductoStock.
 * Este DTO se utiliza para transferir información de stock de productos, específico para 
 * la gestión de inventario y disponibilidad de productos en la aplicación.
 * 
 * @version 1.0
 */
public class ProductoStockDTO {

    @NotNull(message = "El campo 'soldOut' no puede ser nulo")   
    private Boolean soldOut;

    public ProductoStockDTO() {
        // Constructor por defecto
    }

    public ProductoStockDTO(Boolean soldOut) {
        this.soldOut = soldOut;
    }

    public boolean isSoldOut() {
        return soldOut;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
    }
}
