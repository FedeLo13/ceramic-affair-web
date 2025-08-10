import { useSearchParams } from "react-router-dom";

export default function Confirmacion() {
  const [searchParams] = useSearchParams();
  const status = searchParams.get("status");

  const mensaje = (() => {
    switch (status) {
      case "not_found":
        return "No hemos encontrado tu suscripción. Inténtalo de nuevo más tarde.";
      case "expired":
        return "El enlace ha expirado. Por favor, solicita uno nuevo.";
      case "subscribed":
        return "¡Tu suscripción ha sido confirmada!";
      case "unsubscribed":
        return "Tu suscripción ha sido cancelada correctamente.";
      default:
        return "Ocurrió un error al procesar tu solicitud.";
    }
  })();

  return (
    <div style={{ textAlign: "center", marginTop: "3rem" }}>
      <h1>{mensaje}</h1>
    </div>
  );
}
