import { handleFetch } from "./utils";
import type { Producto, ProductoInput, ProductoCreateDTO } from "../types/producto.types";
const API_URL = 'http://localhost:8080/api/productos';

// Funciones para manejar los productos

//------------------ CREAR ------------------//

export const newProducto = async (producto: ProductoInput): Promise<Producto> => {
    const productoDTO: ProductoCreateDTO = {
        nombre: producto.nombre,
        descripcion: producto.descripcion,
        precio: producto.precio,
    };

    const response = await fetch(`${API_URL}/crear`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(productoDTO),
    });

    return await handleFetch(response, 'Error creating product');
}

//------------------ LEER ------------------//

export const getProductoById = async (id: number): Promise<Producto> => {
    const response = await fetch(`${API_URL}/${id}`);

    return await handleFetch(response, 'Error fetching product by ID');
}

export const getAllProductos = async (): Promise<Producto[]> => {
    const response = await fetch(`${API_URL}/todas`);

    return await handleFetch(response, 'Error fetching all products');
}

//------------------ FILTRAR ------------------//

export interface FilterProductosParams {
    nombre?: string;
    categoria?: number;
    soloEnStock?: boolean;
    orden?: 'viejos' | 'nuevos'; // Ordenamiento por fecha de creación
}

export const filterProductos = async ({ nombre, categoria, soloEnStock, orden}:FilterProductosParams): Promise<Producto[]> => {
    const query = new URLSearchParams();

    if (nombre) query.append('nombre', nombre);
    if (categoria !== undefined) query.append('categoria', categoria.toString());
    if (soloEnStock !== undefined) query.append('soloEnStock', soloEnStock.toString());
    if (orden) query.append('orden', orden);

    const response = await fetch(`${API_URL}/filtrar?${query.toString()}`);

    return await handleFetch(response, 'Error filtering products');
}

//------------------ ELIMINAR ------------------//

export const deleteProducto = async (id: number): Promise<void> => {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE',
    });

    if (response.status === 204) return;

    return await handleFetch(response, 'Error deleting product');
};

export const deleteAllProductos = async (): Promise<void> => {
    const response = await fetch(`${API_URL}/eliminarTodas`, {
        method: 'DELETE',
    });

    if (response.status === 204) return;

    return await handleFetch(response, 'Error deleting all products');
}

