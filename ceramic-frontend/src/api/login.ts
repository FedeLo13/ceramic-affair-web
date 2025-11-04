import { handleFetch } from "./utils";
import type { LoginDTO, LoginResponse } from "../types/login.types";
import { API_BASE_PUBLIC } from "./api";

const API_URL = `${API_BASE_PUBLIC}/login`;

export const login = async (data: LoginDTO): Promise<LoginResponse> => {
    const response = await fetch(`${API_URL}/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });

    return await handleFetch<LoginResponse>(response, 'Error logging in');
};