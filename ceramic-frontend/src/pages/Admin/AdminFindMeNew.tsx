import { useState } from "react";
import { useNavigate } from "react-router-dom";
import FindMeForm from "../../components/FindMeForm/FindMeForm";
import {
  getPlantilla,
  sendNewsletter,
  updatePlantilla
} from "../../api/newsletters";
import type { NewsletterDTO } from "../../types/newsletter.types";
import type { FindMePostInputDTO } from "../../types/findmepost.types";
import { newFindMePost } from "../../api/findmeposts";

export default function AdminFindMeNew() {
  const navigate = useNavigate();

  // Newsletter states
  const [showNewsletterModal, setShowNewsletterModal] = useState(false);
  const [newsletter, setNewsletter] = useState<NewsletterDTO>({
    asunto: "",
    mensaje: ""
  });
  const [loadingPlantilla, setLoadingPlantilla] = useState(false);
  const [sending, setSending] = useState(false);

  // Guardar post sin notificar
  const handleSavePost = async (data: FindMePostInputDTO) => {
    try {
      await newFindMePost(data);
      navigate(-1);
    } catch (error) {
      console.error("Error saving FindMe post:", error);
    }
  };

  // Guardar y abrir modal newsletter
  const handleSaveAndNotify = async (data: FindMePostInputDTO) => {
    try {
      // Primero guardamos el post
      await newFindMePost(data);

      // Luego cargamos la plantilla y mostramos el modal
      setLoadingPlantilla(true);
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
      navigate(-1);
    } catch (error) {
      console.error("Error sending newsletter:", error);
    } finally {
      setSending(false);
    }
  };

  return (
    <div>
      <h2>Create New Find Me Post</h2>

      <FindMeForm
        mode="create"
        onSave={handleSavePost}
        onSaveAndNotify={handleSaveAndNotify}
        loadingNewsletter={loadingPlantilla}
      />

      {/* Modal Newsletter */}
      {showNewsletterModal && (
        <div className="modal-backdrop">
          <div className="modal">
            <h2>Send a Newsletter</h2>
            <label>
              Subject:
              <input
                type="text"
                value={newsletter.asunto}
                onChange={(e) =>
                  setNewsletter({ ...newsletter, asunto: e.target.value })
                }
              />
            </label>
            <label>
              Message:
              <textarea
                rows={6}
                value={newsletter.mensaje}
                onChange={(e) =>
                  setNewsletter({ ...newsletter, mensaje: e.target.value })
                }
              />
            </label>

            <div className="modal-actions">
              <button onClick={handleUpdatePlantilla}>
                Save current as template
              </button>
              <button onClick={handleSendNewsletter} disabled={sending}>
                {sending
                  ? "Sending..."
                  : "Send Newsletter & Finish"}
              </button>
              <button onClick={() => setShowNewsletterModal(false)}>
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}