import { Routes, Route, Navigate } from "react-router-dom";
import MainLayout from "./layout/MainLayout";
import About from "./pages/About";
import Pieces from "./pages/Pieces";
import Contact from "./pages/Contact";
import FindMe from "./pages/FindMe";

function App() {
  return (
    <Routes>
      <Route path="/" element={<MainLayout />}>
        <Route index element={<Navigate to="/pieces" />} />
        <Route path="about" element={<About />} />
        <Route path="pieces" element={<Pieces />} />
        <Route path="contact" element={<Contact />} />
        <Route path="find-me" element={<FindMe />} />
        {/* Add more routes as needed */}
      </Route>
    </Routes>
  );
}

export default App;