import { ReactNode } from "react";
import { Link, useLocation } from "react-router-dom";

interface NavLinkProps {
  to: string;
  children: ReactNode;
  active?:boolean
}
function NavLink({children, to, active=false} : NavLinkProps) { 
    return <Link to={to} 
    className={`transition flex items-center gap-2 font-bold px-2 py-2 hover:text-black ${active?'lg:border-b-4 text-black':'border-b-0 text-gray-350'} border-b-main-200`}>
        {children}
    </Link>
}

export default function Navbar() {
    const location = useLocation();

    return <nav className="w-auto flex justify-center font-inter space-x-4 pt-4 lg:py-0 px-6 rounded-3xl mt-2 md:mt-0 bg-white
    flex-col lg:flex-row text-[14px]">
        <NavLink to="/" active={location.pathname === '/'}>
            <span className="inline-block lg:hidden">
                <i className="fa-solid fa-house"></i>                
            </span>
            Inicio
        </NavLink>
        <NavLink to="/testimonios" active={location.pathname === '/testimonios'}>
            <span className="inline-block lg:hidden">
                <i className="fa-solid fa-comment"></i>
            </span>
            Testimonios
        </NavLink>
        <NavLink to="/nosotros" active={location.pathname === '/nosotros'}>
            <span className="inline-block lg:hidden">
                <i className="fa-solid fa-users"></i>                             
            </span>
            Nosotros
        </NavLink>
    </nav>
}