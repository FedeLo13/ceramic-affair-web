import { NavLink } from "react-router-dom";
import { FaInstagram } from "react-icons/fa";
import "./Footer.css";

export default function Footer() {
    return (
        <footer className="footer">
            <div className="footer-links">
                <NavLink to="/privacy-policy" className="footer-link">
                    Privacy Policy
                </NavLink>
                <NavLink to="/contact#newsletter" className="footer-link">
                    Newsletter
                </NavLink>
                <a
                    href="https://www.instagram.com/ceramic_affair/"
                    target="_blank"
                    rel="noopener noreferrer"
                    className="footer-link"
                >
                    <FaInstagram size={20} />
                </a>
            </div>
        </footer>
    );
}