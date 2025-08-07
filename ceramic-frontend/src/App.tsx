import { Routes, Route, Navigate } from "react-router-dom";
import MainLayout from "./layout/MainLayout";
import About from "./pages/About/About";
import Pieces from "./pages/Pieces/Pieces";
import PieceDetail from "./pages/PieceDetail/PieceDetail";
import Contact from "./pages/Contact/Contact";
import FindMe from "./pages/FindMe";
import AdminLogin from "./pages/AdminLogin/AdminLogin";
import AdminProductNew from "./pages/Admin/AdminProductNew";
import AdminProductEdit from "./pages/Admin/AdminProductEdit";
import ProtectedRoute from "./components/ProtectedRoutes/ProtectedRoute";

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
        <Route path="admin-login" element={<AdminLogin />} />
        {/* Rutas protegidas para administración */}
        <Route path="admin/products/new" element={
          <ProtectedRoute>
            <AdminProductNew />
          </ProtectedRoute>
        } />
        <Route path="admin/products/edit/:id" element={
          <ProtectedRoute>
            <AdminProductEdit />
          </ProtectedRoute>
        } />
      </Route>
    </Routes>
  );
}

export default App;