import React, { useEffect, useState } from "react";
import type { Imagen } from "../../types/imagen.types";
import "./ImageUploader.css";

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

  useEffect(() => {
    const previews = newImages.map((file) => URL.createObjectURL(file));
    setNewPreviews(previews);
    return () => previews.forEach((url) => URL.revokeObjectURL(url));
  }, [newImages]);

  const handleSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      onAddNew(Array.from(e.target.files));
    }
  };

  return (
    <div className="image-uploader">
      <label>Upload images</label>
      <input type="file" multiple accept="image/*" onChange={handleSelect} />

      <div className="preview-container">
        {/* Imágenes ya en el servidor */}
        {existingImages.map((img, index) => (
          <div key={img.id} className="preview-item">
            <img src={`${BASE_IMAGE_URL}${img.ruta}`} alt={`Imagen ${img.id}`} />
            <button type="button" onClick={() => onRemoveExisting(index)}>X</button>
          </div>
        ))}

        {/* Imágenes nuevas (aún no subidas) */}
        {newPreviews.map((preview, index) => (
          <div key={index} className="preview-item">
            <img src={preview} alt={`Nueva ${index}`} />
            <button type="button" onClick={() => onRemoveNew(index)}>X</button>
          </div>
        ))}
      </div>
    </div>
  );
}
