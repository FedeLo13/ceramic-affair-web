import { fetchWithAuth, handleFetch } from "./utils";
import type { Categoria, CategoriaInputDTO } from "../types/categoria.types";
import { API_BASE_ADMIN, API_BASE_PUBLIC } from "./api";

const API_PUBLIC = `${API_BASE_PUBLIC}/categorias`;
const API_ADMIN = `${API_BASE_ADMIN}/categorias`;

// Funciones para manejar las categorías

//------------------ PÚBLICOS ------------------//

export const getCategoriaById = async (id: number): Promise<Categoria> => {
    const response = await fetch(`${API_PUBLIC}/${id}`);

    return await handleFetch<Categoria>(response, 'Error obtaining category by ID');
};

export const getAllCategorias = async (): Promise<Categoria[]> => {
    const response = await fetch(`${API_PUBLIC}/todas`);

    return await handleFetch<Categoria[]>(response, 'Error obtaining categories');
};

//------------------ ADMINISTRATIVOS ------------------//

export const newCategoria = async (categoria: CategoriaInputDTO): Promise<number> => {
    const response = await fetchWithAuth(`${API_ADMIN}/crear`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },  
        body: JSON.stringify(categoria),
    });

    return await handleFetch<number>(response, 'Error creating category');
};

export const updateCategoria = async (id: number, categoria: CategoriaInputDTO): Promise<void> => {
    const response = await fetchWithAuth(`${API_ADMIN}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(categoria),
    });

    return await handleFetch<void>(response, 'Error updating category');
};

export const deleteCategoria = async (id: number): Promise<void> => {
    const response = await fetchWithAuth(`${API_ADMIN}/${id}`, {
        method: 'DELETE',
    });
    return await handleFetch<void>(response, 'Error deleting category');
};