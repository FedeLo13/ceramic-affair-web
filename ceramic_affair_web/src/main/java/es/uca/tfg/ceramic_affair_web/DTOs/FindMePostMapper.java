package es.uca.tfg.ceramic_affair_web.DTOs;

import java.util.List;
import java.util.stream.Collectors;

import es.uca.tfg.ceramic_affair_web.entities.FindMePost;

/**
 * Mapper para convertir entre FindMePost y FindMePostDTO.
 * Este mapper se utiliza para transformar entidades FindMePost a DTOs y viceversa,
 * facilitando la transferencia de datos entre la capa de servicio y la capa de presentación.
 * 
 * @version 1.0
 */
public class FindMePostMapper {

    public static FindMePostDTO toDTO(FindMePost findMePost) {
        if (findMePost == null) {
            return null;
        }
        return new FindMePostDTO(
            findMePost.getId(),
            findMePost.getTitulo(),
            findMePost.getDescripcion(),
            findMePost.getFechaInicio(),
            findMePost.getFechaFin(),
            findMePost.getLatitud(),
            findMePost.getLongitud()
        );
    }

    public static FindMePost fromDTO(FindMePostDTO dto) {
        if (dto == null) {
            return null;
        }
        return new FindMePost(
            dto.getTitulo(),
            dto.getDescripcion(),
            dto.getFechaInicio(),
            dto.getFechaFin(),
            dto.getLatitud(),
            dto.getLongitud()
        );
    }

    public static List<FindMePostDTO> toDTOList(List<FindMePost> findMePosts) {
        if (findMePosts == null) {
            return null;
        }
        return findMePosts.stream().map(FindMePostMapper::toDTO).collect(Collectors.toList());
    }
}
