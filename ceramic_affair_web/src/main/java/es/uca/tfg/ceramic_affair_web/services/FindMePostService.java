package es.uca.tfg.ceramic_affair_web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.tfg.ceramic_affair_web.DTOs.FindMePostDTO;
import es.uca.tfg.ceramic_affair_web.DTOs.FindMePostMapper;
import es.uca.tfg.ceramic_affair_web.entities.FindMePost;
import es.uca.tfg.ceramic_affair_web.exceptions.FindMePostException;
import es.uca.tfg.ceramic_affair_web.repositories.FindMePostRepo;

/**
 * Servicio para la entidad FindMePost.
 * 
 * @version 1.0
 */
@Service
public class FindMePostService {

    @Autowired
    private FindMePostRepo findMePostRepo;

    /**
     * Método para insertar una nueva publicación "Encuéntrame".
     * 
     * @param findMePostDTO el DTO de la publicación "Encuéntrame" a insertar
     * @return el id de la publicación insertada
     */
    public Long insertarFindMePost(FindMePostDTO findMePostDTO) {
        FindMePost findMePost = FindMePostMapper.fromDTO(findMePostDTO);
        findMePostRepo.save(findMePost);
        return findMePost.getId();
    }

    /**
     * Método para modificar una publicación "Encuéntrame".
     * 
     * @param id el id de la publicación a modificar
     * @param findMePostDTO el DTO con los nuevos datos de la publicación
     * @throws FindMePostException.NoEncontrado si no se encuentra la publicación
     */
    public void modificarFindMePost(Long id, FindMePostDTO findMePostDTO) {
        FindMePost findMePost = findMePostRepo.findById(id)
                .orElseThrow(() -> new FindMePostException.NoEncontrado(id));
        findMePost.setTitulo(findMePostDTO.getTitulo());
        findMePost.setDescripcion(findMePostDTO.getDescripcion());
        findMePost.setFechaInicio(findMePostDTO.getFechaInicio());
        findMePost.setFechaFin(findMePostDTO.getFechaFin());
        findMePost.setLatitud(findMePostDTO.getLatitud());
        findMePost.setLongitud(findMePostDTO.getLongitud());

        findMePostRepo.save(findMePost);
    }

    /**
     * Método para obtener una publicación "Encuéntrame" por su id.
     * 
     * @param id el id de la publicación a buscar
     * @return el DTO de la publicación encontrada
     * @throws FindMePostException.NoEncontrado si no se encuentra la publicación
     */
    public FindMePostDTO obtenerPorId(Long id) {
        FindMePost findMePost = findMePostRepo.findById(id)
                .orElseThrow(() -> new FindMePostException.NoEncontrado(id));
        return FindMePostMapper.toDTO(findMePost);
    }

    /**
     * Método para eliminar una publicación "Encuéntrame" por su id.
     * 
     * @param id el id de la publicación a eliminar
     * @throws FindMePostException.NoEncontrado si no se encuentra la publicación
     */
    public void eliminarFindMePost(Long id) {
        FindMePost findMePost = findMePostRepo.findById(id)
                .orElseThrow(() -> new FindMePostException.NoEncontrado(id));
        findMePostRepo.delete(findMePost);
    }

    /**
     * Método para obtener todas las publicaciones "Encuéntrame".
     * 
     * @return una lista de DTOs de todas las publicaciones "Encuéntrame"
     */
    public List<FindMePostDTO> obtenerTodas() {
        return FindMePostMapper.toDTOList(findMePostRepo.findAllByOrderByFechaInicioDesc());
    }
}
