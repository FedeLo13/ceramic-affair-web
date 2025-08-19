import ProductCard from "../ProductCard/ProductCard";
import type { ProductoOutputDTO } from "../../types/producto.types";
import { motion } from "framer-motion";
import "./ProductGrid.css";

interface ProductGridProps {
  productos: ProductoOutputDTO[];
  selectionMode?: "delete" | "soldout" | null; // Modo de selección para administrador
  selectedProductos: number[];
  onProductClick: (productoId: number) => void;
}

const containerVariants = {
    hidden: { opacity: 0 },
    visible: { opacity: 1, transition: { staggerChildren: 0.1 } },
};

export default function ProductGrid({ productos, selectionMode, selectedProductos, onProductClick }: ProductGridProps) {
    return (
        <motion.div
            className="product-grid"
            variants={containerVariants}
            initial="hidden"
            animate="visible"
        >
           {productos.map((producto) => (
                <ProductCard 
                    key={producto.id} 
                    producto={producto}
                    selectionMode={selectionMode}
                    selected={selectedProductos.includes(producto.id)}
                    onClick={() => onProductClick(producto.id)}    
                />
            ))}
        </motion.div>
    );
}
// Este componente renderiza una cuadrícula de productos utilizando el componente ProductCard para cada producto.
// Utiliza el tipo Producto para asegurar que los datos del producto tengan la estructura correcta.