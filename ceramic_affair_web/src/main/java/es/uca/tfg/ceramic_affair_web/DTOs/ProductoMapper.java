package es.uca.tfg.ceramic_affair_web.DTOs;

import java.util.List;
import java.util.stream.Collectors;

import es.uca.tfg.ceramic_affair_web.entities.Categoria;
import es.uca.tfg.ceramic_affair_web.entities.Imagen;
import es.uca.tfg.ceramic_affair_web.entities.Producto;

/**
 * Mapper para convertir entre Producto y ProductoDTO.
 * Este mapper se utiliza para transformar entidades Producto a DTOs y viceversa,
 * facilitando la transferencia de datos entre la capa de servicio y la capa de presentación.
 * 
 * @version 1.0
 */
public class ProductoMapper {

    public static ProductoDTO toDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        List<Long> idsImagenes = producto.getImagenes() != null ? 
            producto.getImagenes().stream().map(Imagen::getId).collect(Collectors.toList())
            : null;
        
        return new ProductoDTO(
            producto.getId(),
            producto.getNombre(),
            producto.getCategoria() != null ? producto.getCategoria().getId() : null,
            producto.getDescripcion(),
            producto.getAltura(),
            producto.getAnchura(),
            producto.getDiametro(),
            producto.getPrecio(),
            producto.isSoldOut(),
            idsImagenes
        );
    }

    public static Producto fromDTO(ProductoDTO dto, Categoria categoria, List<Imagen> imagenes) {
        if (dto == null) {
            return null;
        }

        return new Producto(
            dto.getNombre(),
            categoria,
            dto.getDescripcion(),
            dto.getAltura(),
            dto.getAnchura(),
            dto.getDiametro(),
            dto.getPrecio(),
            dto.isSoldOut(),
            imagenes
        );
    }

    public static List<ProductoDTO> toDTOList(List<Producto> productos) {
        if (productos == null) {
            return null;
        }
        return productos.stream().map(ProductoMapper::toDTO).collect(Collectors.toList());
    }
}
