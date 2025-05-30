import ProductCard from "../ProductCard/ProductCard";
import type { Producto } from "../../types/producto.types";
import "./ProductGrid.css";

interface ProductGridProps {
  productos: Producto[];
}

export default function ProductGrid({ productos }: ProductGridProps) {
    return (
        <div className="product-grid">
            {productos.map((producto) => (
                <ProductCard key={producto.id} producto={producto} />
            ))}
        </div>
    );
}
// Este componente renderiza una cuadrícula de productos utilizando el componente ProductCard para cada producto.
// Utiliza el tipo Producto para asegurar que los datos del producto tengan la estructura correcta.