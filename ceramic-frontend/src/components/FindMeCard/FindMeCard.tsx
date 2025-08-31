import { MapContainer, TileLayer, Marker, useMap } from "react-leaflet";
import * as L from "leaflet";
import { useEffect } from "react";
import "leaflet/dist/leaflet.css";
import type { FindMePostOutputDTO } from "../../types/findmepost.types";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { deleteFindMePost } from "../../api/findmeposts";
import "./FindMeCard.css"
import { FaEdit, FaTrash } from "react-icons/fa";

delete (L.Icon.Default.prototype as any)._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png",
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  shadowUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
});

interface FindMeCardProps {
  key: number;
  post: FindMePostOutputDTO;
}

function CenteredMarker({ latitud, longitud }: { latitud: number; longitud: number }) {
  const map = useMap();

  useEffect(() => {
    const markerLatLng = L.latLng(latitud, longitud);
    map.setView(markerLatLng, map.getZoom(), { animate: false });

    // Desactivar arrastre y otros zooms
    map.dragging.disable();
    map.touchZoom.disable();
    map.doubleClickZoom.disable();
    map.boxZoom.disable();
    map.keyboard.disable();

    // Interceptar scroll para hacer zoom hacia el marcador
    const onWheel = (e: WheelEvent) => {
      e.preventDefault();
      const zoomDelta = e.deltaY > 0 ? -1 : 1;
      const newZoom = Math.min(Math.max(map.getZoom() + zoomDelta, map.getMinZoom()), map.getMaxZoom());
      map.setZoom(newZoom);
      map.panTo(markerLatLng, { animate: false });
    };


    map.getContainer().addEventListener("wheel", onWheel, { passive: false });
    return () => {
      map.getContainer().removeEventListener("wheel", onWheel);
    };
  }, [latitud, longitud, map]);

  return <Marker position={[latitud, longitud]} />;
}

function formatLocal(dateStr: string) {
  const d = new Date(dateStr);
  return d.toLocaleString("en-US", {
    weekday: "short",   // "lun"
    day: "2-digit",     // "22"
    month: "short",     // "may"
    year: "numeric",    // "2024"
    hour: "2-digit",
    minute: "2-digit"
  });
}

export default function FindMeCard({ post }: FindMeCardProps) {
  const { titulo, descripcion, fechaInicio, fechaFin, latitud, longitud } = post;
  const { isAuthenticated } = useAuth(); // Hook de autenticación
  const navigate = useNavigate();

  // Convertir fechas a objetos Date
  const fechaInicioDate = formatLocal(fechaInicio);
  const fechaFinDate = formatLocal(fechaFin);

  const googleMapsUrl = `https://www.google.com/maps?q=${latitud},${longitud}`;

  const handleEdit = () => {
    navigate(`/admin/find-me/edit/${post.id}`)
  };

  const handleDelete = async () => {
     if (confirm("Delete post?")) {
       try {
        await deleteFindMePost(post.id)
        window.location.reload();
       } catch (error) {
         console.error("Error deleting post:", error);
       }
    }
  };

  return (
    <div className="findme-card">
      {/* Título */}
      <div className="findme-card-title">
        {titulo} - {fechaInicioDate.toUpperCase()} → {fechaFinDate.toUpperCase()}
      </div>


      <div className="findme-card-body">
        {/* Mapa */}
        <div className="findme-card-map">
          <a href={googleMapsUrl} target="_blank" rel="noopener noreferrer" style={{ display: "block", width: "100%", height: "100%" }}>
              <MapContainer
                  center={[latitud, longitud]}
                  zoom={13}
                  scrollWheelZoom={false}
                  style={{ width: "100%", height: "100%" }}
              >
                  <TileLayer
                  attribution='&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a>'
                  url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                  />
                  <CenteredMarker latitud={latitud} longitud={longitud} />
              </MapContainer>
          </a>
        </div>

        {/* Descripción */}
        <div className="findme-card-content">
          <div className="findme-card-description">
            {descripcion}
            <br />
          </div>
          <div className="findme-card-footer">
            {isAuthenticated && (
              <div className="findme-admin-actions">
                <button onClick={handleEdit} className="findme-admin-button findme-admin-button-edit">
                  <FaEdit /> Edit
                </button>
                <button onClick={handleDelete} className="findme-admin-button findme-admin-button-delete">
                  <FaTrash /> Delete
                </button>
              </div>
            )}
            <a href={googleMapsUrl} target="_blank" rel="noopener noreferrer" className="findme-card-map-link">
              Click here to see the location in Google Maps
            </a>
          </div>
        </div>
      </div>
    </div>
  );
}