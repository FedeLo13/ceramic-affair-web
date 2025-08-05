import type { FindMePostInputDTO, FindMePostOutputDTO } from "../types/findmepost.types";
import { fetchWithAuth, handleFetch } from "./utils";

const API_PUBLIC = 'http://localhost:8080/api/public/find-me-posts';
const API_ADMIN = 'http://localhost:8080/api/admin/find-me-posts';

// Funciones para manejar los posts de "Encuéntrame"

//------------------ PÚBLICOS ------------------//

export const getFindMePostById = async (id: number): Promise<FindMePostOutputDTO> => {
    const response = await fetch(`${API_PUBLIC}/${id}`);

    return await handleFetch<FindMePostOutputDTO>(response, 'Error al obtener post por ID');
};

export const getAllFindMePosts = async (): Promise<FindMePostOutputDTO[]> => {
    const response = await fetch(`${API_PUBLIC}/todos`);

    return await handleFetch<FindMePostOutputDTO[]>(response, 'Error al obtener todos los posts');
};

//------------------ ADMINISTRATIVOS ------------------//

export const newFindMePost = async (post: FindMePostInputDTO): Promise<number> => {
    const response = await fetchWithAuth(`${API_ADMIN}/crear`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(post),
    });

    return await handleFetch<number>(response, 'Error al crear post');
};

export const updateFindMePost = async (id: number, post: FindMePostInputDTO): Promise<void> => {
    const response = await fetchWithAuth(`${API_ADMIN}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(post),
    });

    return await handleFetch<void>(response, 'Error al actualizar post');
};

export const deleteFindMePost = async (id: number): Promise<void> => {
    const response = await fetchWithAuth(`${API_ADMIN}/${id}`, {
        method: 'DELETE',
    });

    return await handleFetch<void>(response, 'Error al eliminar post');
};
