import { useState, useRef } from "react";

interface ZoomImageProps {
    src: string;
    alt: string;
    onDoubleClick: () => void;
}

export default function ZoomImage({ src, alt, onDoubleClick }: ZoomImageProps) {
    const [hover, setHover] = useState(false);
    const [backgroundPosition, setBackgroundPosition] = useState("center");
    const containerRef = useRef<HTMLDivElement>(null);

    const handleMouseMove = (e: React.MouseEvent<HTMLImageElement>) => {
        const { left, top, width, height } = e.currentTarget.getBoundingClientRect();
        const x = ((e.pageX - left) / width) * 100;
        const y = ((e.pageY - top) / height) * 100;
        setBackgroundPosition(`${x}% ${y}%`);
    };

    return (
        <div
            ref={containerRef}
            className={`zoom-container ${hover ? "hover" : ""}`}
            style={hover ? { backgroundImage: `url(${src})`, backgroundPosition } : {}}
        >
            <img
                src={src}
                alt={alt}
                className="zoom-image"
                onMouseEnter={() => setHover(true)}
                onMouseLeave={() => setHover(false)}
                onMouseMove={handleMouseMove}
                onDoubleClick={onDoubleClick}
            />
        </div>
    );
}
