import { jwtDecode } from "jwt-decode";
import { createContext, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

interface jwtPayload {
    exp: number;
    sub: string;
}

interface AuthContextProps {
    token: string | null;
    isAuthenticated: boolean;
    login: (token: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextProps | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [token, setToken] = useState<string | null>(localStorage.getItem("token"));
    const navigate = useNavigate();

    // Verificar expiración al cargar la aplicación
    useEffect(() => {
        if (token && isTokenExpired(token)) {
            handleLogout();
        }
    }, []);

    // Use effect para verificar el estado del token (temporal para desarrollo)
    useEffect(() => {
        if (token && !isTokenExpired(token)) {
            console.log("✅ Admin logueado");
        } else {
            console.log("❌ Admin no logueado");
        }
    }, [token]);

    const handleLogin = (newToken: string) => {
        setToken(newToken);
        localStorage.setItem("token", newToken);
        
        // Auto logout si el token expira
        const { exp } = jwtDecode<jwtPayload>(newToken);
        const expirationTime = exp * 1000 - Date.now();

        setTimeout(() => {
            handleLogout();
        }, expirationTime);
    }

    const handleLogout = () => {
        setToken(null);
        localStorage.removeItem("token");
        navigate("/pieces"); // Redirigir a la página de piezas
    }

    const isTokenExpired = (token: string): boolean => {
        try {
            const { exp } = jwtDecode<jwtPayload>(token);
            return Date.now() >= exp * 1000;
        } catch {
            return true; // Si hay un error al decodificar, consideramos que el token está expirado
        }
    };

    return (
        <AuthContext.Provider value={{ token, isAuthenticated: !!token && !isTokenExpired(token), login: handleLogin, logout: handleLogout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
};