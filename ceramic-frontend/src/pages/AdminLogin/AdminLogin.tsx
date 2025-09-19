import { useState } from "react";
import { useNavigate } from "react-router-dom";
import type { LoginDTO } from "../../types/login.types";
import { login } from "../../api/login";
import { useAuth } from "../../context/AuthContext";
import "./AdminLogin.css"

export default function AdminLogin() {
    const { login: doLogin} = useAuth();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

        const trimmedEmail = email.trim();
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(trimmedEmail)) {
            setError("Please enter a valid email address.");
            return;
        }

        try {
            const res = await login({ email, password } as LoginDTO);
            doLogin(res.token);
            navigate("/pieces"); // Redirigir a la página de piezas después del login exitoso
        } catch {
            setError("Invalid email or password. Please try again.");
        }
    };

    return (
        <div className="admin-login">
            <form className="admin-login-form" onSubmit={handleSubmit}>
                <div className="login-form-group">
                    <label htmlFor="email" className="login-label">Email</label>
                    <input 
                        type="email" 
                        id="email" 
                        name="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        onKeyDown={(e) => {
                            if (e.key === " ") {
                                e.preventDefault();
                            }
                        }}
                        onPaste={(e) => {
                            const pasted = e.clipboardData.getData("text");
                            if (/\s/.test(pasted)) {
                                e.preventDefault();
                            }
                        }}
                        required />
                </div>
                <div className="login-form-group">
                    <label htmlFor="password" className="login-label">Password</label>
                    <input 
                        type="password" 
                        id="password" 
                        name="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)} 
                        required />
                </div>
                {error && <p className="error-message">{error}</p>}
                <button type="submit" className="login-button">Login</button>
            </form>
        </div>
    );
}