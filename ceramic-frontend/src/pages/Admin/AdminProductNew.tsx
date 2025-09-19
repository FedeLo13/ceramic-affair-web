import ProductForm from "../../components/ProductForm/ProductForm";
import { newProducto } from "../../api/productos";
import { useNavigate } from "react-router-dom";
import type { ProductoInputDTOWithImages } from "../../components/ProductForm/ProductoInputDTOWithImages";
import { useEffect, useState } from "react";
import { newImagen } from "../../api/imagenes";
import "./AdminProductNew.css";
import type { NewsletterDTO } from "../../types/newsletter.types";
import { getPlantilla, sendNewsletter, updatePlantilla } from "../../api/newsletters";
import type { ProductoInputDTO } from "../../types/producto.types";

export default function AdminProductNew() {
  const navigate = useNavigate();
  const [drafts, setDrafts] = useState<ProductoInputDTOWithImages[]>([]);
  const [editingIndex, setEditingIndex] = useState<number | null>(null);

  // Estados para el modal de la newsletter
  const [showNewsletterModal, setShowNewsletterModal] = useState(false);
  const [newsletter, setNewsletter] = useState<NewsletterDTO>({ asunto: "", mensaje: "" });
  const [loadingPlantilla, setLoadingPlantilla] = useState(false);
  const [sending, setSending] = useState(false);

  // Toast
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

  const handleSaveAll = async (toastMessage?: string) => {
    try {
      for (const draft of drafts) {
        const uploadedImages = await Promise.all(
          draft.localImages.map((image) => newImagen(image))
        );
        
        const productData: ProductoInputDTO = {
          nombre: draft.nombre,
          descripcion: draft.descripcion,
          precio: draft.precio,
          soldOut: draft.soldOut,
          idCategoria: draft.idCategoria,
          altura: draft.altura,
          anchura: draft.anchura,
          diametro: draft.diametro,
          idsImagenes: [...draft.idsImagenes, ...uploadedImages.map(img => img.id)],
        };
          

        await newProducto(productData);
      }

      setDrafts([]);
      navigate("/pieces", { state: { toastMessage: toastMessage ?? "Products saved successfully" } });
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
      setMessage("Template updated successfully");
    } catch (error) {
      console.error("Error updating template:", error);
    }
  };

  const handleSendNewsletter = async () => {
    setSending(true);
    try {
      await sendNewsletter(newsletter);
      setShowNewsletterModal(false);
      await handleSaveAll("Products saved and newsletter sent successfully");
    } catch (error) {
      console.error("Error sending newsletter:", error);
      setMessage("Error sending newsletter, please contact support");
    } finally {
      setSending(false);
    }
  };

  return (
    <div>
      <ProductForm 
        mode="create" 
        isEditingDraft={editingIndex !== null}
        initialData={editingIndex !== null ? drafts[editingIndex] : undefined}
        onAddToList={handleAddToList}
        onCancel={() => setEditingIndex(null)} 
        drafts={drafts}
        editingIndex={editingIndex}
        onEditDraft={handleEdit}
        onDeleteDraft={handleDelete}
        onSaveAll={handleSaveAll}
        onSaveAndNotify={handleSaveAndNotify}
        onGlobalCancel={() => navigate("/pieces")}
        disableGlobalActions={drafts.length === 0 || editingIndex !== null || loadingPlantilla}
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
