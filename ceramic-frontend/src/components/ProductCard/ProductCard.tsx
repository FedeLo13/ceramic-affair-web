import { Link } from "react-router-dom";
import { getImagenById } from "../../api/imagenes";
import type { ProductoOutputDTO } from "../../types/producto.types";
import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import "./ProductCard.css";

interface ProductCardProps {
  producto: ProductoOutputDTO;
  selectionMode?: "delete" | "soldout" | null; // Modo de selección para administrador
  selected?: boolean; // Indica si el producto está seleccionado en modo edición
  onClick?: () => void; // Función para manejar el clic en el producto
}

export default function ProductCard({ producto, selectionMode, selected = false, onClick }: ProductCardProps) {
    const {
        id,
        nombre,
        precio,
        soldOut,
        idsImagenes,
    } = producto;

    const [imageURL, setImageURL] = useState<string>("images/1068302.png");

    const BASE_IMAGE_URL = "http://localhost:8080/uploads/";

    useEffect(() => {

        const fetchImage = async () => {
            if (Array.isArray(idsImagenes) && idsImagenes.length > 0) {
                try {
                    const image = await getImagenById(idsImagenes[0]);
                    setImageURL(`${BASE_IMAGE_URL}${image.ruta}`);
                } catch (error) {
                    console.error("Error fetching product image:", error);
                }
            } else {
                setImageURL("images/1068302.png");
            }
        };
        fetchImage();
    }, [idsImagenes]);

    const cardClass = `
        product-card 
        ${selectionMode ? "selectable" : ""} 
        ${selected && selectionMode === "delete" ? "selected-delete" : ""}
        ${selected && selectionMode === "soldout" ? "selected-soldout" : ""}
    `;

    return (
        <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.4, ease: "easeOut" }}
            onClick={selectionMode ? onClick : undefined} // Maneja el clic solo si hay un modo de selección
        >
            {selectionMode ? (
                <div className={cardClass}>
                    <img src={imageURL} alt={nombre} className="product-image" />
                    <h3 className="product-name">{nombre}</h3>
                    <p className="product-price">€{precio.toFixed(2)}</p>
                    {soldOut && (
                        <span className="product-sold-out">
                            Sold Out
                        </span>
                    )}
                </div>
            ) : (
                <Link to={`/pieces/${id}`} className="product-card">
                    <img
                        src={imageURL}
                        alt={nombre}
                        className="product-image"
                    />
                    <h3 className="product-name">{nombre}</h3>
                    <p className="product-price">€{precio.toFixed(2)}</p>
                    {soldOut && (
                        <span className="product-sold-out">
                            Sold Out
                        </span>
                    )}
                </Link>
            )}
        </motion.div>
    );
}
// Este componente ProductCard renderiza una tarjeta de producto con su imagen, nombre, precio y estado de disponibilidad.
// Utiliza el tipo Producto para asegurar que los datos del producto tengan la estructura correcta.