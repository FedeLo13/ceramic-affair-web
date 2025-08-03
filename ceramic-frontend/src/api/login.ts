import { handleFetch } from "./utils";
import type { LoginDTO, LoginResponse } from "../types/login.types";

const API_URL = 'http://localhost:8080/api/public/login';

export const login = async (data: LoginDTO): Promise<LoginResponse> => {
    const response = await fetch(`${API_URL}/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });

    return await handleFetch<LoginResponse>(response, 'Error al iniciar sesión');
};