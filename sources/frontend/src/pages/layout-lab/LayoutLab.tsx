import { useState } from "react";
import { Outlet, Link } from "react-router-dom";
import bgSvg from "../../assets/bg-svg/bg-chat.svg";
import iconRobot from "../../assets/svg-lab/icon-robot.svg";
import iconCareers from "../../assets/svg-lab/icon-careers.svg";
import iconSchools from "../../assets/svg-lab/icon-schools.svg";
import iconResources from "../../assets/svg-lab/icon-resources.svg";

export default function LayoutLab() {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <div className="flex h-full relative">
      <img
        src={bgSvg}
        alt="bg"
        className="absolute top-0 left-0 w-full h-full object-cover -z-10"
      />

      {/* Botón hamburguesa para móviles */}
      <button
        className="md:hidden fixed top-4 left-4 z-50 p-2 bg-white/90 backdrop-blur-sm rounded-lg shadow-lg"
        onClick={() => setSidebarOpen(!sidebarOpen)}
      >
        <i
          className={`fas ${
            sidebarOpen ? "fa-times" : "fa-bars"
          } text-gray-800`}
        ></i>
      </button>

      {/* Overlay oscuro al abrir menú en mobile */}
      {sidebarOpen && (
        <div
          className="md:hidden fixed inset-0 bg-black/50 z-30"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {/* Sidebar */}
      <aside
        className={`
          fixed md:relative top-0 left-0 h-full z-40 w-64 md:w-2/12
          bg-white bg-opacity-80 backdrop-blur-md transition-transform duration-300 ease-in-out
          ${
            sidebarOpen ? "translate-x-0" : "-translate-x-full"
          } md:translate-x-0
        `}
      >
        {/* Fondo decorativo */}
        <div className="absolute inset-0 bg-gradient-to-b from-[#D2D1FF] to-[#D2D1FF] opacity-50 z-0" />

        {/* Contenido del sidebar */}
        <div className="relative z-10 py-8 px-6 flex flex-col h-full overflow-hidden">
          {/* Logo */}
          <div className="w-full flex items-center flex-shrink-0 text-xl font-bold text-gray-800 overflow-hidden">
            <Link to="/" className="flex items-center text-[18px]">
              <img
                src="/logo_header.png"
                alt="Logo"
                className="mr-2"
                width={24}
              />
              <span>VocationLab</span>
            </Link>
          </div>

          {/* Navegación */}
          <nav className="flex flex-col grow py-10 gap-5">
            <Link
              to="/lab"
              className="flex items-center gap-4 font-inter text-[18px] font-semibold text-transparent bg-clip-text bg-gradient-to-t from-[#A629FF] to-[#1DD1CB]"
              onClick={() => setSidebarOpen(false)}
            >
              <img
                src={iconRobot}
                alt="Test"
                className="w-5 h-5 md:w-6 md:h-6 shrink-0"
              />
              <span>Test</span>
            </Link>
            <Link
              to="/lab/carreras"
              className="flex items-center gap-4 font-inter text-[18px] font-semibold text-transparent bg-clip-text bg-gradient-to-t from-[#A629FF] to-[#1DD1CB]"
              onClick={() => setSidebarOpen(false)}
            >
              <img
                src={iconCareers}
                alt="Carreras"
                className="w-5 h-5 md:w-6 md:h-6 shrink-0"
              />
              <span>Carreras</span>
            </Link>
            <Link
              to="/lab/universidad"
              className="flex items-center gap-4 font-inter text-[18px] font-semibold text-transparent bg-clip-text bg-gradient-to-t from-[#A629FF] to-[#1DD1CB]"
              onClick={() => setSidebarOpen(false)}
            >
              <img
                src={iconSchools}
                alt="Universidades"
                className="w-5 h-5 md:w-6 md:h-6 shrink-0"
              />
              <span>Universidades</span>
            </Link>
            <Link
              to="/lab/recursos"
              className="flex items-center gap-4 font-inter text-[18px] font-semibold text-transparent bg-clip-text bg-gradient-to-t from-[#A629FF] to-[#1DD1CB]"
              onClick={() => setSidebarOpen(false)}
            >
              <img
                src={iconResources}
                alt="Recursos"
                className="w-5 h-5 md:w-6 md:h-6 shrink-0"
              />
              <span>Recursos</span>
            </Link>
          </nav>

          {/* Salir */}
          <div>
            <Link
              to="/"
              className="flex items-center gap-4 font-inter text-[18px] font-semibold text-transparent bg-clip-text bg-gradient-to-t from-[#A629FF] to-[#1DD1CB]"
              onClick={() => setSidebarOpen(false)}
            >
              <i className="fa-solid fa-right-from-bracket w-5 md:w-6" />
              <span>Salir del Lab</span>
            </Link>
          </div>
        </div>
      </aside>

      {/* Main */}
      <main className="flex-1 md:ml-0 ml-0 px-4 md:px-8">
        <Outlet />
      </main>
    </div>
  );
}
