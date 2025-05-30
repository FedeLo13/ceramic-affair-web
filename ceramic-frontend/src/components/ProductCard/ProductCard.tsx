import type { Producto } from "../../types/producto.types";

interface ProductCardProps {
  producto: Producto;
}

export default function ProductCard({ producto }: ProductCardProps) {
    const {
        nombre,
        precio,
        soldOut,
        imagenes,
    } = producto;

    const shownImage = imagenes.length > 0 ? imagenes[0] : "/images/1068302.png";

    return (
        <div className="product-card">
            <img
                src={shownImage}
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
        </div>
    );
}
// Este componente ProductCard renderiza una tarjeta de producto con su imagen, nombre, precio y estado de disponibilidad.
// Utiliza el tipo Producto para asegurar que los datos del producto tengan la estructura correcta.