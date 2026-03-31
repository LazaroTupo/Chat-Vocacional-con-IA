import { ReactNode, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { isUserLoggedIn, clearCredentials  } from "../../../components/ModalLogin.tsx";

interface ButtonProps {
    children: ReactNode,
    onClick?: () => void
}
function Button({children, onClick = ()=>{}} : ButtonProps) {
    return <button onClick={onClick}
    className="text-center rounded-3xl bg-black text-white text-[14px] px-4 py-2 font-bold cursor-pointer hover:bg-gray-800">
        {children}
    </button>
}

interface SocialMediaLinksProps {
  setShowModal: (x:boolean) => void
}
export default function SocialMediaLinks({setShowModal}:SocialMediaLinksProps) {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    // Verificar estado de login al cargar
    useEffect(() => {
        setIsLoggedIn(isUserLoggedIn());
        
        // Escuchar eventos de cambio de autenticación
        const handleAuthChange = () => {
            setIsLoggedIn(isUserLoggedIn());
        };

        window.addEventListener('authChange', handleAuthChange);
        window.addEventListener('storage', handleAuthChange); // Para cambios entre pestañas

        return () => {
            window.removeEventListener('authChange', handleAuthChange);
            window.removeEventListener('storage', handleAuthChange);
        };
    }, []);

    const handleLogout = () => {
        clearCredentials();
        setIsLoggedIn(false);
        window.dispatchEvent(new CustomEvent('authChange', { detail: { isLoggedIn: false } }));
    };

    return (
        <div className="flex space-x-4 py-2 md:mt-0 items-start lg:items-center px-6 flex-col lg:flex-row">
            {isLoggedIn ? (
                <Button onClick={handleLogout}>
                    Cerrar Sesión
                </Button>
            ) : (
                <Button onClick={() => setShowModal(true)}>
                    Login / Sign Up
                </Button>
            )}
            <div className="flex gap-2 items-center flex-wrap pt-4 lg:pt-0">
                <p className="block lg:hidden w-full">Siguenos:</p>
                <Link to="#" className="text-[20px] bg-white px-2 py-1 rounded-lg hover:bg-gray-50 transition-all">
                    <i className="fa-brands fa-linkedin-in"></i>
                </Link>
                <Link to="#" className="text-[20px] bg-white px-2 py-1 rounded-lg hover:bg-gray-50 transition-all">
                    <i className="fa-brands fa-github"></i>
                </Link>
                <Link to="#" className="text-[20px] bg-white px-2 py-1 rounded-lg hover:bg-gray-50 transition-all">
                    <i className="fa-brands fa-x-twitter"></i>
                </Link>
            </div>
        </div>
    );
}