import type { Producto } from "./producto.types";

export interface Categoria {
  id: number;
  nombre: string;
  productos: Producto[];
}

export interface CategoriaInputDTO {
  nombre: string;
}