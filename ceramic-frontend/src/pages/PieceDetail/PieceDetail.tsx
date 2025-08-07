import { Link, useParams } from "react-router-dom";
import { useCallback, useEffect, useRef, useState } from "react";
import { getProductoById, deleteProducto } from "../../api/productos";
import type { ProductoOutputDTO } from "../../types/producto.types";
import type { Imagen } from "../../types/imagen.types";
import { getImagenById } from "../../api/imagenes";
import { FaChevronLeft, FaChevronRight, FaEdit, FaTrash } from "react-icons/fa";
import "./PieceDetail.css";
import ZoomImage from "../../components/ZoomImage/ZoomImage";
import ImageModal from "../../components/ImageModal/ImageModal";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { useSwipeable } from "react-swipeable";

const BASE_IMAGE_URL = "http://localhost:8080/uploads/";

export default function PieceDetail() {
    const navigate = useNavigate();
    const { id } = useParams();
    const [producto, setProducto] = useState<ProductoOutputDTO | null>(null);
    const [imagenes, setImagenes] = useState<Imagen[]>([]);
    const [showModal, setShowModal] = useState(false);
    const [currentImageIndex, setCurrentImageIndex] = useState(0);
    const { isAuthenticated } = useAuth(); // Hook de autenticación

    const thumbsRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (!thumbsRef.current) return;

        const activeThumb = thumbsRef.current.querySelector<HTMLImageElement>(
            ".thumb.active"
        );
        if (activeThumb) {
            // Scroll horizontal para que la miniatura activa sea visible y centrada aproximadamente
            const container = thumbsRef.current;
            const containerRect = container.getBoundingClientRect();
            const thumbRect = activeThumb.getBoundingClientRect();

            const offset =
            thumbRect.left - containerRect.left - container.clientWidth / 2 + thumbRect.width / 2;

            container.scrollBy({
            left: offset,
            behavior: "smooth",
            });
        }
    }, [currentImageIndex]);


    useEffect(() => {
        const fetchProducto = async () => {
            if (id) {
                try {
                    const data = await getProductoById(Number(id));
                    setProducto(data);

                    // Obtener las imágenes asociadas al producto
                    const imagePromises = await Promise.all(
                        data.idsImagenes.map((imageId) => getImagenById(imageId))
                    );
                    setImagenes(imagePromises);
                } catch (error) {
                    console.error("Error fetching product:", error);
                }
            }
        };
        fetchProducto();
    }, [id]);

    const handlers = useSwipeable({
        onSwipedLeft: () => {
            if (thumbsRef.current) {
                thumbsRef.current.scrollBy({ left: 70, behavior: "smooth" });
            }
        },
        onSwipedRight: () => {
            if (thumbsRef.current) {
                thumbsRef.current.scrollBy({ left: -70, behavior: "smooth" });
            }
        },
        trackMouse: true,
    });

    const { ref: swipeRef, ...handlersWithoutRef } = handlers;

    const combinedRef = useCallback((node: HTMLDivElement | null) => {
        thumbsRef.current = node;

        if (typeof swipeRef === "function") {
            swipeRef(node);
        } else if (swipeRef && "current" in swipeRef) {
            (swipeRef as React.RefObject<HTMLDivElement | null>).current = node;
        }
    }, [swipeRef]);

    const handleWheel = (e: React.WheelEvent<HTMLDivElement>) => {
        if (thumbsRef.current) {
            e.preventDefault(); // evitar scroll vertical de la página
            thumbsRef.current.scrollBy({
            left: e.deltaY,
            });
        }
    };

    if (!producto) {
        return <p>Loading...</p>;
    }

    const handlePrev = () => {
        setCurrentImageIndex((prevIndex) => 
            (prevIndex === 0 ? producto.idsImagenes.length - 1 : prevIndex - 1)
        );
    };

    const handleNext = () => {
        setCurrentImageIndex((prevIndex) => 
            (prevIndex === producto.idsImagenes.length - 1 ? 0 : prevIndex + 1)
        );
    };

    const handleEditClick = () => {
        if (producto) {
            navigate(`/admin/products/edit/${producto.id}`);
        }
    };

    const handleDeleteClick = async () => {
        if (window.confirm("Are you sure you want to delete this piece?")) {
            try {
                await deleteProducto(producto.id);
                navigate("/pieces");
            } catch (error) {
                console.error("Error deleting product:", error);
            }
        }
    };

    return (
        <div className="piece-detail-container">

            {/* Breadcrumb */}
            <div className="breadcrumb">
                <Link to="/pieces">Pieces</Link> &gt;{" "}
                <Link 
                    to={
                        producto.idCategoria
                        ? `/pieces?categoria=${encodeURIComponent(producto.idCategoria)}`
                        : `/pieces`
                    }
                    >
                        <span>{producto.nombreCategoria || "Uncategorized"}</span>
                </Link> &gt;
                <span>{producto.nombre}</span>
            </div>

            <div className="piece-detail-content">

                {/* Galería */}
                <div className="gallery">
                    <div className="gallery-main">
                        {imagenes.length > 0 && (
                            <ZoomImage
                                src={`${BASE_IMAGE_URL}${imagenes[currentImageIndex].ruta}`}
                                alt={producto.nombre}
                                onDoubleClick={() => setShowModal(true)}
                            />
                        )}
                        {showModal && (
                            <ImageModal
                                images={imagenes}
                                currentIndex={currentImageIndex}
                                alt={producto.nombre}
                                onClose={() => setShowModal(false)}
                            />
                        )}
                        {imagenes.length > 1 && (
                            <>
                                <button className="nav-btn-prev" onClick={handlePrev}>
                                    <FaChevronLeft />
                                </button>
                                <button className="nav-btn-next" onClick={handleNext}>
                                    <FaChevronRight />
                                </button>
                            </>
                        )}
                    </div>

                    <div className="gallery-thumbs" ref={combinedRef} {...handlersWithoutRef} onWheel={handleWheel}>
                        {imagenes.map((imagen, index) => (
                            <img
                            key={imagen.id}
                            src={`${BASE_IMAGE_URL}${imagen.ruta}`}
                            alt={`${producto.nombre} thumbnail`}
                            className={`thumb ${index === currentImageIndex ? "active" : ""}`}
                            onClick={() => setCurrentImageIndex(index)}
                            />
                        ))}
                    </div>
                </div>

                {/* Detalles del producto */}
                <div className="details">
                    <h2 className="product-name">{producto.nombre}</h2>
                    <p className="product-price">€{producto.precio.toFixed(2)}</p>
                    <p className="product-description">{producto.descripcion}</p>
                    
                    {/* Tamaños */}
                    <div className="sizes">
                        <h3>Tamaños</h3>
                        {producto.altura > 0 && <p>Alto: {producto.altura} cm</p>}
                        {producto.anchura > 0 && <p>Ancho: {producto.anchura} cm</p>}
                        {producto.diametro > 0 && <p>Diámetro: {producto.diametro} cm</p>}
                    </div>

                    {/* Sold out */}
                    {producto.soldOut && (
                        <div className="sold-out">
                            <span>Sold Out</span>
                        </div>
                    )}

                    {/* Botón de editar (solo para administradores) */}
                    {isAuthenticated && (
                        <>
                            <button 
                                className="edit-button"
                                onClick={handleEditClick}
                            >
                                <FaEdit /> Editar
                            </button>
                            <button 
                                className="delete-button"
                                onClick={handleDeleteClick}
                            >
                                <FaTrash /> Eliminar
                            </button>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
}