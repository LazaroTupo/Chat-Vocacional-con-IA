import { Link } from "react-router-dom";

export default function Logo() {
    return <div className="flex items-center flex-shrink-0 text-xl font-bold text-gray-800">
        <Link to={'/'} className="flex items-center text-[18px] font-inter">
            <img src="/logo_header.png" alt="Logo" className="mr-2" width={24} />
            <span className="hidden md:inline">VocationLab</span>
        </Link>
    </div>
}