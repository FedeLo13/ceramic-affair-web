package es.uca.tfg.ceramic_affair_web.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import es.uca.tfg.ceramic_affair_web.entities.Imagen;
import es.uca.tfg.ceramic_affair_web.exceptions.ImagenException;
import es.uca.tfg.ceramic_affair_web.repositories.ImagenRepo;
import jakarta.transaction.Transactional;

/**
 * Servicio para la entidad Imagen.
 * 
 * @version 1.0
 */
public class ImagenService {


    private ImagenRepo imagenRepo;
    private final Path imagenesPath;

    public ImagenService(ImagenRepo imagenRepo, @Value("${ceramic.affair.images.path}") String imagenesPath) throws IOException {
        this.imagenRepo = imagenRepo;
        this.imagenesPath = Paths.get(imagenesPath).toAbsolutePath().normalize();
        Files.createDirectories(this.imagenesPath); // Asegura que el directorio existe
    }

    /**
     * Método para insertar una nueva imagen en el sistema.
     * 
     * @param archivo el archivo de imagen a insertar
     * @return la entidad Imagen creada y almacenada en la base de datos
     * @throws IOException si ocurre un error al guardar el archivo
     * @throws ImagenException.NoValida si el archivo no es una imagen válida o no cumple con los requisitos
     */
    public Imagen insertarImagen(MultipartFile archivo) throws IOException {
        // 1. Validar tipo MIME del archivo y que se pueda tratar como imagen
        String tipoMime = archivo.getContentType();
        if (tipoMime == null || !tipoMime.startsWith("image/")) {
            throw new ImagenException.NoValida("El archivo no es válido.");
        }

        // 2. Validar extensiones permitidas
        String nombreOriginal = archivo.getOriginalFilename();
        if (nombreOriginal == null || !nombreOriginal.contains(".")) {
            throw new ImagenException.NoValida("El archivo no es válido.");
        }

        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf('.') + 1).toLowerCase();
        List<String> extensionesPermitidas = List.of("jpg", "jpeg", "png", "gif", "webp");

        if (!extensionesPermitidas.contains(extension)) {
            throw new ImagenException.NoValida("El archivo no es válido.");
        }

        // 3. Leer imagen para asegurarse de que es válida y obtener sus dimensiones
        BufferedImage img = ImageIO.read(archivo.getInputStream());
        if (img == null) {
            throw new ImagenException.NoValida("El archivo no es válido.");
        }

        float ancho = img.getWidth();
        float alto = img.getHeight();
        float tamano = (float) archivo.getSize();

        // 4. Generar un nombre único para la imagen y guardarla
        String nombreImagen = UUID.randomUUID().toString() + "." + extension;
        Path rutaImagen = imagenesPath.resolve(nombreImagen);
        archivo.transferTo(rutaImagen.toFile());

        Imagen imagen = new Imagen(nombreImagen, extension, tamano, ancho, alto);
        return imagenRepo.save(imagen);
    }

    /**
     * Método para obtener una imagen por su ID.
     * 
     * @param id el ID de la imagen a buscar
     * @return la entidad Imagen con el id especificado
     * @throws ImagenException.NoEncontrada si no se encuentra la imagen con el id especificado
     */
    public Imagen obtenerPorId(Long id) {
        return imagenRepo.findById(id)
            .orElseThrow(() -> new ImagenException.NoEncontrada(id));
    }

    /**
     * Método para eliminar una imagen por su ID.
     * 
     * @param id el ID de la imagen a eliminar
     * @throws IOException si ocurre un error al eliminar el archivo de imagen del sistema de archivos
     * @throws ImagenException.NoEncontrada si no se encuentra la imagen con el id especificado
     */
    @Transactional
    public void eliminarImagen(Long id) throws IOException {
        Imagen imagen = obtenerPorId(id);
        
        // 1. Eliminar el archivo de imagen del sistema de archivos
        Path rutaImagen = imagenesPath.resolve(imagen.getRuta());
        Files.deleteIfExists(rutaImagen);

        // 2. Eliminar la entidad Imagen de la base de datos
        imagenRepo.delete(imagen);
    }
}
