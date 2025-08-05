import { fetchWithAuth, handleFetch } from "./utils";
import type { Categoria, CategoriaInputDTO } from "../types/categoria.types";

const API_PUBLIC = 'http://localhost:8080/api/public/categorias';
const API_ADMIN = 'http://localhost:8080/api/admin/categorias';

// Funciones para manejar las categorías

//------------------ PÚBLICOS ------------------//

export const getCategoriaById = async (id: number): Promise<Categoria> => {
    const response = await fetch(`${API_PUBLIC}/${id}`);

    return await handleFetch<Categoria>(response, 'Error al obtener categoría por ID');
};

export const getAllCategorias = async (): Promise<Categoria[]> => {
    const response = await fetch(`${API_PUBLIC}/todas`);

    return await handleFetch<Categoria[]>(response, 'Error al obtener todas las categorías');
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

    return await handleFetch<number>(response, 'Error al crear categoría');
};

export const updateCategoria = async (id: number, categoria: CategoriaInputDTO): Promise<void> => {
    const response = await fetchWithAuth(`${API_ADMIN}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(categoria),
    });

    return await handleFetch<void>(response, 'Error al actualizar categoría');
};

export const deleteCategoria = async (id: number): Promise<void> => {
    const response = await fetchWithAuth(`${API_ADMIN}/${id}`, {
        method: 'DELETE',
    });
    return await handleFetch<void>(response, 'Error al eliminar categoría');
};