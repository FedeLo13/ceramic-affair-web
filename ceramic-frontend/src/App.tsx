import { Routes, Route, Navigate } from "react-router-dom";
import MainLayout from "./layout/MainLayout";
import About from "./pages/About/About";
import Pieces from "./pages/Pieces/Pieces";
import PieceDetail from "./pages/PieceDetail/PieceDetail";
import Contact from "./pages/Contact/Contact";
import FindMe from "./pages/FindMe/FindMe";
import AdminLogin from "./pages/AdminLogin/AdminLogin";
import AdminProductNew from "./pages/Admin/AdminProductNew";
import AdminProductEdit from "./pages/Admin/AdminProductEdit";
import ProtectedRoute from "./components/ProtectedRoutes/ProtectedRoute";
import AdminManageCategories from "./pages/Admin/AdminManageCategories";
import Confirmacion from "./pages/Confirmation/Confirmation";
import { GoogleReCaptchaProvider } from "react-google-recaptcha-v3";
import AdminFindMeEdit from "./pages/Admin/AdminFindMeEdit";
import AdminFindMeNew from "./pages/Admin/AdminFindMeNew";
import PoliticaPrivacidad from "./pages/PolíticaPrivacidad/PrivacyPolicy";

function App() {
  return (
    <Routes>
      <Route path="/" element={<MainLayout />}>
        {/* Rutas públicas */}
        <Route index element={<Navigate to="/pieces" />} />
        <Route path="about" element={<About />} />
        <Route path="pieces" element={<Pieces showFilters={false} />} />
        <Route path="search" element={<Pieces showFilters={true} />} />
        <Route path="pieces/:id" element={<PieceDetail />} />
        <Route path="contact" element={
          <GoogleReCaptchaProvider reCaptchaKey='6Ley9IwrAAAAANfsyFpChaZHQLShQWdi7UwYBWxR'>
            <Contact />
          </GoogleReCaptchaProvider>
        } />
        <Route path="find-me" element={<FindMe />} />
        <Route path="privacy-policy" element={<PoliticaPrivacidad />} />
        
        {/* Rutas de administración */}
        <Route path="admin-login" element={<AdminLogin />} />
        <Route path="confirmation" element={<Confirmacion />} />
        {/* Rutas protegidas para administración */}
        <Route path="admin/products/new" element={
          <ProtectedRoute>
            <AdminProductNew />
          </ProtectedRoute>
        } />
        <Route path="admin/find-me/new" element={
          <ProtectedRoute>
            <AdminFindMeNew />
          </ProtectedRoute>
        } />
        <Route path="admin/products/edit/:id" element={
          <ProtectedRoute>
            <AdminProductEdit />
          </ProtectedRoute>
        } />
        <Route path="admin/find-me/edit/:id" element={
          <ProtectedRoute>
            <AdminFindMeEdit />
          </ProtectedRoute>
        } />
        <Route path="admin/categories" element={
          <ProtectedRoute>
            <AdminManageCategories />
          </ProtectedRoute>
        } />
      </Route>
    </Routes>
  );
}

export default App;