import type { ContactoFormDTO } from "../types/contactoform.types";
import { handleFetch } from "./utils";

const API_URL = 'http://localhost:8080/api/public/contacto';

export const sendContactoForm = async (form: ContactoFormDTO): Promise<void> => {
    const response = await fetch(`${API_URL}/enviar`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(form),
    });

    return await handleFetch<void>(response, 'Error sending contact form');
};