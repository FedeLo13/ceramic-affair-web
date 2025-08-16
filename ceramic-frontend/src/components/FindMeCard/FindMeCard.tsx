import { MapContainer, TileLayer, Marker, useMap } from "react-leaflet";
import * as L from "leaflet";
import { useEffect } from "react";
import "leaflet/dist/leaflet.css";
import type { FindMePostOutputDTO } from "../../types/findmepost.types";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { deleteFindMePost } from "../../api/findmeposts";

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
      map.setZoomAround(markerLatLng, newZoom);
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
  return d.toLocaleString("es-ES", {
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
    <div style={{display: "flex", alignItems: "center", gap: "16px"}}>
      {/* Mapa y Título */}
      <div style={{ width: "300px", height: "300px" }}>
        {/* Título */}
        <div style={{ minWidth: "120px", fontWeight: "bold" }}>
          {titulo} <br />
          {fechaInicioDate} → {fechaFinDate}
        </div>

        {/* Mapa */}
        <a href={googleMapsUrl} target="_blank" rel="noopener noreferrer" style={{ display: "block", width: "100%", height: "100%" }}>
            <MapContainer
                center={[latitud, longitud]}
                zoom={13}
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
      <div style={{ minWidth: "120px" }}>
        {descripcion}
        <br />
        <a href={googleMapsUrl} target="_blank" rel="noopener noreferrer" style={{ fontSize: "0.9em", color: "blue", textDecoration: "underline" }}>
          ← Click here to see the location in Google Maps
        </a>

        {isAuthenticated && (
          <div style={{ marginTop: "10px", display: "flex", gap: "8px" }}>
            <button onClick={handleEdit} style={{ padding: "6px 12px", backgroundColor: "orange", color: "white", border: "none", borderRadius: "4px" }}>
              Edit
            </button>
            <button onClick={handleDelete} style={{ padding: "6px 12px", backgroundColor: "red", color: "white", border: "none", borderRadius: "4px" }}>
              Delete
            </button>
          </div>
        )}
      </div>
    </div>
  );
}