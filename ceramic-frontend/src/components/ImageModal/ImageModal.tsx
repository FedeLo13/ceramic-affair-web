import { useState } from "react";
import "./ImageModal.css";

interface ImageModalProps {
    images: { ruta: string }[];
    currentIndex: number;
    alt: string;
    onClose: () => void;
}

export default function ImageModal({ images, currentIndex, alt, onClose }: ImageModalProps) {
    const [index, setIndex] = useState(currentIndex);

    const handlePrev = () => {
        setIndex((prev) => (prev === 0 ? images.length - 1 : prev - 1));
    };

    const handleNext = () => {
        setIndex((prev) => (prev === images.length - 1 ? 0 : prev + 1));
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <button className="close-btn" onClick={onClose}>✕</button>
                <button className="nav-btn left" onClick={handlePrev}>‹</button>
                <img
                    src={`http://localhost:8080/uploads/${images[index].ruta}`}
                    alt={alt}
                    className="modal-image"
                />
                <button className="nav-btn right" onClick={handleNext}>›</button>
            </div>
        </div>
    );
}
