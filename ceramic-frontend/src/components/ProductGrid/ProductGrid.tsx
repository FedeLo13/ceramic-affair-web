import ProductCard from "../ProductCard/ProductCard";
import type { ProductoOutputDTO } from "../../types/producto.types";
import { motion } from "framer-motion";
import "./ProductGrid.css";

interface ProductGridProps {
  productos: ProductoOutputDTO[];
  isLoading?: boolean;
}

const containerVariants = {
    hidden: { opacity: 0 },
    visible: { opacity: 1, transition: { staggerChildren: 0.1 } },
};

export default function ProductGrid({ productos, isLoading }: ProductGridProps) {
    return (
        <motion.div
            className="product-grid"
            variants={containerVariants}
            initial="hidden"
            animate="visible"
        >
           {productos.map((producto) => (
                <ProductCard key={producto.id} producto={producto} />
            ))}

            {isLoading && (
                <>
                    <div className="skeleton-card"></div>
                    <div className="skeleton-card"></div>
                    <div className="skeleton-card"></div>
                </>
            )}
        </motion.div>
    );
}
// Este componente renderiza una cuadrícula de productos utilizando el componente ProductCard para cada producto.
// Utiliza el tipo Producto para asegurar que los datos del producto tengan la estructura correcta.