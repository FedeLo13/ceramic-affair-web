import type { Producto } from "./producto.types";

export interface Categoria {
  id: number;
  nombre: string;
  productos: Producto[]; // Array de productos asociados a la categoría
}

export interface CategoriaInputDTO {
  nombre: string;
}