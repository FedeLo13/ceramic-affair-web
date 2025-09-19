// Funciones de utilidad para manejar errores y respuestas de la API

import type { ApiError } from "./api.error";
import type { ApiResponse } from "./api.response";

export class ValidationError extends Error {
    public validationErrors: Record<string, string>;

    constructor(message: string, validationErrors: Record<string, string>) {
        super(message);
        this.name = 'ValidationError';
        this.validationErrors = validationErrors;
    }
}

export const fetchWithAuth = async (url: string, options: RequestInit = {}) => {
    const token = localStorage.getItem('token');
    
    const headers= {
        ...options.headers,
        'Authorization': token ? `Bearer ${token}` : '',
    };

    return fetch(url, { ...options, headers });
}

export const handleFetch = async <T>(response: Response, errorPrefix = 'Error'): Promise<T> => {

    const text = await response.text();
    const data = text ? JSON.parse(text) : null;

    if (!response.ok) {
        const apiError = data as ApiError;

        // Si el error es de validación, lanzamos un ValidationError
        if (apiError.validationErrors) {
            throw new ValidationError(
                `${errorPrefix}: ${apiError.message}`,
                apiError.validationErrors
            );
        }

        // Si no es un error de validación, lanzamos un Error genérico
        throw new Error(`${errorPrefix}: ${apiError.message || response.statusText}`);
    }

    // Manejar 204 No Content
    if (response.status === 204) {
        return null as unknown as T;
    }

    // Cualquier otra respuesta exitosa se espera que sea un ApiResponse
    const apiResponse = data as ApiResponse<T>;
    if (!apiResponse.success) {
        throw new Error(`${errorPrefix}: ${apiResponse.message}`);
    }
    return apiResponse.data;
};