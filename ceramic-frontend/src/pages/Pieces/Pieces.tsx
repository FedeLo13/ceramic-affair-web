import { useEffect, useRef, useState } from "react";
import { filterProductos, type FilterProductosParams } from "../../api/productos";
import { getAllCategorias } from "../../api/categorias";
import type { ProductoOutputDTO } from "../../types/producto.types";
import type { Categoria } from "../../types/categoria.types";
import ProductGrid from "../../components/ProductGrid/ProductGrid";
import "./Pieces.css";

export default function Pieces() {
    const [productos, setProductos] = useState<ProductoOutputDTO[]>([]);
    const [pageInfo, setPageInfo] = useState({
        totalPages: 0,
        currentPage: 0,
    });
    const [categorias, setCategorias] = useState<Categoria[]>([]);
    const [loading, setLoading] = useState(false);
    const [isFetching, setIsFetching] = useState(false);

    // Estado para los filtros
    const [nombre, setNombre] = useState("");
    const [debouncedNombre, setDebouncedNombre] = useState(nombre); // Para evitar llamadas excesivas a la API
    const [categoriaId, setCategoriaId] = useState<number | undefined>(undefined); // undefined para "Todas las categorías"
    const [soloEnStock, setSoloEnStock] = useState<boolean | undefined>(undefined); // undefined para "Todos los productos"
    const [orden, setOrden] = useState<"nuevos" | "viejos">("nuevos");

    // Ref para manejar la paginación con lazy loading
    const loader = useRef<HTMLDivElement | null>(null);

    // Debounce para la búsqueda por nombre
    useEffect(() => {
        const handler = setTimeout(() => {
            setDebouncedNombre(nombre);
        }, 300); // Espera 300ms antes de actualizar el nombre

        return () => {
            clearTimeout(handler);
        };
    }, [nombre]);

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
            setLoading(true);
            try {
                const filtros: FilterProductosParams = {
                    nombre: debouncedNombre.trim() || undefined, // Usar el nombre debounced
                    categoria: categoriaId,
                    soloEnStock,
                    orden,
                };
                const data = await filterProductos({...filtros, page: pageInfo.currentPage, size: 3 });

                // Acumular productos si no estamos en la primera página
                setProductos((prev) => {
                    const nuevos = data.content.filter(
                        (prod) => !prev.some(p => p.id === prod.id)
                    );

                    return pageInfo.currentPage === 0
                        ? data.content
                        : [...prev, ...nuevos];
                });

                setPageInfo((prev) => ({
                    ...prev,
                    totalPages: data.totalPages,
                }));
            } catch (error) {
                console.error("Error fetching products:", error);
            } finally {
                setIsFetching(false); // Unlock
                setLoading(false);
            }
        };
        fetchProductos();
    }, [debouncedNombre, categoriaId, soloEnStock, orden, pageInfo.currentPage]);

    // Manejar el lazy loading
    useEffect(() => {
        const observer = new IntersectionObserver((entries) => {
            const isVisible = entries[0].isIntersecting;
            if (isVisible && pageInfo.currentPage + 1 < pageInfo.totalPages && !isFetching) {
                setIsFetching(true); // Lock
                setPageInfo((prev) => ({
                    ...prev,
                    currentPage: prev.currentPage + 1,
                }));
            }
        }, { threshold: 1.0 });

        const loaderElement = loader.current;
        if (loaderElement) {
            observer.observe(loaderElement);
        }

        return () => {
            if (loaderElement) {
                observer.unobserve(loaderElement);
            }
        };
    }, [isFetching, pageInfo.currentPage, pageInfo.totalPages]);

    // Handlers para los filtros
    const handleNombreChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setProductos([]); // Limpiar productos al cambiar el nombre
        setPageInfo({ totalPages: 0, currentPage: 0 }); // Reiniciar paginación
        setNombre(e.target.value);
    };

    const handleCategoriaChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setProductos([]); // Limpiar productos al cambiar la categoría
        setPageInfo({ totalPages: 0, currentPage: 0 }); // Reiniciar paginación
        setCategoriaId(e.target.value === "" ? undefined : Number(e.target.value));
    };

    const handleOrdenChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setProductos([]); // Limpiar productos al cambiar el orden
        setPageInfo({ totalPages: 0, currentPage: 0 }); // Reiniciar paginación
        setOrden(e.target.value as "nuevos" | "viejos");
    };

    const handleSoloEnStockChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setProductos([]); // Limpiar productos al cambiar el stock
        setPageInfo({ totalPages: 0, currentPage: 0 }); // Reiniciar paginación
        const value = e.target.value;
        setSoloEnStock(value === "" ? undefined : value === "true");
    };

    const hasMore = pageInfo.totalPages === 0 || pageInfo.currentPage < pageInfo.totalPages - 1;

    return (
        <div className="pieces-page">
            {/* Filtros */}
            <div className="filters">

                {/* Barra de búsqueda */}
                <input
                    type="text"
                    placeholder="Search for a product..."
                    value={nombre}
                    onChange={handleNombreChange}
                    className="search-bar"
                />

                {/* Dropdown de categorías */}
                <select
                    value={categoriaId}
                    onChange={handleCategoriaChange}
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
                    onChange={handleOrdenChange}
                    className="select-order"
                >
                    <option value="nuevos">Newest</option>
                    <option value="viejos">Oldest</option>
                </select>

                {/* Stock */}
                <select
                    value={soloEnStock === undefined ? "" : soloEnStock ? "true" : "false"}
                    onChange={handleSoloEnStockChange}
                    className="select-stock"
                >
                    <option value="">All products</option>
                    <option value="true">In stock</option>
                </select>
            </div>

            {productos.length === 0 ? (
                <p className="text-no-products">No products found.</p>
            ) : (
                <ProductGrid productos={productos} isLoading={loading} />
            )}

            {/* Loader para lazy loading */}
            {hasMore && <div ref={loader} style = {{ height: "20px" }}></div>}
        </div>
    );
}