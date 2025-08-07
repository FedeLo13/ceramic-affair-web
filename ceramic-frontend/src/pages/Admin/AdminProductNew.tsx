import ProductForm from "../../components/ProductForm/ProductForm";
import { newProducto } from "../../api/productos";
import { useNavigate } from "react-router-dom";
import type { ProductoInputDTOWithImages } from "../../components/ProductForm/ProductoInputDTOWithImages";
import { useState } from "react";
import { newImagen } from "../../api/imagenes";
import "./AdminProductNew.css";

export default function AdminProductNew() {
  const navigate = useNavigate();
  const [drafts, setDrafts] = useState<ProductoInputDTOWithImages[]>([]);
  const [editingIndex, setEditingIndex] = useState<number | null>(null);

  const handleAddToList = (data: ProductoInputDTOWithImages) => {
    if (editingIndex !== null) {
      const updated = [...drafts];
      updated[editingIndex] = data;
      setDrafts(updated);
      setEditingIndex(null);
    } else {
      setDrafts((prev) => [...prev, data]);
    }
  };

  const handleEdit = (index: number) => {
    setEditingIndex(index);
  }

  const handleDelete = (index: number) => {
    setDrafts((prev) => prev.filter((_, i) => i !== index));
    if (editingIndex === index) {
      setEditingIndex(null);
    }
  }

  const handleSaveAll = async () => {
    try {
      for (const draft of drafts) {
        const uploadedImages = await Promise.all(
          draft.localImages.map((image) => newImagen(image))
        );
        
        const data = {
          ...draft,
          idsImagenes: uploadedImages.map((img) => img.id),
        };

        await newProducto(data);
      }

      setDrafts([]);
      navigate("/pieces");
    } catch (error) {
      console.error("Error saving drafts:", error);
    }
  };

  return (
    <div>
      <h3>Added pieces</h3>
      <ul>
        {drafts.map((draft, index) => {
          const isEditing = editingIndex === index;
          return (
            <li key={index} className={isEditing ? "editing" : ""}>
              {draft.localImages?.[0] && (
                <img
                  src={URL.createObjectURL(draft.localImages[0])}
                  alt={draft.nombre}
                  className="preview-image"
                />
              )}
              <strong>{draft.nombre}</strong> - ${draft.precio}
              <button onClick={() => handleEdit(index)} disabled={isEditing}>
                Edit
              </button>
              <button onClick={() => handleDelete(index)} disabled={isEditing}>
                Delete
              </button>
            </li>
          );
        })}
      </ul>

      <div style={{ marginTop: "1rem" }}>
        <button className="save-button" onClick={handleSaveAll} disabled={drafts.length === 0}> 
          Save All Pieces
        </button>
        <button className="save-button" onClick={handleSaveAll} disabled={drafts.length === 0}> 
          Save & Notify Subscribers
        </button>
        <button onClick={() => navigate("/pieces")}>
          Cancel
        </button>
      </div>

      <hr style={{ margin: "2rem 0" }} />

      <ProductForm 
        mode="create" 
        isEditingDraft={editingIndex !== null}
        initialData={editingIndex !== null ? drafts[editingIndex] : undefined}
        onAddToList={handleAddToList}
        onCancel={() => setEditingIndex(null)} 
      />
    </div>
  );
}
