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

export const unsubscribe = async (token: string): Promise<void> => {
    const response = await fetch(`${API_URL}/desuscribir?token=${token}`, {
        method: 'GET',
    });

    return await handleFetch<void>(response, 'Error al desuscribirse');
}

export const verifySubscription = async (token: string): Promise<void> => {
    const response = await fetch(`${API_URL}/verificar?token=${token}`, {
        method: 'GET',
    });

    return await handleFetch<void>(response, 'Error al verificar la suscripción');
};
