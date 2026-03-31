import Navbar from "./Navbar";
import { Link } from "react-router-dom";
import SocialMediaLinks from "./SocialMediaLinks";
import { useState } from "react";

function Logo() {
    return <div className="flex items-center flex-shrink-0 text-xl font-bold text-gray-800">
        <Link to={'/'} className="flex items-center text-[18px]">
            <img src="/logo_header.png" alt="Logo" className="mr-2" width={24} />
            <span className="hidden lg:inline">VocationLab</span>
        </Link>
    </div>
}

interface HeaderProps {
  setShowModal: (x:boolean) => void
}
export default function Header({setShowModal} : HeaderProps) {

  const [showMenu, setShowMenu] = useState(false);

   return (
    <header className="w-full px-4 py-8 font-inter">
      <div className="max-w-10/12 mx-auto flex justify-between items-center relative">
        <div className="block lg:hidden">
          <Logo/>
        </div>
        <div className={`flex-wrap items-center justify-between lg:w-full absolute bg-white 
          right-0 top-10 z-20
          ${showMenu?'block':'hidden'} lg:flex lg:static lg:bg-transparent rounded-4xl`}>
          <div className="hidden lg:block">
            <Logo/>
          </div>
          <Navbar/>
          <SocialMediaLinks setShowModal={setShowModal}/>
        </div>
        <button className="block lg:hidden bg-white cursor-pointer px-3 py-2 rounded-full"
        onClick={()=>setShowMenu(!showMenu)}>
          <i className="fa-solid fa-bars"></i>
        </button>
      </div>
    </header>
  );
};
