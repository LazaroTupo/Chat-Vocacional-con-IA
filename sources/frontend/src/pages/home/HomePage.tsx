import UsersRecomendations from "./components/UsersRecomendations";
import { useOutletContext } from "react-router-dom";
import { useEffect } from "react";

import mainSvgDesktop from "../../assets/svg-home/main-home.svg";
import mainSvgMobile from "../../assets/svg-home/main-mobile-home.svg";
import { isUserLoggedIn } from "../../components/ModalLogin"; // Ajusta la ruta según tu estructura

type ContextType = {
    setShowModal: (show: boolean) => void;
};


export default function HomePage() {
    const { setShowModal } = useOutletContext<ContextType>();

    const handleTestClick = (e: React.MouseEvent) => {
        // Prevenir el comportamiento normal del Link
        e.preventDefault();
        
        // Verificar si el usuario está logueado
        if (isUserLoggedIn()) {
            // Si está logueado, redirigir a /lab
            window.location.href = '/lab';
        } else {
            // Si no está logueado, mostrar el modal
            setShowModal(true);
        }
    };

    useEffect(() => {
        // Esto asegura que el estado se sincronice al cargar la página
        window.dispatchEvent(new Event('checkAuthStatus'));
    }, []);

    return (
        <div className="home-page h-full relative">
            {/* Imagen que cambia con el tamaño */}
            <picture className="pointer-events-none select-none">
                <source media="(min-width: 768px)" srcSet={mainSvgDesktop} />
                <img
                    src={mainSvgMobile}
                    alt="Decoración"
                    className="
                        absolute w-[1800px] 
                        bottom-[-50px] left-1/2 transform -translate-x-1/2
                        md:top-1/2 md:right-[-20px] md:left-auto md:translate-x-0 md:-translate-y-1/2 md:w-auto md:h-full
                    "
                />
            </picture>

            {/* Contenido */}
            <div className="py-10 md:py-20 w-full md:w-10/12 mx-auto flex flex-col h-full relative z-10">
                <h1 className="font-sf font-semibold leading-[0.8] w-fit md:m-0 m-auto text-center md:text-start
                    text-[40px] md:text-[60px] lg:text-[80px]">
                    <span>Transformamos la</span><br />
                    <span className="text-transparent bg-clip-text bg-gradient-to-r from-[#A629FF] to-[#1DD1CB]">
                        orientación vocacional
                    </span>
                </h1>

                <p className="font-inter my-4 lg:my-10 text-[14px] lg:text-[24px] text-[#5D5D5D] text-center md:text-start">
                    Prueba nuestro test interactivo, diseñado para <br /> conocerte de verdad sin tener que aburrirte.
                </p>

                <div className="flex space-x-2 justify-center md:justify-start">
                    <button
                        onClick={handleTestClick}
                        className="px-8 py-3 text-[14px] lg:text-[20px] font-bold cursor-pointer flex items-center gap-2 font-inter rounded-full text-white bg-gradient-to-r opacity-[.72] hover:opacity-100 from-main-400 to-main-300 transition"
                    >
                        <i className="fa-solid fa-rocket" style={{ fontSize: '18px' }}></i>
                        Iniciar test
                    </button>
                </div>

                <div className="grow flex justify-center md:justify-start items-end">
                    <UsersRecomendations
                        usuarios={400}
                        recomiendan={290}
                        avatars={['user1.png', 'user2.png', 'user3.png']}
                    />
                </div>
            </div>
        </div>
    );
}