export interface FindMePostInputDTO {
    titulo: string;
    descripcion: string;
    fechaInicio: string; // ISO string (Ejemplo: "2024-05-22T14:00:00")
    fechaFin: string;
    latitud: number;
    longitud: number;
}

export interface FindMePostOutputDTO {
    id: number;
    titulo: string;
    descripcion: string;
    fechaInicio: string;
    fechaFin: string;
    latitud: number;
    longitud: number;
}