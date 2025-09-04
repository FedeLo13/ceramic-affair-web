import React, { useEffect, useRef, useState } from "react";
import type { Imagen } from "../../types/imagen.types";
import "./ImageUploader.css";
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";

const BASE_IMAGE_URL = "http://localhost:8080/uploads/";

interface Props {
  existingImages: Imagen[];
  onRemoveExisting: (index: number) => void;
  newImages: File[];
  onAddNew: (files: File[]) => void;
  onRemoveNew: (index: number) => void;
}

export default function ImageUploader({
  existingImages,
  onRemoveExisting,
  newImages,
  onAddNew,
  onRemoveNew
}: Props) {
  const [newPreviews, setNewPreviews] = useState<string[]>([]);
  const thumbsRef = useRef<HTMLDivElement>(null);
  const [message, setMessage] = useState<string | null>(null);
  const [visibleMessage, setVisibleMessage] = useState(false);

  useEffect(() => {
    if (message) {
      setVisibleMessage(true);
      const timer = setTimeout(() => setVisibleMessage(false), 3000);
      return () => clearTimeout(timer);
    }
  }, [message]);

  const handleTransitionEnd = () => {
    if (!visibleMessage) setMessage(null);
  };

  useEffect(() => {
    const previews = newImages.map((file) => URL.createObjectURL(file));
    setNewPreviews(previews);
    return () => previews.forEach((url) => URL.revokeObjectURL(url));
  }, [newImages]);

  const handleSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const filesArray = Array.from(e.target.files);
      const imageFiles = filesArray.filter(file =>
        file.type.startsWith('image/')
      );
      if (imageFiles.length > 0) {
        onAddNew(imageFiles);
      }
      if (imageFiles.length < filesArray.length) {
        setMessage("Some files were not images and were ignored.");
      }
    }
  };

  const scrollLeft = () => {
    thumbsRef.current?.scrollBy({ left: -100, behavior: "smooth" });
  };

  const scrollRight = () => {
    thumbsRef.current?.scrollBy({ left: 100, behavior: "smooth" });
  }

  return (
    <div className="image-uploader">
      <label className="image-uploader-title">Upload images</label>
      <label className="file-upload-button">
        Browse files
        <input 
          type="file" 
          multiple 
          accept="image/*" 
          onChange={handleSelect} 
          style={{ display: 'none' }}
        />
      </label>  
      <div
        className="dropzone"
        onDragOver={(e) => e.preventDefault()}
        onDrop={(e) => {
          e.preventDefault();
          if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
            const filesArray = Array.from(e.dataTransfer.files);
            const imageFiles = filesArray.filter(file =>
              file.type.startsWith('image/')
            );
            if (imageFiles.length > 0) {
              onAddNew(imageFiles);
            }
            if (imageFiles.length < filesArray.length) {
              setMessage("Some files were not images and were ignored.");
            }
            e.dataTransfer.clearData();
          }
        }}
      >
        Drag & drop images to upload for current product
      </div>

      <div className="preview-container">
        <div className="preview-thumbs" ref={thumbsRef}>
          {/* Imágenes ya en el servidor */}
          {existingImages.map((img, index) => (
            <div key={img.id} className="preview-thumb">
              <img src={`${BASE_IMAGE_URL}${img.ruta}`} alt={`Imagen ${img.id}`} />
              <button type="button" onClick={() => onRemoveExisting(index)}>X</button>
            </div>
          ))}
          {/* Imágenes nuevas (aún no subidas) */}
          {newPreviews.map((preview, index) => (
            <div key={index} className="preview-thumb">
              <img src={preview} alt={`Nueva ${index}`} />
              <button type="button" onClick={() => onRemoveNew(index)}>X</button>
            </div>
          ))}
        </div>
        {/* Flechas de navegación */}
        {existingImages.length + newPreviews.length > 1 && (
          <div className="uploader-thumbs-navigation">
            <button type="button" className="uploader-thumb-nav-btn left" onClick={scrollLeft}>
              <FaChevronLeft />
            </button>
            <button type="button" className="uploader-thumb-nav-btn right" onClick={scrollRight}>
              <FaChevronRight />
            </button>
          </div>
        )}
      </div>

      {/* Toast */}
      {message && (
        <div
          className={`toast-message ${visibleMessage ? "show" : "hide"}`}
          onTransitionEnd={handleTransitionEnd}
        >
          {message}
          <button
            className="toast-close-btn"
            onClick={() => setVisibleMessage(false)}
          >
            ×
          </button>
        </div>
      )}
    </div>
  );
}
