import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import { FaInstagram, FaBars, FaCrown } from "react-icons/fa";
import "./Header.css";
import { useAuth } from "../../context/AuthContext";

export default function Header() {
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const [adminMenuOpen, setAdminMenuOpen] = useState(false);
    const { isAuthenticated, logout } = useAuth();

    useEffect(() => {
        if (sidebarOpen) {
            document.body.classList.add("no-scroll");
        } else {
            document.body.classList.remove("no-scroll");
        }
    }, [sidebarOpen]);

    return (
        <>
            {/* Header*/}
            <header className="header">
                {/*Logo de Ceramic Affair */}
                <div className="logo">
                    <img src="/images/CERAMIC_AFFAIR_logo.png" alt="Ceramic Affair Logo" />
                </div>

                {/*Enlaces de navegación */}
                <div className="nav-container">
                    <nav className="nav-links">
                        <NavLink to="/about">About</NavLink>
                        <NavLink to="/pieces">Pieces</NavLink>
                        <NavLink to="/search">Search</NavLink>
                        <NavLink to="/contact">Contact</NavLink>
                        <NavLink to="/find-me">Find Me</NavLink>
                    </nav>  
                </div>
            </header>

            {/* Sidebar */}
            <div className={`sidebar ${sidebarOpen ? "open" : ""}`}>
                {/* Imagen del sidebar */}
                <div className="sidebar-img">
                    <img src="/images/CERAMIC_AFFAIR_logo.png" alt="Sidebar Image" />
                </div>
                <div className="sidebar-content">
                    <NavLink to="/about" onClick={() => setSidebarOpen(false)}>About</NavLink>
                    <NavLink to="/pieces" onClick={() => setSidebarOpen(false)}>Pieces</NavLink>
                    <NavLink to="/search" onClick={() => setSidebarOpen(false)}>Search</NavLink>
                    <NavLink to="/contact" onClick={() => setSidebarOpen(false)}>Contact</NavLink>
                    <NavLink to="/find-me" onClick={() => setSidebarOpen(false)}>Find Me</NavLink>
                    <a href="https://www.instagram.com/ceramic_affair/" target="_blank" rel="noopener noreferrer" className="instagram-icon" aria-label="Instagram" onClick={() => setSidebarOpen(false)}>
                        <FaInstagram size={24} />
                    </a>

                    {/* Admin Menu (solo visible si está logueado) */}
                    {isAuthenticated && (
                        <div className="admin-menu">
                            <button
                                className="admin-toggle" 
                                onClick={() => setAdminMenuOpen(!adminMenuOpen)}>
                                <FaCrown size={24} /> Admin Menu
                            </button>
                            {adminMenuOpen && (
                                <div className="sidebar-admin-dropdown">
                                    <NavLink to="/admin/products/new" onClick={() => setSidebarOpen(false)}>Add Pieces</NavLink>
                                    <NavLink to="/admin/find-me/new" onClick={() => setSidebarOpen(false)}>Add Find Me Post</NavLink>
                                    <NavLink to="/admin/categories" onClick={() => setSidebarOpen(false)}>Manage Categories</NavLink>
                                    <button className="logout-btn" onClick={() => { logout(); setSidebarOpen(false); }}>
                                        Logout
                                    </button>
                                </div>
                            )}
                        </div>
                    )}
                    <NavLink to="/privacy-policy" onClick={() => setSidebarOpen(false)} className="privacy-policy-link">Privacy Policy</NavLink>
                </div>
            </div>

            {/*Botón de barra lateral */}
            <button className="sidebar-toggle" onClick={() => setSidebarOpen(!sidebarOpen)} aria-label="Toggle Sidebar">
                <FaBars size={24} />
            </button>

            {/* Iconos del header */}
            <div className="header-icons">
                    <a href="https://www.instagram.com/ceramic_affair/" target="_blank" rel="noopener noreferrer" className="instagram-icon">
                        <FaInstagram size={24} />
                    </a>
            </div>

            {/* Overlay */}
            {sidebarOpen && <div className="overlay" onClick={() => setSidebarOpen(false)} />}
        </>
    );
}
// Este componente Header renderiza un encabezado con un logo y enlaces de navegación.
// Utiliza NavLink de react-router-dom para manejar la navegación interna y un enlace externo a Instagram.