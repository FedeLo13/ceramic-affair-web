package es.uca.tfg.ceramic_affair_web.repositories;

import org.springframework.data.jpa.domain.Specification;

import es.uca.tfg.ceramic_affair_web.entities.Producto;

/**
 * Clase que contiene las especificaciones para la entidad Producto.
 * Se utiliza para construir consultas dinámicas y complejas en la base de datos.
 * 
 * @version 1.0
 */
public class ProductoSpecifications {
    
    /**
     * Especificación para filtrar productos por categoría.
     * 
     * @param categoriaId el ID de la categoría (null si no se desea filtrar por categoría)
     * @return una especificación que filtra productos por categoría
     */
    public static Specification<Producto> conCategoria(Long categoriaId) {
        return (root, query, criteriaBuilder) -> categoriaId == null
            ? null
            : criteriaBuilder.equal(root.get("categoria").get("id"), categoriaId);
    }

    /**
     * Especificación para filtrar productos en stock
     * 
     * @param soloEnStock true si se desean solo productos en stock, false/null si no se desea filtrar por stock
     * @return una especificación que filtra productos en stock
     */
    public static Specification<Producto> enStock(Boolean soloEnStock) {
        return (root, query, criteriaBuilder) -> (soloEnStock != null && soloEnStock)
        ? criteriaBuilder.isFalse(root.get("soldOut"))
        : null;
    }

    /**
     * Especificación para ordenar productos por fecha de creación.
     * 
     * @param orden el orden "viejos" para mas antiguos primero, o cualquier otro valor para más recientes primero
     * @return null, ya que el ordenamiento no requiere un predicado
     */
    public static Specification<Producto> ordenarPorFecha(String orden) {
        return (root, query, criteriaBuilder) -> {
            if ("viejos".equalsIgnoreCase(orden)) {
                query.orderBy(criteriaBuilder.asc(root.get("fechaCreacion")));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get("fechaCreacion")));
            }
            return null; // No se necesita un predicado para el ordenamiento
        };
    }

    /**
     * Especificación para filtrar productos por nombre.
     * 
     * @param nombre el nombre del producto (null o vacío si no se desea filtrar por nombre)
     * @return una especificación que filtra productos por nombre
     */
    public static Specification<Producto> nombreLike(String nombre) {
        return (root, query, criteriaBuilder) -> {
            if (nombre != null && !nombre.isEmpty()) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
            } else {
                return null; // No se aplica filtro si el nombre es nulo o vacío
            }
        };
    }
}
