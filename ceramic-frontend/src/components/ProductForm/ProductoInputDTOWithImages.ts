import type { ProductoInputDTO } from "../../types/producto.types";

export interface ProductoInputDTOWithImages extends ProductoInputDTO {
    localImages: File[];
}