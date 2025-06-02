import { NavLink } from "react-router-dom";

export default function Header() {
    return (
        <header>
            <div className="logo">
                <img src="/images/CERAMIC_AFFAIR_logo.png" alt="Ceramic Affair Logo" />
            </div>
            <nav>
                <NavLink to="/about">About</NavLink>
                <NavLink to="/pieces">Pieces</NavLink>
                <NavLink to="/contact">Contact</NavLink>
                <NavLink to="/find-me">Find Me</NavLink>
                <a href="https://www.instagram.com/ceramic_affair/" target="_blank" rel="noopener noreferrer">Instagram</a>
            </nav>
        </header>
    );
}
// Este componente Header renderiza un encabezado con un logo y enlaces de navegación.
// Utiliza NavLink de react-router-dom para manejar la navegación interna y un enlace externo a Instagram.