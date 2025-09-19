import { useSearchParams } from "react-router-dom";

export default function Confirmacion() {
  const [searchParams] = useSearchParams();
  const status = searchParams.get("status");

  const mensaje = (() => {
    switch (status) {
      case "not_found":
        return "We couldn't find your subscription. Please try again later.";
      case "expired":
        return "The link has expired. Please request a new one.";
      case "subscribed":
        return "Your subscription has been confirmed!";
      case "unsubscribed":
        return "Your subscription has been successfully canceled.";
      default:
        return "An error occurred while processing your request.";
    }
  })();

  return (
    <div style={{ textAlign: "center", marginTop: "3rem" }}>
      <h1>{mensaje}</h1>
    </div>
  );
}
