import type { FindMePostInputDTO, FindMePostOutputDTO } from "../types/findmepost.types";
import { fetchWithAuth, handleFetch } from "./utils";
import { API_BASE_ADMIN, API_BASE_PUBLIC } from "./api";

const API_PUBLIC = `${API_BASE_PUBLIC}/find-me-posts`;
const API_ADMIN = `${API_BASE_ADMIN}/find-me-posts`;

// Funciones para manejar los posts de "Encuéntrame"

//------------------ PÚBLICOS ------------------//

export const getFindMePostById = async (id: number): Promise<FindMePostOutputDTO> => {
    const response = await fetch(`${API_PUBLIC}/${id}`);

    return await handleFetch<FindMePostOutputDTO>(response, 'Error obtaining post by ID');
};

export const getAllFindMePosts = async (): Promise<FindMePostOutputDTO[]> => {
    const response = await fetch(`${API_PUBLIC}/todos`);

    return await handleFetch<FindMePostOutputDTO[]>(response, 'Error obtaining all posts');
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

    return await handleFetch<number>(response, 'Error creating post');
};

export const updateFindMePost = async (id: number, post: FindMePostInputDTO): Promise<void> => {
    const response = await fetchWithAuth(`${API_ADMIN}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(post),
    });

    return await handleFetch<void>(response, 'Error updating post');
};

export const deleteFindMePost = async (id: number): Promise<void> => {
    const response = await fetchWithAuth(`${API_ADMIN}/${id}`, {
        method: 'DELETE',
    });

    return await handleFetch<void>(response, 'Error deleting post');
};
