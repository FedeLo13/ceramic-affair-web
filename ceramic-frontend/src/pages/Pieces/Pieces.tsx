import { useEffect, useState } from "react";
import { filterProductos, type FilterProductosParams } from "../../api/productos";
import { getAllCategorias } from "../../api/categorias";
import type { Producto } from "../../types/producto.types";
import type { Categoria } from "../../types/categoria.types";
import ProductGrid from "../../components/ProductGrid/ProductGrid";

export default function Pieces() {
    const [productos, setProductos] = useState<Producto[]>([]);
    const [categorias, setCategorias] = useState<Categoria[]>([]);

    // Estado para los filtros
    const [nombre, setNombre] = useState("");
    const [categoriaId, setCategoriaId] = useState<number | undefined>(undefined); // undefined para "Todas las categorías"
    const [soloEnStock, setSoloEnStock] = useState<boolean | undefined>(undefined); // undefined para "Todos los productos"
    const [orden, setOrden] = useState<"nuevos" | "viejos">("nuevos");

    // Cargar categorías para el dropdown
    useEffect(() => {
        const fetchCategorias = async () => {
            try {
                const data = await getAllCategorias();
                setCategorias(data);
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };
        fetchCategorias();
    }, []);

    // Cargar productos cada vez que cambian los filtros
    useEffect(() => {
        const fetchProductos = async () => {
            try {
                const filtros: FilterProductosParams = {
                    nombre: nombre.trim() || undefined,
                    categoria: categoriaId,
                    soloEnStock,
                    orden,
                };
                const data = await filterProductos(filtros);
                setProductos(data);
            } catch (error) {
                console.error("Error fetching products:", error);
            }
        };
        fetchProductos();
    }, [nombre, categoriaId, soloEnStock, orden]);

    return (
        <div className="pieces-page">
            {/* Filtros */}
            <div className="filters">

                {/* Barra de búsqueda */}
                <input
                    type="text"
                    placeholder="Search for a product..."
                    value={nombre}
                    onChange={(e) => setNombre(e.target.value)}
                    className="search-bar"
                />

                {/* Dropdown de categorías */}
                <select
                    value={categoriaId}
                    onChange={(e) => 
                        setCategoriaId(e.target.value === "" ? undefined : Number(e.target.value))
                    }
                    className="select-category"
                >
                    <option value="">All categories</option>
                    {categorias.map((categoria) => (
                        <option key={categoria.id} value={categoria.id}>
                            {categoria.nombre}
                        </option>
                    ))}
                </select>

                {/* Orden */}
                <select
                    value={orden}
                    onChange={(e) => setOrden(e.target.value as "nuevos" | "viejos")}
                    className="select-order"
                >
                    <option value="nuevos">Newest</option>
                    <option value="viejos">Oldest</option>
                </select>

                {/* Stock */}
                <select
                    value={soloEnStock === undefined ? "" : soloEnStock ? "true" : "false"}
                    onChange={(e) => {
                        const value = e.target.value;
                        setSoloEnStock(value === "" ? undefined : value === "true");
                    }}
                    className="select-stock"
                >
                    <option value="">All products</option>
                    <option value="true">In stock</option>
                </select>
            </div>

            {productos.length === 0 ? (
                <p className="text-no-products">No products found.</p>
            ) : (
                <ProductGrid productos={productos} />
            )}
        </div>
    );
}