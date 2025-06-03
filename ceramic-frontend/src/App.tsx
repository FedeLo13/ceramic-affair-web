import { Routes, Route, Navigate } from "react-router-dom";
import MainLayout from "./layout/MainLayout";
import About from "./pages/About/About";
import Pieces from "./pages/Pieces/Pieces";
import PieceDetail from "./pages/PieceDetail/PieceDetail";
import Contact from "./pages/Contact/Contact";
import FindMe from "./pages/FindMe";

function App() {
  return (
    <Routes>
      <Route path="/" element={<MainLayout />}>
        <Route index element={<Navigate to="/pieces" />} />
        <Route path="about" element={<About />} />
        <Route path="pieces" element={<Pieces />} />
        <Route path="pieces/:id" element={<PieceDetail />} />
        <Route path="contact" element={<Contact />} />
        <Route path="find-me" element={<FindMe />} />
        {/* Añadir más rutas aquí según sea necesario */}	
      </Route>
    </Routes>
  );
}

export default App;