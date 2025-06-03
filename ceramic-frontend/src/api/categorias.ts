import { handleFetch } from "./utils";
import type { Categoria, CategoriaInput } from "../types/categoria.types";


const API_URL = 'http://localhost:8080/api/categorias';

// Funciones para manejar las categorías

//------------------ CREAR ------------------//

export const newCategoria = async (categoria: CategoriaInput): Promise<Categoria> => {
    const response = await fetch(`${API_URL}/crear`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },  
        body: JSON.stringify(categoria),
    });

    return await handleFetch(response, 'Error creating category');
};

//------------------ LEER ------------------//

export const getCategoriaById = async (id: number): Promise<Categoria> => {
    const response = await fetch(`${API_URL}/${id}`);

    return await handleFetch(response, 'Error fetching category by ID');
};

export const getAllCategorias = async (): Promise<Categoria[]> => {
    const response = await fetch(`${API_URL}/todas`);

    return await handleFetch(response, 'Error fetching all categories');
};

//------------------ ELIMINAR ------------------//

export const deleteCategoria = async (id: number): Promise<void> => {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE',
    });

    if (response.status === 204) return;

    return await handleFetch(response, 'Error deleting category');
};

export const deleteAllCategorias = async (): Promise<void> => {
    const response = await fetch(`${API_URL}/eliminarTodas`, {
        method: 'DELETE',
    });

    if (response.status === 204) return;

    return await handleFetch(response, 'Error deleting all categories');
}