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
  fechaCreacion: string; // ISO string (Ejemplo: "2024-05-22T14:00:00")
  imagenes: Imagen[];
}

export interface ProductoInputDTO {
  nombre: string;
  descripcion: string;
  precio: number;
  soldOut?: boolean;
  idCategoria: number;
  altura: number;
  anchura: number;
  diametro: number;
  idsImagenes: number[];
}

export interface ProductoOutputDTO {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  soldOut?: boolean;
  idCategoria: number;
  nombreCategoria: string;
  altura: number;
  anchura: number;
  diametro: number;
  idsImagenes: number[];
}

export interface ProductoStockDTO {
  soldOut: boolean;
}
