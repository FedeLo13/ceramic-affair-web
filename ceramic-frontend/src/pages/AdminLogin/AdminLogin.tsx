import { useState } from "react";
import { useNavigate } from "react-router-dom";
import type { LoginDTO } from "../../types/login.types";
import { login } from "../../api/login";
import { useAuth } from "../../context/AuthContext";

export default function AdminLogin() {
    const { login: doLogin} = useAuth();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
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
            <h1>Admin Login</h1>
            <form className="admin-login-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="email">Email</label>
                    <input 
                        type="text" 
                        id="email" 
                        name="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)} 
                        required />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password</label>
                    <input 
                        type="password" 
                        id="password" 
                        name="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)} 
                        required />
                </div>
                {error && <p className="error-message">{error}</p>}
                <button type="submit">Login</button>
            </form>
        </div>
    );
}