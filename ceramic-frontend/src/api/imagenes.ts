import type { Imagen } from "../types/imagen.types";
import { handleFetch } from "./utils";

const API_PUBLIC = 'http://localhost:8080/api/public/imagenes';
const API_ADMIN = 'http://localhost:8080/api/admin/imagenes';

// Funciones para manejar las imágenes

//------------------ PÚBLICOS ------------------//

export const getImagenById = async (id: number): Promise<Imagen> => {
    const response = await fetch(`${API_PUBLIC}/${id}`);

    return await handleFetch<Imagen>(response, 'Error al obtener la imagen por ID');
};

//------------------ ADMINISTRATIVOS ------------------//

export const newImagen = async (archivo: File): Promise<Imagen> => {
    const formData = new FormData();
    formData.append('archivo', archivo);

    const response = await fetch(`${API_ADMIN}/crear`, {
        method: 'POST',
        body: formData,
    });

    return await handleFetch<Imagen>(response, 'Error al crear imagen');
};

export const deleteImagen = async (id: number): Promise<void> => {
    const response = await fetch(`${API_ADMIN}/${id}`, {
        method: 'DELETE',
    });

    return await handleFetch<void>(response, 'Error al eliminar imagen');
};
