import { useState } from "react";

interface ModalLoginProps {
  setShowModal: (x: boolean) => void;
}

// Claves para localStorage
const CREDENTIALS_KEY = 'user_credentials';

// Interfaz para las credenciales
interface UserCredentials {
  username: string;
  password: string;
}

// Función para guardar credenciales
export function saveCredentials(username: string, password: string): void {
  const credentials: UserCredentials = { username, password };
  localStorage.setItem(CREDENTIALS_KEY, JSON.stringify(credentials));
}

// Función para limpiar credenciales
export function clearCredentials(): void {
  localStorage.removeItem(CREDENTIALS_KEY);
}

// Función para verificar si hay credenciales guardadas
export function isUserLoggedIn(): boolean {
  if (typeof window === 'undefined') return false; // Para SSR
  
  const stored = localStorage.getItem(CREDENTIALS_KEY);
  if (stored) {
    try {
      const credentials: UserCredentials = JSON.parse(stored);
      return credentials.username !== "" && credentials.password !== "";
    } catch (error) {
      console.error("Error parsing stored credentials:", error);
      return false;
    }
  }
  return false;
}

// Función para obtener credenciales
export function getGlobalCredentials(): UserCredentials {
  if (typeof window === 'undefined') return { username: "", password: "" };
  
  const stored = localStorage.getItem(CREDENTIALS_KEY);
  if (stored) {
    try {
      return JSON.parse(stored);
    } catch (error) {
      console.error("Error parsing stored credentials:", error);
      return { username: "", password: "" };
    }
  }
  return { username: "", password: "" };
}

export default function ModalLogin({ setShowModal }: ModalLoginProps) {
  const [isLogin, setIsLogin] = useState(true);
  const [loginData, setLoginData] = useState({ username: "", password: "" });
  const [registerData, setRegisterData] = useState({
    username: "",
    email: "",
    password: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const API_BASE_URL = "http://localhost:8080/api";

  const handleLoginSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: loginData.username,
          password: loginData.password,
        }),
      });

      if (response.ok) {
        const data = await response.json();
        console.log("Login exitoso:", data);
        
        // Guardar credenciales en localStorage
        saveCredentials(loginData.username, loginData.password);
        
        // Cerrar modal después de login exitoso
        setShowModal(false);
        
        // Limpiar formulario
        setLoginData({ username: "", password: "" });
        
        // Emitir evento personalizado para notificar el login
        window.dispatchEvent(new CustomEvent('authChange', { detail: { isLoggedIn: true } }));
        
      } else if (response.status === 401) {
        setError("Credenciales inválidas. Por favor, verifica tu usuario y contraseña.");
      } else {
        setError("Error en el servidor. Intenta nuevamente.");
      }
    } catch (err) {
      console.error("Error en login:", err);
      setError("Error de conexión. Verifica tu conexión a internet.");
    } finally {
      setLoading(false);
    }
  };

  const handleRegisterSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(registerData),
      });

      if (response.status === 201) {
        console.log("Registro exitoso");
        
        // Guardar credenciales en localStorage
        saveCredentials(registerData.username, registerData.password);
        
        // Cerrar modal después de registro exitoso
        setShowModal(false);
        
        // Limpiar formulario
        setRegisterData({ username: "", email: "", password: "" });
        
        // Emitir evento personalizado para notificar el registro
        window.dispatchEvent(new CustomEvent('authChange', { detail: { isLoggedIn: true } }));
        
      } else if (response.status === 400) {
        const errorData = await response.json();
        setError(errorData.message || "El usuario ya existe o datos inválidos.");
      } else {
        setError("Error en el servidor. Intenta nuevamente.");
      }
    } catch (err) {
      console.error("Error en registro:", err);
      setError("Error de conexión. Verifica tu conexión a internet.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/[.83]">
      <div className="relative bg-white rounded-4xl shadow-lg w-full max-w-md mx-auto px-8 pb-14 pt-16">
        {/* Botón de cierre */}
        <button
          className="absolute top-8 right-8 text-gray-400 hover:text-black cursor-pointer transition-colors"
          onClick={() => setShowModal(false)}
          disabled={loading}
        >
          <i className="fa-solid fa-x"></i>
        </button>

        {/* Logo */}
        <div className="flex justify-center mb-4">
          <img src="/logo_modal.png" alt="Logo de la app" width={65} />
        </div>

        {/* Pestañas */}
        <div className="flex justify-center gap-4 mb-6">
          <button
            className={`px-6 py-2 font-bold rounded-lg transition ${
              isLogin
                ? "bg-black text-white"
                : "bg-gray-200 text-gray-600 hover:bg-gray-300"
            }`}
            onClick={() => setIsLogin(true)}
            disabled={loading}
          >
            Iniciar Sesión
          </button>
          <button
            className={`px-6 py-2 font-bold rounded-lg transition ${
              !isLogin
                ? "bg-black text-white"
                : "bg-gray-200 text-gray-600 hover:bg-gray-300"
            }`}
            onClick={() => setIsLogin(false)}
            disabled={loading}
          >
            Registrarse
          </button>
        </div>

        {/* Mensaje de error */}
        {error && (
          <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded-lg text-sm">
            {error}
          </div>
        )}

        {isLogin ? (
          // Formulario de Login
          <form onSubmit={handleLoginSubmit} className="space-y-4">
            <h2 className="text-xl font-bold text-center mb-4">
              Inicia sesión
            </h2>

            <div>
              <label
                htmlFor="login-username"
                className="block text-sm font-medium mb-2"
              >
                Username
              </label>
              <input
                id="login-username"
                type="text"
                value={loginData.username}
                onChange={(e) =>
                  setLoginData({ ...loginData, username: e.target.value })
                }
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-black"
                placeholder="usuario123"
                required
                disabled={loading}
              />
            </div>

            <div>
              <label
                htmlFor="login-password"
                className="block text-sm font-medium mb-2"
              >
                Contraseña
              </label>
              <input
                id="login-password"
                type="password"
                value={loginData.password}
                onChange={(e) =>
                  setLoginData({ ...loginData, password: e.target.value })
                }
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-black"
                placeholder="••••••••"
                required
                disabled={loading}
              />
            </div>

            <button
              type="submit"
              className="w-full bg-black text-white font-bold py-3 rounded-lg hover:bg-gray-800 transition disabled:bg-gray-400 disabled:cursor-not-allowed"
              disabled={loading}
            >
              {loading ? "Procesando..." : "Iniciar Sesión"}
            </button>
          </form>
        ) : (
          // Formulario de Registro
          <form onSubmit={handleRegisterSubmit} className="space-y-4">
            <h2 className="text-xl font-bold text-center mb-4">
              Crea tu cuenta
            </h2>

            <div>
              <label
                htmlFor="register-username"
                className="block text-sm font-medium mb-2"
              >
                Nombre de usuario
              </label>
              <input
                id="register-username"
                type="text"
                value={registerData.username}
                onChange={(e) =>
                  setRegisterData({ ...registerData, username: e.target.value })
                }
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-black"
                placeholder="usuario123"
                required
                disabled={loading}
              />
            </div>

            <div>
              <label
                htmlFor="register-email"
                className="block text-sm font-medium mb-2"
              >
                Email
              </label>
              <input
                id="register-email"
                type="email"
                value={registerData.email}
                onChange={(e) =>
                  setRegisterData({ ...registerData, email: e.target.value })
                }
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-black"
                placeholder="tu@email.com"
                required
                disabled={loading}
              />
            </div>

            <div>
              <label
                htmlFor="register-password"
                className="block text-sm font-medium mb-2"
              >
                Contraseña
              </label>
              <input
                id="register-password"
                type="password"
                value={registerData.password}
                onChange={(e) =>
                  setRegisterData({ ...registerData, password: e.target.value })
                }
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-black"
                placeholder="••••••••"
                required
                disabled={loading}
              />
            </div>

            <button
              type="submit"
              className="w-full bg-black text-white font-bold py-3 rounded-lg hover:bg-gray-800 transition disabled:bg-gray-400 disabled:cursor-not-allowed"
              disabled={loading}
            >
              {loading ? "Procesando..." : "Registrarse"}
            </button>
          </form>
        )}
      </div>
    </div>
  );
}