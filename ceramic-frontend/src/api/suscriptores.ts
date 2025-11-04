import { handleFetch } from "./utils";
import type { SuscripcionDTO } from "../types/suscripcion.types";
import { API_BASE_PUBLIC } from "./api";

const API_URL = `${API_BASE_PUBLIC}/suscriptores`;

export const subscribe = async (suscripcion: SuscripcionDTO): Promise<string> => {
    const response = await fetch(`${API_URL}/suscribir`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(suscripcion),
    });

    return await handleFetch<string>(response, 'Error subscribing');
}
