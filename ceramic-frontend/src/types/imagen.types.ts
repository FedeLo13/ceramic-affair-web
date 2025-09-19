export interface Imagen {
    id: number;
    ruta: string; // Ruta de la imagen en el servidor
    formato: string; // png, jpg, etc.
    tamano: number; // Tamaño en bytes
    ancho: number; // Ancho de la imagen en píxeles
    alto: number; // Alto de la imagen en píxeles
}