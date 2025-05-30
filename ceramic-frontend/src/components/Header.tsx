import { NavLink } from "react-router-dom";

export default function Header() {
    return (
        <header>
            <div className="logo">LOGO</div>
            <nav>
                <NavLink to="/about">About</NavLink>
                <NavLink to="/pieces">Pieces</NavLink>
                <NavLink to="/contact">Contact</NavLink>
                <NavLink to="/find-me">Find Me</NavLink>
                <a href="https://instagram.com" target="_blank" rel="noopener noreferrer">Instagram</a>
            </nav>
        </header>
    );
}