export interface ApiError {
    timestamp: string; // ISO string (e.g., "2024-05-22T14:00:00")
    status: number;
    error: string;
    message: string;
    path: string;
    validationErrors?: Record<string, string>; // Optional field for validation errors
}