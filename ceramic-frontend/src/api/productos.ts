import { handleFetch } from "./utils";
import type { ProductoInputDTO, ProductoOutputDTO, ProductoStockDTO } from "../types/producto.types";
import type { PageResponse } from "./page.response";

const API_PUBLIC = 'http://localhost:8080/api/public/productos';
const API_ADMIN = 'http://localhost:8080/api/admin/productos';

// Funciones para manejar los productos

//------------------ PÚBLICOS ------------------//

export const getProductoById = async (id: number): Promise<ProductoOutputDTO> => {
    const response = await fetch(`${API_PUBLIC}/${id}`);

    return await handleFetch<ProductoOutputDTO>(response, 'Error al obtener el producto por ID');
};

export const getAllProductos = async (page = 0, size = 10): Promise<PageResponse<ProductoOutputDTO>> => {
    const response = await fetch(`${API_PUBLIC}/todos?page=${page}&size=${size}`);

    return await handleFetch<PageResponse<ProductoOutputDTO>>(response, 'Error al obtener todos los productos');
};

export interface FilterProductosParams {
    nombre?: string;
    categoria?: number;
    soloEnStock?: boolean;
    orden?: 'viejos' | 'nuevos'; // Ordenamiento por fecha de creación
}

export const filterProductos = async (params:FilterProductosParams & {page?: number, size?: number}): Promise<PageResponse<ProductoOutputDTO>> => {
    const query = new URLSearchParams();

    if (params.nombre) query.append('nombre', params.nombre);
    if (params.categoria !== undefined) query.append('categoria', params.categoria.toString());
    if (params.soloEnStock !== undefined) query.append('soloEnStock', params.soloEnStock.toString());
    if (params.orden) query.append('orden', params.orden);
    if (params.page !== undefined) query.append('page', params.page.toString());
    if (params.size !== undefined) query.append('size', params.size.toString());

    const response = await fetch(`${API_PUBLIC}/filtrar?${query.toString()}`);

    return await handleFetch<PageResponse<ProductoOutputDTO>>(response, 'Error al filtrar productos');
}

//------------------ ADMINISTRATIVOS ------------------//

export const newProducto = async (producto: ProductoInputDTO): Promise<number> => {
    const response = await fetch(`${API_ADMIN}/crear`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(producto),
    });

    return await handleFetch<number>(response, 'Error al crear producto');
}

export const updateProducto = async (id: number, producto: ProductoInputDTO): Promise<void> => {
    const response = await fetch(`${API_ADMIN}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(producto),
    });

    return await handleFetch<void>(response, 'Error al actualizar producto');
};

export const updateStockProducto = async (id: number, stock: ProductoStockDTO): Promise<void> => {
    const response = await fetch(`${API_ADMIN}/${id}/stock`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(stock),
    });

    return await handleFetch<void>(response, 'Error al actualizar stock del producto');
}

export const deleteProducto = async (id: number): Promise<void> => {
    const response = await fetch(`${API_ADMIN}/${id}`, {
        method: 'DELETE',
    });

    return await handleFetch<void>(response, 'Error al eliminar producto');
};

