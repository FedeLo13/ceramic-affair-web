import type { ContactoFormDTO } from "../types/contactoform.types";
import { handleFetch } from "./utils";
import { API_BASE_PUBLIC } from "./api";

const API_URL = `${API_BASE_PUBLIC}/contacto`;

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