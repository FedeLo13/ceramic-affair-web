import { useState, useRef } from "react";

interface ZoomableImageProps extends React.ImgHTMLAttributes<HTMLImageElement> {
  enableZoom?: boolean;
}

export default function ZoomableImage({ src, alt, enableZoom = true, ...rest }: ZoomableImageProps) {
  const [isZoomed, setIsZoomed] = useState(false);
  const [scale, setScale] = useState(1);
  const [translate, setTranslate] = useState({ x: 0, y: 0 });
  const lastPosition = useRef<{ x: number; y: number } | null>(null);
  const containerRef = useRef<HTMLDivElement>(null);
  const lastTap = useRef<number>(0);
  const didDrag = useRef(false);

  const clamp = (value: number, min: number, max: number) =>
    Math.max(min, Math.min(value, max));

  const toggleZoom = (x?: number, y?: number) => {
    if (!enableZoom) return;

    if (isZoomed) {
      // Reset
      setScale(1);
      setTranslate({ x: 0, y: 0 });
      setIsZoomed(false);
    } else {
      // Zoom in, centrar en punto clicado
      const rect = containerRef.current?.getBoundingClientRect();
      if (!rect) return;

      const offsetX = x ? x - rect.left - rect.width / 2 : 0;
      const offsetY = y ? y - rect.top - rect.height / 2 : 0;

      setScale(2);
      setTranslate({ x: -offsetX, y: -offsetY });
      setIsZoomed(true);
    }
  };

  const handleMouseDown = (e: React.MouseEvent) => {
    if (!isZoomed) return;
    lastPosition.current = { x: e.clientX, y: e.clientY };
    didDrag.current = false; // reset
  };

  const handleMouseMove = (e: React.MouseEvent) => {
    if (!isZoomed || !lastPosition.current) return;

    const dx = e.clientX - lastPosition.current.x;
    const dy = e.clientY - lastPosition.current.y;

    if (Math.abs(dx) > 2 || Math.abs(dy) > 2) {
      didDrag.current = true;
    }

    updateTranslate(dx, dy);
    lastPosition.current = { x: e.clientX, y: e.clientY };
  };

  const handleMouseUp = () => {
    lastPosition.current = null;
  };

  const handleClick = (e: React.MouseEvent) => {
    if (didDrag.current) {
      didDrag.current = false;
      return;
    }
    toggleZoom(e.clientX, e.clientY);
  };

  // Móvil
  const handleTouchEnd = (e: React.TouchEvent) => {
    const now = Date.now();
    if (now - lastTap.current < 300) {
      const touch = e.changedTouches[0];
      toggleZoom(touch.clientX, touch.clientY);
    }
    lastTap.current = now;
  };

  const handleTouchMove = (e: React.TouchEvent) => {
    if (!isZoomed || e.touches.length !== 1 || !lastPosition.current) return;

    const touch = e.touches[0];
    const dx = touch.clientX - lastPosition.current.x;
    const dy = touch.clientY - lastPosition.current.y;

    updateTranslate(dx, dy);
    lastPosition.current = { x: touch.clientX, y: touch.clientY };
  };

  const handleTouchStart = (e: React.TouchEvent) => {
    if (!isZoomed) return;
    const touch = e.touches[0];
    lastPosition.current = { x: touch.clientX, y: touch.clientY };
  };

  // Limitar traslación
  const updateTranslate = (dx: number, dy: number) => {
    const container = containerRef.current;
    if (!container) return;

    const rect = container.getBoundingClientRect();
    const imgWidth = rect.width;
    const imgHeight = rect.height;

    const maxX = ((scale - 1) * imgWidth) / 2;
    const maxY = ((scale - 1) * imgHeight) / 2;

    setTranslate((prev) => {
      const newX = clamp(prev.x + dx, -maxX, maxX);
      const newY = clamp(prev.y + dy, -maxY, maxY);
      return { x: newX, y: newY };
    });
  };

  return (
    <div
      ref={containerRef}
      style={{
        overflow: "hidden",
        touchAction: "none",
        cursor: isZoomed ? "grab" : "zoom-in",
        maxWidth: "90vw",
        maxHeight: "90vh",
        display: "flex",
        justifyContent: "center",
      }}
      onMouseDown={handleMouseDown}
      onMouseMove={handleMouseMove}
      onMouseUp={handleMouseUp}
      onClick={handleClick}
      onTouchStart={handleTouchStart}
      onTouchMove={handleTouchMove}
      onTouchEnd={handleTouchEnd}
    >
      <img
        src={src}
        alt={alt}
        {...rest}
        style={{
            transform: `scale(${scale}) translate(${translate.x / scale}px, ${translate.y / scale}px)`,
            transformOrigin: "center center",
            transition: isZoomed ? "none" : "transform 0.3s ease",
            maxWidth: "100%",
            maxHeight: "100%",
            objectFit: "contain",
            userSelect: "none",
            pointerEvents: "none",
        }}
        draggable={false}
      />
    </div>
  );
}