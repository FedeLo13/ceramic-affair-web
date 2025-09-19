export interface FindMePostInputDTO {
    titulo: string;
    descripcion: string;
    fechaInicio: string; // ISO string (ej. "2024-05-22T14:00:00")
    fechaFin: string; // ISO string (ej. "2024-05-22T14:00:00")
    latitud: number;
    longitud: number;
}

export interface FindMePostOutputDTO {
    id: number;
    titulo: string;
    descripcion: string;
    fechaInicio: string; // ISO string (ej. "2024-05-22T14:00:00")
    fechaFin: string; // ISO string (ej. "2024-05-22T14:00:00")
    latitud: number;
    longitud: number;
}