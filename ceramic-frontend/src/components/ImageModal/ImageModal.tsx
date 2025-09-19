import { useEffect, useRef, useState } from "react";
import "./ImageModal.css";
import { FaChevronLeft, FaChevronRight, FaTimes } from "react-icons/fa";
import ZoomableImage from "./ZoomableImage";

interface ImageModalProps {
    images: { ruta: string }[];
    currentIndex: number;
    alt: string;
    onClose: () => void;
}

export default function ImageModal({ images, currentIndex, alt, onClose }: ImageModalProps) {
    const [index, setIndex] = useState(currentIndex);
    const [showNav, setShowNav] = useState(true);
    const hideTimer = useRef<ReturnType<typeof setTimeout> | null>(null);

    // Función que reinicia el temporizador
    const resetHideTimer = () => {
        if (window.innerWidth >= 768) return; // solo móvil
        if (hideTimer.current) clearTimeout(hideTimer.current);
        setShowNav(true);
        hideTimer.current = setTimeout(() => setShowNav(false), 3000);
    };

    // al montar, activar temporizador (solo móvil)
    useEffect(() => {
        resetHideTimer();
        return () => {
            if (hideTimer.current) clearTimeout(hideTimer.current);
        };
    }, []);

    const handlePrev = () => {
        setIndex((prev) => (prev === 0 ? images.length - 1 : prev - 1));
        resetHideTimer();
    };

    const handleNext = () => {
        setIndex((prev) => (prev === images.length - 1 ? 0 : prev + 1));
        resetHideTimer();
    };

    return (
        <div className="modal-overlay" onClick={resetHideTimer}>
            <button className="modal-close-btn" onClick={onClose}>
                <FaTimes size={24} />
            </button>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <button 
                    className={`nav-btn left ${!showNav ? "hidden" : ""}`}
                    onClick={(e) => {
                        e.stopPropagation();
                        handlePrev();
                    }}
                >
                    <FaChevronLeft size={24} />
                </button>
                <ZoomableImage
                    src={`http://localhost:8080/uploads/${images[index].ruta}`}
                    alt={alt}
                    enableZoom={true}
                    className="modal-image"
                />
                <button 
                    className={`nav-btn right ${!showNav ? "hidden" : ""}`} 
                    onClick={(e) => {
                        e.stopPropagation();
                        handleNext();
                    }}
                >
                    <FaChevronRight size={24} />
                </button>
            </div>
        </div>
    );
}
