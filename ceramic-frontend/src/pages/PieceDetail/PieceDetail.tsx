import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { getProductoById } from "../../api/productos";
import type { Producto } from "../../types/producto.types";

export default function PieceDetail() {
    const { id } = useParams();
    const [producto, setProducto] = useState<Producto | null>(null);

    useEffect(() => {
        const fetchProducto = async () => {
            if (id) {
                try {
                    const data = await getProductoById(Number(id));
                    setProducto(data);
                } catch (error) {
                    console.error("Error fetching product:", error);
                }
            }
        };
        fetchProducto();
    }, [id]);

    return (
        <div className="piece-detail">
            {/* Cosas a añadir aquí: Galería de imágenes y ruta Pieces > Categoría > Producto */}
            {producto ? (
                <>
                    <h2>{producto.nombre}</h2>
                    <p><strong>Price:</strong> €{producto.precio.toFixed(2)}</p>
                    <p><strong>Description:</strong> {producto.descripcion}</p>
                    <p><strong>Sold out:</strong> {producto.soldOut ? "Yes" : "No"}</p>
                    <p><strong>Height:</strong> {producto.altura} cm</p>
                    <p><strong>Width:</strong> {producto.anchura} cm</p>
                    <p><strong>Diameter:</strong> {producto.diametro} cm</p>
                    <p><strong>Category:</strong> {producto.categoria?.nombre || "No category"}</p>
                </>
            ) : (
                <p>Loading...</p>
            )}
        </div>
    );
}