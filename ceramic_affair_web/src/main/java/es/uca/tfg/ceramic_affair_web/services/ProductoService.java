package es.uca.tfg.ceramic_affair_web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import es.uca.tfg.ceramic_affair_web.DTOs.ProductoDTO;
import es.uca.tfg.ceramic_affair_web.DTOs.ProductoMapper;
import es.uca.tfg.ceramic_affair_web.entities.Categoria;
import es.uca.tfg.ceramic_affair_web.entities.Imagen;
import es.uca.tfg.ceramic_affair_web.entities.Producto;
import es.uca.tfg.ceramic_affair_web.exceptions.CategoriaException;
import es.uca.tfg.ceramic_affair_web.exceptions.ProductoException;
import es.uca.tfg.ceramic_affair_web.repositories.CategoriaRepo;
import es.uca.tfg.ceramic_affair_web.repositories.ImagenRepo;
import es.uca.tfg.ceramic_affair_web.repositories.ProductoRepo;
import es.uca.tfg.ceramic_affair_web.repositories.ProductoSpecifications;

/**
 * Servicio para la entidad Producto.
 * 
 * @version 1.1
 */
@Service
public class ProductoService {

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private CategoriaRepo categoriaRepo;

    @Autowired
    private ImagenRepo imagenRepo;

    /**
     * Método para insertar un nuevo producto
     * 
     * @param productoDTO el DTO del producto a insertar
     * @return el id del producto insertado
     * @throws CategoriaException.NoEncontrada si la categoría no existe
     */
    public Long insertarProducto(ProductoDTO productoDTO) {
        // 1. Obtener la categoría por su id
        Categoria categoria = categoriaRepo.findById(productoDTO.getIdCategoria())
            .orElseThrow(() -> new CategoriaException.NoEncontrada(productoDTO.getIdCategoria()));

        // 2. Obtener las imágenes por sus ids
        List<Imagen> imagenes = productoDTO.getIdsImagenes() != null 
            ? imagenRepo.findAllById(productoDTO.getIdsImagenes()) 
            : List.of();

        // 3. Crear el producto
        Producto producto = ProductoMapper.fromDTO(productoDTO, categoria, imagenes);

        // 4. Guardar el producto y devolver su id
        productoRepo.save(producto);
        return producto.getId();
    }

    /**
     * Método para modificar un producto
     * 
     * @param id el id del producto a modificar
     * @param productoDTO el DTO del producto con los nuevos datos
     * @throws ProductoException.NoEncontrado si no se encuentra el producto
     * @throws CategoriaException.NoEncontrada si la categoría no existe
     */
    public void modificarProducto(Long id, ProductoDTO productoDTO) {
        // 1. Obtener el producto por su id
        Producto producto = productoRepo.findById(id)
            .orElseThrow(() -> new ProductoException.NoEncontrado(id));

        // 2. Actualizar los campos del producto
        Categoria categoria = categoriaRepo.findById(productoDTO.getIdCategoria())
            .orElseThrow(() -> new CategoriaException.NoEncontrada(productoDTO.getIdCategoria()));

        List<Imagen> imagenes = productoDTO.getIdsImagenes() != null 
            ? imagenRepo.findAllById(productoDTO.getIdsImagenes()) 
            : List.of();

        producto.setNombre(productoDTO.getNombre());
        producto.setCategoria(categoria);
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setAltura(productoDTO.getAltura());
        producto.setAnchura(productoDTO.getAnchura());
        producto.setDiametro(productoDTO.getDiametro());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setSoldOut(productoDTO.isSoldOut());
        producto.setImagenes(imagenes);

        // 3. Guardar el producto modificado
        productoRepo.save(producto);
    }

    /**
     * Método para establecer el stock de un producto
     * 
     * @param id el id del producto a modificar
     * @param soldOut true si el producto está agotado, false si está en stock
     * @throws ProductoException.NoEncontrado si no se encuentra el producto
     */
    public void establecerStock(Long id, boolean soldOut) {
        // 1. Obtener el producto por su id
        Producto producto = productoRepo.findById(id)
            .orElseThrow(() -> new ProductoException.NoEncontrado(id));

        // 2. Actualizar el estado de stock del producto
        producto.setSoldOut(soldOut);

        // 3. Guardar el producto modificado
        productoRepo.save(producto);
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
     * @param pageable objeto Pageable para la paginación
     * @return una lista de productos que cumplen con los filtros
     */
    public Page<Producto> filtrarProductos(String nombre, Long categoria, Boolean soloEnStock, String orden, Pageable pageable) {
        Specification<Producto> spec = Specification
            .where(ProductoSpecifications.nombreLike(nombre))
            .and(ProductoSpecifications.conCategoria(categoria))
            .and(ProductoSpecifications.enStock(soloEnStock))
            .and(ProductoSpecifications.ordenarPorFecha(orden));

        return productoRepo.findAll(spec, pageable);
    }

    /**
     * Método para eliminar un producto
     * 
     * @param id el id del producto a eliminar
     * @throws ProductoException.NoEncontrado si no se encuentra el producto
     */
    public void eliminarProducto(Long id) {
        Producto producto = productoRepo.findById(id)
            .orElseThrow(() -> new ProductoException.NoEncontrado(id));
        
        productoRepo.delete(producto);
    }

    /**
     * Método para obtener todos los productos
     * 
     * @param pageable objeto Pageable para la paginación
     * @return una lista de todos los productos
     */ 
    public Page<Producto> obtenerTodos(Pageable pageable) {
        return productoRepo.findAll(pageable);
    }
}
