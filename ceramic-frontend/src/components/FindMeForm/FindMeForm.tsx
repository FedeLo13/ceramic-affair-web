import { useState, useEffect } from "react";
import type { FindMePostInputDTO } from "../../types/findmepost.types";
import * as L from "leaflet";
import { MapContainer, Marker, TileLayer, useMap, useMapEvents } from "react-leaflet";
import { useNavigate } from "react-router-dom";
import "leaflet/dist/leaflet.css";
import "./FindMeForm.css";

delete (L.Icon.Default.prototype as any)._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png",
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  shadowUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
});

interface FindMeFormProps {
  mode: "create" | "edit";
  initialData?: FindMePostInputDTO;
  onSave?: (data: FindMePostInputDTO) => void;
  onSaveAndNotify?: (data: FindMePostInputDTO) => void;
  loadingNewsletter?: boolean;
}

// Función util para convertir fecha ISO UTC -> formato local para datetime-local
function toLocalDateTimeString(dateStr: string) {
  const d = new Date(dateStr);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  const hours = String(d.getHours()).padStart(2, "0");
  const minutes = String(d.getMinutes()).padStart(2, "0");
  return `${year}-${month}-${day}T${hours}:${minutes}`;
}

export default function FindMeForm({ mode, initialData, onSave, onSaveAndNotify, loadingNewsletter }: FindMeFormProps) {
  const [titulo, setTitulo] = useState(initialData?.titulo || "");
  const [descripcion, setDescripcion] = useState(initialData?.descripcion || "");
  const [fechaInicio, setFechaInicio] = useState(initialData?.fechaInicio? toLocalDateTimeString(initialData.fechaInicio) : "");
  const [fechaFin, setFechaFin] = useState(initialData?.fechaFin? toLocalDateTimeString(initialData.fechaFin) : "");
  const [latitud, setLatitud] = useState(initialData?.latitud || 36.5167); // Cádiz por defecto
  const [longitud, setLongitud] = useState(initialData?.longitud || -5.9667); // Cádiz por defecto

  const [search, setSearch] = useState("");
  const [debouncedSearch, setDebouncedSearch] = useState("");
  const [searchResults, setSearchResults] = useState<{ display_name: string; lat: string; lon: string}[]>([]);
  const [markerMode, setMarkerMode] = useState(false);
  const [hoverLatLng, setHoverLatLng] = useState<[number, number] | null>(null);

  const [errors, setErrors] = useState<{ titulo?: string; fechaInicio?: string; fechaFin?: string }>({});
  const [isFormValid, setIsFormValid] = useState(false);

  const navigate = useNavigate();

  // Debounce para la búsqueda (500 ms)
  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedSearch(search);
    }, 500);

    return () => {
      clearTimeout(handler);
    };
  }, [search]);

  // Buscar en Nominatim
  useEffect(() => {
    const fetchPlaces = async () => {
      if (debouncedSearch.length < 3) {
        setSearchResults([]);
        return;
      }

      try {
        const response = await fetch(
            `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(
                debouncedSearch
            )}
            &addressdetails=1&limit=5`
        );
        const data = await response.json();
        setSearchResults(data);
      } catch (error) {
        console.error("Error fetching places:", error);
      }
    };

    fetchPlaces();
  }, [debouncedSearch]);

  // Validación básica
  const getValidationErrors = () => {
    const newErrors: typeof errors = {};
    if (!titulo.trim()) newErrors.titulo = "El título es obligatorio.";
    if (!fechaInicio) newErrors.fechaInicio = "La fecha de inicio es obligatoria.";
    if (!fechaFin) newErrors.fechaFin = "La fecha de fin es obligatoria.";
    return newErrors;
  };

  useEffect(() => {
    const errs = getValidationErrors();
    setErrors(errs);
    setIsFormValid(Object.keys(errs).length === 0);
  }, [titulo, fechaInicio, fechaFin]);

  // Componente para detectar click en mapa y mover el marcador
  function LocationMarker() {
    useMapEvents({
      mousemove(e) {
        if (markerMode) setHoverLatLng([e.latlng.lat, e.latlng.lng]);
      },
      click(e) {
        if (markerMode) {
          setLatitud(e.latlng.lat);
          setLongitud(e.latlng.lng);
          setMarkerMode(false);
          setHoverLatLng(null);
        }
      },
    });

    return (
      <>
        {/* marcador real */}
        <Marker
          position={[latitud, longitud]}
          draggable
          eventHandlers={{
            dragend(e) {
              const position = e.target.getLatLng();
              setLatitud(position.lat);
              setLongitud(position.lng);
            },
          }}
        />
        {/* marcador fantasma */}
        {markerMode && hoverLatLng && (
          <Marker
            position={hoverLatLng}
            opacity={0.5}
            interactive={false}
            icon={new L.Icon({
              iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
              shadowUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
              iconSize: [25, 41],
              iconAnchor: [12, 41],
              className: "ghost-marker"
            })}
          />
        )}
      </>
    );
  }

  function CenterMarkerButton({ lat, lng }: { lat: number; lng: number }) {
    const map = useMap();

    return (
      <button
        className="center-marker-btn"
        type="button"
        onClick={() => {
          map.flyTo([lat, lng], 15, { duration: 1.2 });
        }}
      >
        📍
      </button>
    );
  }

  const handleSave = (notify = false) => {
    const errs = getValidationErrors();
    if (Object.keys(errs).length > 0) {
      setErrors(errs);
      return;
    }

    const data: FindMePostInputDTO = {
      titulo,
      descripcion,
      fechaInicio: fechaInicio,
      fechaFin: fechaFin,
      latitud,
      longitud,
    };

    if (notify && onSaveAndNotify) {
      onSaveAndNotify(data);
    } else if (onSave) {
      onSave(data);
    }
  };

  return (
    <form className="findme-form" onSubmit={(e) => e.preventDefault()}>
      <h2>{mode === "create" ? "New Find Me Post" : "Edit Find Me Post"}</h2>

      <div className="form-group">
        <label>Title</label>
        <input type="text" value={titulo} onChange={(e) => setTitulo(e.target.value)} />
        {errors.titulo && <p className="error-message">{errors.titulo}</p>}
      </div>

      <div className="form-group">
        <label>Description</label>
        <textarea value={descripcion} onChange={(e) => setDescripcion(e.target.value)} />
      </div>

      <div className="form-row">
        <div>
          <label>Start Date</label>
          <input type="datetime-local" value={fechaInicio} onChange={(e) => setFechaInicio(e.target.value)} />
          {errors.fechaInicio && <p className="error-message">{errors.fechaInicio}</p>}
        </div>
        <div>
          <label>End Date</label>
          <input type="datetime-local" value={fechaFin} onChange={(e) => setFechaFin(e.target.value)} />
          {errors.fechaFin && <p className="error-message">{errors.fechaFin}</p>}
        </div>
      </div>

      <div className="form-group">
        <label>Location</label>
        <input type="text" value={search} onChange={(e) => setSearch(e.target.value)} placeholder="Search for a location" />
        {searchResults.length > 0 && (
            <ul className="search-results">
              {searchResults.map((r, i) => (
                <li 
                    key={i} 
                    onClick={() => {
                        setLatitud(parseFloat(r.lat));
                        setLongitud(parseFloat(r.lon));
                        setSearchResults([]);
                        setSearch(r.display_name);
                    }}
                >
                  {r.display_name}
                </li>
              ))}
            </ul>
        )}
      </div>

      <div className={`map-container ${markerMode ? "marker-mode" : ""}`}>
        <MapContainer
          center={[latitud, longitud]}
          zoom={8}
          style={{ width: "100%", height: "100%" }}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          <LocationMarker />
          <CenterMarkerButton lat={latitud} lng={longitud} />
        </MapContainer>
        <button
          type="button"
          className={`marker-toggle-btn ${markerMode ? "active" : ""}`}
          onClick={() => {
            setMarkerMode((prev) => !prev);
            setHoverLatLng(null);
          }}
        >
          {markerMode ? "Cancel" : "Place Marker"}
        </button>
      </div>

      <div className="button-group">
        {mode === "create" && (
          <>
            <button type="button" onClick={() => handleSave(false)} disabled={!isFormValid}>
              Save Post
            </button>
            <button type="button" onClick={() => handleSave(true)} disabled={!isFormValid || loadingNewsletter}>
              {loadingNewsletter ? "Loading..." : "Save and Notify Subscribers"}
            </button>
          </>
        )}
        {mode === "edit" && (
          <button type="button" onClick={() => handleSave(false)} disabled={!isFormValid}>
            Save Changes
          </button>
        )}
        <button type="button" className="cancel-button" onClick={() => navigate(-1)}>
          Cancel
        </button>
      </div>
    </form>
  );
}