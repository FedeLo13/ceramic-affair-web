import ProductForm from "../../components/ProductForm/ProductForm";
import { newProducto } from "../../api/productos";
import { useNavigate } from "react-router-dom";
import type { ProductoInputDTOWithImages } from "../../components/ProductForm/ProductoInputDTOWithImages";
import { useState } from "react";
import { newImagen } from "../../api/imagenes";
import "./AdminProductNew.css";
import type { NewsletterDTO } from "../../types/newsletter.types";
import { getPlantilla, sendNewsletter, updatePlantilla } from "../../api/newsletters";

export default function AdminProductNew() {
  const navigate = useNavigate();
  const [drafts, setDrafts] = useState<ProductoInputDTOWithImages[]>([]);
  const [editingIndex, setEditingIndex] = useState<number | null>(null);

  // Estados para el modal de la newsletter
  const [showNewsletterModal, setShowNewsletterModal] = useState(false);
  const [newsletter, setNewsletter] = useState<NewsletterDTO>({ asunto: "", mensaje: "" });
  const [loadingPlantilla, setLoadingPlantilla] = useState(false);
  const [sending, setSending] = useState(false);

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

  const handleSaveAndNotify = async () => {
    setLoadingPlantilla(true);
    try {
      const plantilla = await getPlantilla();
      setNewsletter(plantilla);
      setShowNewsletterModal(true);
    } catch (error) {
      console.error("Error saving and notifying:", error);
    } finally {
      setLoadingPlantilla(false);
    }
  };

  const handleUpdatePlantilla = async () => {
    try {
      await updatePlantilla(newsletter);
      alert("Template updated successfully");
    } catch (error) {
      console.error("Error updating template:", error);
    }
  };

  const handleSendNewsletter = async () => {
    setSending(true);
    try {
      await sendNewsletter(newsletter);
      alert("Newsletter sent successfully");
      setShowNewsletterModal(false);
      await handleSaveAll(); // Guardar todos los cambios tras enviar
    } catch (error) {
      console.error("Error sending newsletter:", error);
    } finally {
      setSending(false);
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
        <button className="save-button" onClick={handleSaveAndNotify} disabled={drafts.length === 0 || loadingPlantilla}> 
          {loadingPlantilla ? "Loading..." : "Save & Notify Subscribers"}
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

      {/*Modal Newsletter */}
      {showNewsletterModal && (
        <div className="modal-backdrop">
          <div className="modal">
            <h2>Send a Newsletter</h2>
            <label>
              Subject:
              <input
                type="text"
                value={newsletter.asunto}
                onChange={(e) => setNewsletter({ ...newsletter, asunto: e.target.value })}
              />
            </label>
            <label>
              Message:
              <textarea
                rows={6}
                value={newsletter.mensaje}
                onChange={(e) => setNewsletter({ ...newsletter, mensaje: e.target.value })}
              />
              </label>

              <div className="modal-actions">
                <button onClick={handleUpdatePlantilla}>Save current as template</button>
                <button onClick={handleSendNewsletter} disabled={sending}>
                  {sending ? "Sending..." : "Send Newsletter & Save Products"}
                </button>
                <button onClick={() => setShowNewsletterModal(false)}>Cancel</button>
              </div>
            </div>
          </div>
        )}
      </div>
  );
}
