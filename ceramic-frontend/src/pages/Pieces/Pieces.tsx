import { useEffect, useRef, useState } from "react";
import { filterProductos, updateStockProducto, deleteProducto, type FilterProductosParams } from "../../api/productos";
import { getAllCategorias } from "../../api/categorias";
import type { ProductoOutputDTO, ProductoStockDTO } from "../../types/producto.types";
import type { Categoria } from "../../types/categoria.types";
import ProductGrid from "../../components/ProductGrid/ProductGrid";
import "./Pieces.css";
import { useAuth } from "../../context/AuthContext";
import { FaCrown, FaSearch } from "react-icons/fa";
import { useSearchParams } from "react-router-dom";

type PiecesProps = {
    showFilters?: boolean;
};

export default function Pieces({ showFilters: defaultShowFilters = true }: PiecesProps) {
    const [searchParams] = useSearchParams(); // Obtener los parámetros de búsqueda
    const categoriaParam = searchParams.get("categoria"); // Obtener el parámetro de categoría
    const showFilters = searchParams.get("showFilters") === "true" || defaultShowFilters; // Obtener el parámetro showFilters o usar el valor por defecto
    const [productos, setProductos] = useState<ProductoOutputDTO[]>([]); // Estado para los productos
    const [pageInfo, setPageInfo] = useState({
        totalPages: 0,
        currentPage: 0,
    }); // Estado para la paginación
    const [categorias, setCategorias] = useState<Categoria[]>([]); // Estado para las categorías
    const [loading, setLoading] = useState(false); // Estado de carga
    const [isFetching, setIsFetching] = useState(false); // Estado para el lazy loading
    const [adminDropdownOpen, setAdminDropdownOpen] = useState(false); // Estado para el menú de administrador
    const { isAuthenticated } = useAuth(); // Hook de autenticación

    // Estado para los filtros
    const [nombre, setNombre] = useState(""); // Estado para el filtro de búsqueda por nombre
    const [debouncedNombre, setDebouncedNombre] = useState(nombre); // Para evitar llamadas excesivas a la API
    const [categoriaId, setCategoriaId] = useState<number | undefined>(
        categoriaParam ? Number(categoriaParam) : undefined
    ); // undefined para "Todas las categorías"
    const [soloEnStock, setSoloEnStock] = useState<boolean | undefined>(undefined); // undefined para "Todos los productos"
    const [orden, setOrden] = useState<"nuevos" | "viejos">("nuevos"); // Filtro de orden

    // Estados para el modo edición del administrador
    const [selectedProductos, setSelectedProductos] = useState<number[]>([]); // IDs de productos seleccionados
    const [selectionMode, setSelectionMode] = useState<"delete" | "soldout" | null>(null); // Modo de selección actual
    const isSoldOutSelectionMode = selectionMode === "soldout"; 

    // Ref para manejar la paginación con lazy loading
    const loader = useRef<HTMLDivElement | null>(null);

    // Estado para manejar el filtro de categoría por URL
    useEffect(() => {
        if (categoriaParam && !isNaN(Number(categoriaParam))) {
            setCategoriaId(Number(categoriaParam));
        } else {
            setCategoriaId(undefined);
        }
    }, [categoriaParam]);


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

            // Vaciar productos justo antes de hacer la búsqueda
            if (pageInfo.currentPage === 0) setProductos([]);

            try {
                const filtros: FilterProductosParams = {
                    nombre: debouncedNombre.trim() || undefined, // Usar el nombre debounced
                    categoria: categoriaId && !isNaN(categoriaId) ? categoriaId : undefined,
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

    // Forzar filtro de stock en el modo de selección para establecer el stock
    useEffect(() => {
        if (isSoldOutSelectionMode) {
            setSoloEnStock(true); // Solo mostrar productos en stock
            setProductos([]); // Limpiar productos al cambiar a modo sold out
            setPageInfo({ totalPages: 0, currentPage: 0 }); // Reiniciar paginación
        }
    }, [isSoldOutSelectionMode]);

    // Handlers para los filtros
    const handleNombreChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPageInfo({ totalPages: 0, currentPage: 0 }); // Reiniciar paginación
        setNombre(e.target.value);
    };

    const handleCategoriaChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setPageInfo({ totalPages: 0, currentPage: 0 }); // Reiniciar paginación
        setCategoriaId(e.target.value === "" ? undefined : Number(e.target.value));
    };

    const handleOrdenChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setPageInfo({ totalPages: 0, currentPage: 0 }); // Reiniciar paginación
        setOrden(e.target.value as "nuevos" | "viejos");
    };

    const handleSoloEnStockChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setPageInfo({ totalPages: 0, currentPage: 0 }); // Reiniciar paginación
        const value = e.target.value;
        setSoloEnStock(value === "" ? undefined : value === "true");
    };

    // Handlers para el modo edición del administrador
    const handleProductClick = (productoId: number) => {
        if(!selectionMode) return; // No hacer nada si no hay modo de selección

        setSelectedProductos((prev) => 
          prev.includes(productoId)
            ? prev.filter(id => id !== productoId) // Desmarcar si ya está seleccionado
            : [...prev, productoId] // Marcar si no está seleccionado  
        );
    }

    const handleConfirmAction = async () => {
        if (selectedProductos.length === 0 || !selectionMode) return;

        try {
            if (selectionMode === "delete") {
                await Promise.all(selectedProductos.map(id => deleteProducto(id)));
            } else if (selectionMode === "soldout") {
                const soldOutPayload: ProductoStockDTO = { soldOut: true };
                await Promise.all(selectedProductos.map(id => updateStockProducto(id, soldOutPayload)));
            }

            // Reiniciar productos y paginación
            setProductos([]);
            setPageInfo({ totalPages: 0, currentPage: 0 });

            // Limpiar selección y modo
            setSelectedProductos([]);
            setSelectionMode(null);
        } catch (error) {
            console.error("Error performing action on products:", error);
        }
    }

    const hasMore = pageInfo.totalPages === 0 || pageInfo.currentPage < pageInfo.totalPages - 1;

    return (
        <div className="pieces-page">
            {/* Filtros */}
            { showFilters && (
                <div className="filters">

                    {/* Barra de búsqueda */}
                    <div className="search-wrapper">
                        <input
                            type="text"
                            placeholder="Search for a product..."
                            value={nombre}
                            onChange={handleNombreChange}
                            className="search-bar"
                        />
                        <FaSearch className="search-icon" />
                    </div>

                    {/* Dropdown de categorías */}
                    <div className="select-wrapper">
                        <select
                            value={categoriaId !== undefined ? categoriaId.toString() : ""}
                            onChange={handleCategoriaChange}
                            className="minimal-select"
                        >
                            <option value="">All categories</option>
                            {categorias.map((categoria) => (
                                <option key={categoria.id} value={categoria.id}>
                                    {categoria.nombre}
                                </option>
                            ))}
                        </select>
                    </div>

                    {/* Orden */}
                    <div className="select-wrapper">
                        <select
                            value={orden}
                            onChange={handleOrdenChange}
                            className="minimal-select"
                        >
                            <option value="nuevos">Newest</option>
                            <option value="viejos">Oldest</option>
                        </select>
                    </div>

                    {/* Stock */}
                    <div className="select-wrapper">
                        <select
                            value={soloEnStock === undefined ? "" : soloEnStock ? "true" : "false"}
                            onChange={handleSoloEnStockChange}
                            className="minimal-select"
                            disabled={isSoldOutSelectionMode} // Deshabilitar si estamos en modo sold out
                        >
                            <option value="">All products</option>
                            <option value="true">In stock</option>
                        </select>
                    </div>

                    {/* Admin Dropdown (solo visible si está logueado) */}
                    {isAuthenticated && (
                        <div className="admin-menu">
                            <button
                                className="admin-toggle" 
                                onClick={() => setAdminDropdownOpen(!adminDropdownOpen)}>
                                    <FaCrown size={24} /> Admin Menu
                            </button>
                            {adminDropdownOpen && (
                                <div className="admin-dropdown">
                                    <button className="delete-btn" onClick={() => {
                                        setSelectionMode("delete");
                                        setSelectedProductos([]);
                                        setAdminDropdownOpen(false);
                                    }}>
                                        Delete products
                                    </button>
                                    <button className="soldout-btn" onClick={() => {
                                        setSelectionMode("soldout");
                                        setSelectedProductos([]);
                                        setAdminDropdownOpen(false);
                                    }}>
                                        Mark as sold out
                                    </button>
                                </div>
                            )}
                        </div>
                    )}
                </div>
            )}
            {/* Grid de productos */}
            {productos.length === 0 ? (
                <div className="loader-container">
                    {loading ? (
                        <>
                            <div className="spinner"></div>
                            <p>Cargando productos...</p>
                        </>
                    ) : (
                        <p>No se encontraron productos</p>
                    )}
                </div>
            ) : (
                <ProductGrid 
                    productos={productos} 
                    selectionMode={selectionMode}
                    selectedProductos={selectedProductos}
                    onProductClick={handleProductClick}
                />
            )}


            {/* Loader para lazy loading */}
            {hasMore && <div ref={loader} style = {{ height: "20px" }}></div>}

            {/* Botones de confirmar y cancelar para el modo edición */}
            {selectionMode && (
                <div className="floating-action-buttons">
                    <button className="confirm-selection-btn" disabled={selectedProductos.length === 0} onClick={handleConfirmAction}>
                        Confirm
                    </button>
                    <button className="cancel-selection-btn" onClick={() => {
                        setSelectionMode(null);
                        setSelectedProductos([]);
                    }}>
                        Cancel
                    </button>
                </div>
            )}  
        </div>
    );
}