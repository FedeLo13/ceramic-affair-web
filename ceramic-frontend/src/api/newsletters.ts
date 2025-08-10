import type { NewsletterDTO } from "../types/newsletter.types";
import { fetchWithAuth, handleFetch } from "./utils";

const API_NEWSLETTER = 'http://localhost:8080/api/admin/newsletter';
const API_PLANTILLA = 'http://localhost:8080/api/admin/plantilla';

// Funciones para manejar newsletter y plantillas

//------------------ NEWSLETTER ------------------//

export const sendNewsletter = async (newsletter: NewsletterDTO): Promise<string> => {
    const response = await fetchWithAuth(`${API_NEWSLETTER}/enviar`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(newsletter),
    });

    return await handleFetch<string>(response, 'Error al enviar newsletter');
}

//------------------ PLANTILLAS ------------------//

export const getPlantilla = async (): Promise<NewsletterDTO> => {
    const response = await fetchWithAuth(`${API_PLANTILLA}/obtener`);

    return await handleFetch<NewsletterDTO>(response, 'Error al obtener plantilla');
};

export const updatePlantilla = async (plantilla: NewsletterDTO): Promise<void> => {
    const response = await fetchWithAuth(`${API_PLANTILLA}/modificar`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(plantilla),
    });

    return await handleFetch<void>(response, 'Error al actualizar plantilla');
};