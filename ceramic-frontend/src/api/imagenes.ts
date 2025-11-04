import type { Imagen } from "../types/imagen.types";
import { fetchWithAuth, handleFetch } from "./utils";
import { API_BASE_ADMIN, API_BASE_PUBLIC } from "./api";

const API_PUBLIC = `${API_BASE_PUBLIC}/imagenes`;
const API_ADMIN = `${API_BASE_ADMIN}/imagenes`;

// Funciones para manejar las imágenes

//------------------ PÚBLICOS ------------------//

export const getImagenById = async (id: number): Promise<Imagen> => {
    const response = await fetch(`${API_PUBLIC}/${id}`);

    return await handleFetch<Imagen>(response, 'Error obtaining image by ID');
};

//------------------ ADMINISTRATIVOS ------------------//

export const newImagen = async (archivo: File): Promise<Imagen> => {
    const formData = new FormData();
    formData.append('archivo', archivo);

    const response = await fetchWithAuth(`${API_ADMIN}/crear`, {
        method: 'POST',
        body: formData,
    });

    return await handleFetch<Imagen>(response, 'Error creating image');
};

export const deleteImagen = async (id: number): Promise<void> => {
    const response = await fetchWithAuth(`${API_ADMIN}/${id}`, {
        method: 'DELETE',
    });

    return await handleFetch<void>(response, 'Error deleting image');
};
