// Funciones de utilidad para manejar errores y respuestas de la API

export const handleFetch = async <T>(response: Response, errorPrefix = 'Error'): Promise<T> => {
    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`${errorPrefix}: ${errorMessage}`);
    }
    return await response.json() as T;
};