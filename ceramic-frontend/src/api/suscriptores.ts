import { handleFetch } from "./utils";
import type { SuscripcionDTO } from "../types/suscripcion.types";

const API_URL = 'http://localhost:8080/api/public/suscriptores';

export const subscribe = async (suscripcion: SuscripcionDTO): Promise<string> => {
    const response = await fetch(`${API_URL}/suscribir`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(suscripcion),
    });

    return await handleFetch<string>(response, 'Error al suscribirse');
}
