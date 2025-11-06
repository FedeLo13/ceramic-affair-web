import type { NewsletterDTO } from "../types/newsletter.types";
import { fetchWithAuth, handleFetch } from "./utils";
import { API_BASE_ADMIN } from "./api";

const API_NEWSLETTER = `${API_BASE_ADMIN}/newsletter`;
const API_PLANTILLA = `${API_BASE_ADMIN}/plantilla`;

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

    return await handleFetch<string>(response, 'Error sending newsletter');
}

//------------------ PLANTILLAS ------------------//

export const getPlantilla = async (): Promise<NewsletterDTO> => {
    const response = await fetchWithAuth(`${API_PLANTILLA}/obtener`);

    return await handleFetch<NewsletterDTO>(response, 'Error obtaining template');
};

export const updatePlantilla = async (plantilla: NewsletterDTO): Promise<void> => {
    const response = await fetchWithAuth(`${API_PLANTILLA}/modificar`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(plantilla),
    });

    return await handleFetch<void>(response, 'Error updating template');
};