import { useEffect, useState } from "react";
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
import "./AdminFindMeNew.css";

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
  const [pendingPostData, setPendingPostData] = useState<FindMePostInputDTO | null>(null);

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

  // Guardar post sin notificar
  const handleSavePost = async (data: FindMePostInputDTO) => {
    try {
      await newFindMePost(data);
      navigate("/pieces", { state: { toastMessage: "Post created successfully!" } });
    } catch (error) {
      console.error("Error saving FindMe post:", error);
      setMessage("Error saving FindMe post");
    }
  };

  // Guardar y abrir modal newsletter
  const handleSaveAndNotify = async (data: FindMePostInputDTO) => {
    try {
      setPendingPostData(data);
      setLoadingPlantilla(true);
      const plantilla = await getPlantilla();
      setNewsletter(plantilla);
      setShowNewsletterModal(true);
    } catch (error) {
      console.error("Error saving and notifying:", error);
      setMessage("Error while preparing newsletter, please contact support");
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
      setMessage("Error updating template");
    }
  };

  const handleSendNewsletter = async () => {
    if (!pendingPostData) {
      setMessage("No post data available to send");
      return;
    }

    setSending(true);
    try {
      await newFindMePost(pendingPostData);
      await sendNewsletter(newsletter);
      setShowNewsletterModal(false);
      setPendingPostData(null);
      navigate("/pieces", { state: { toastMessage: "Post created and newsletter sent successfully!" } });
    } catch (error) {
      console.error("Error sending newsletter:", error);
      setMessage("Error sending newsletter");
    } finally {
      setSending(false);
    }
  };

  return (
    <div>
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
                rows={10}
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