package es.uca.tfg.ceramic_affair_web.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import es.uca.tfg.ceramic_affair_web.entities.Producto;
import es.uca.tfg.ceramic_affair_web.exceptions.ProductoException;
import es.uca.tfg.ceramic_affair_web.repositories.ProductoRepo;
import es.uca.tfg.ceramic_affair_web.repositories.ProductoSpecifications;

/**
 * Servicio para la entidad Producto.
 * 
 * @version 1.0
 */
@Service
public class ProductoService {

    @Autowired
    private ProductoRepo productoRepo;

    /**
     * Método para insertar un nuevo producto
     * 
     * @param producto el producto a insertar
     */
    public Long insertarProducto(String nombre, String descripcion, BigDecimal precio) {
        Producto producto = new Producto(nombre, descripcion, precio);
        productoRepo.save(producto);
        return producto.getId();
    }

    /**
     * Método para obtener un producto por su id
     * 
     * @param id el id del producto a obtener
     * @return el producto con el id especificado
     * @throws ProductoException.NoEncontrado si no se encuentra el producto
     */
    public Producto obtenerPorId(Long id) {
        return productoRepo.findById(id)
            .orElseThrow(() -> new ProductoException.NoEncontrado(id));
    }

    /**
     * Método para obtener una lista de productos según un filtro
     * 
     * @param nombre el nombre a aplicar como filtro
     * @param categoria el id de la categoría a aplicar como filtro
     * @param soloEnStock true si se desean solo productos en stock, false/null si no se desea filtrar por stock
     * @param orden el orden "viejos" para mas antiguos primero, o cualquier otro valor para más recientes primero
     * @return una lista de productos que cumplen con los filtros
     */
    public List<Producto> filtrarProductos(String nombre, Long categoria, Boolean soloEnStock, String orden) {
        Specification<Producto> spec = Specification
            .where(ProductoSpecifications.nombreLike(nombre))
            .and(ProductoSpecifications.conCategoria(categoria))
            .and(ProductoSpecifications.enStock(soloEnStock))
            .and(ProductoSpecifications.ordenarPorFecha(orden));

        return productoRepo.findAll(spec);
    }

    /**
     * Método para eliminar un producto
     * 
     * @param id el id del producto a eliminar
     */
    public void eliminarProducto(Long id) {
        Producto producto = productoRepo.findById(id)
            .orElseThrow(() -> new ProductoException.NoEncontrado(id));
        
        productoRepo.delete(producto);
    }

    /**
     * Método para obtener todos los productos
     * 
     * @return una lista de todos los productos
     */
    public List<Producto> obtenerTodos() {
        return productoRepo.findAll();
    }

    /**
     * Método para eliminar todos los productos
     */
    public void eliminarTodos() {
        productoRepo.deleteAll();
    }
}
