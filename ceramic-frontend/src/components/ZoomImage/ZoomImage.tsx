import { useState, useRef } from "react";

interface ZoomImageProps extends React.ImgHTMLAttributes<HTMLImageElement> {
    enableZoom?: boolean;
}

export default function ZoomImage({ src, alt, enableZoom, ...rest }: ZoomImageProps) {
    const [hover, setHover] = useState(false);
    const [backgroundPosition, setBackgroundPosition] = useState("center");
    const containerRef = useRef<HTMLDivElement>(null);

    const handleMouseMove = (e: React.MouseEvent<HTMLImageElement>) => {
        if (!enableZoom) return; // No hacer nada si el zoom está deshabilitado
        const { left, top, width, height } = e.currentTarget.getBoundingClientRect();
        const x = ((e.pageX - left) / width) * 100;
        const y = ((e.pageY - top) / height) * 100;
        setBackgroundPosition(`${x}% ${y}%`);
    };

    return (
        <div
            ref={containerRef}
            className={`zoom-container ${hover ? "hover" : ""}`}
            style={hover && enableZoom ? { backgroundImage: `url(${src})`, backgroundPosition } : {}}
        >
            <img
                src={src}
                alt={alt}
                className="zoom-image"
                onMouseEnter={() => enableZoom && setHover(true)}
                onMouseLeave={() => enableZoom && setHover(false)}
                onMouseMove={handleMouseMove}
                {...rest}
            />
        </div>
    );
}
