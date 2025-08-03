import type { Imagen } from "./imagen.types";

export interface Producto {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  soldOut: boolean;
  categoria: string
  altura: number;
  anchura: number;
  diametro: number;
  fechaCreacion: string; // ISO string (ej. "2024-05-22T14:00:00")
  imagenes: Imagen[]; // Array de objetos Imagen
}

export interface ProductoInputDTO {
  nombre: string;
  descripcion: string;
  precio: number;
  soldOut?: boolean;
  categoriaId: number;
  altura: number;
  anchura: number;
  diametro: number;
  imagenes: number[];
}

export interface ProductoOutputDTO {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  soldOut?: boolean;
  categoriaId: number;
  categoriaNombre: string;
  altura: number;
  anchura: number;
  diametro: number;
  imagenes: number[];
}

export interface ProductoStockDTO {
  soldOut: boolean;
}
