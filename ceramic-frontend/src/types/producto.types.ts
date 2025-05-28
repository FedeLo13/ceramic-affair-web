import type { Categoria } from "./categoria.types";

export interface Producto {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  soldOut: boolean;
  categoria: Categoria | null;
  altura: number;
  anchura: number;
  diametro: number;
  fechaCreacion: string; // ISO string (ej. "2024-05-22T14:00:00")
  imagenes: string[];
}

export interface ProductoInput {
  nombre: string;
  descripcion: string;
  precio: number;
  soldOut?: boolean;
  categoriaId: number;
  altura: number;
  anchura: number;
  diametro: number;
  imagenes: string[];
}

export interface ProductoCreateDTO {
  nombre: string;
  descripcion: string;
  precio: number;
}
