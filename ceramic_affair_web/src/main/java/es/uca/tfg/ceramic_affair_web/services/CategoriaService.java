package es.uca.tfg.ceramic_affair_web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.tfg.ceramic_affair_web.entities.Categoria;
import es.uca.tfg.ceramic_affair_web.entities.Producto;
import es.uca.tfg.ceramic_affair_web.exceptions.CategoriaException;
import es.uca.tfg.ceramic_affair_web.repositories.CategoriaRepo;
import es.uca.tfg.ceramic_affair_web.repositories.ProductoRepo;

/**
 * Servicio para la entidad Categoria.
 * 
 * @version 1.0
 */
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepo categoriaRepo;

    @Autowired
    private ProductoRepo productoRepo;

    /**
     * Método para insertar una nueva categoría
     * 
     * @param categoria la categoría a insertar
     */
    public void insertarCategoria(String nombre) {
        Categoria categoria = new Categoria(nombre);
        categoriaRepo.save(categoria);
    }

    /**
     * Método para obtener una categoría por su id
     * 
     * @param id el id de la categoría a obtener
     * @return la categoría con el id especificado
     * @throws CategoriaException.NoEncontrada si no se encuentra la categoría
     */
    public Categoria obtenerPorId(Long id) {
        return categoriaRepo.findById(id)
            .orElseThrow(() -> new CategoriaException.NoEncontrada(id));
    }

    /**
     * Método para eliminar una categoría
     * 
     * @param id el id de la categoría a eliminar
     */
    public void eliminarCategoria(Long id) {
        Categoria categoria = categoriaRepo.findById(id)
            .orElseThrow(() -> new CategoriaException.NoEncontrada(id));
        
        // Desvincular todos los productos de la categoría antes de eliminarla
        categoria.getProductos().forEach(producto -> {
            producto.setCategoria(null);
            productoRepo.save(producto);
        });
        // Eliminar la categoría
        categoriaRepo.delete(categoria);
    }

    /**
     * Método para obtener todas las categorías
     * 
     * @return una lista de todas las categorías
     */
    public List<Categoria> obtenerTodas() {
        return categoriaRepo.findAll();
    }

    /**
     * Método para eliminar todas las categorías
     */
    public void eliminarTodas() {
        List<Producto> productos = productoRepo.findAll();

        // Desvincular todos los productos de las categorías antes de eliminar
        for(Producto producto : productos) {
            if(producto.getCategoria() != null) {
                producto.setCategoria(null);
                productoRepo.save(producto);
            }
        }

        categoriaRepo.deleteAll();
    }
}
