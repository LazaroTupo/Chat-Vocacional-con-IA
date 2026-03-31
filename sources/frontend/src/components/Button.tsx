import { ReactNode } from "react";

interface ButtonProps {
    children: ReactNode,
    className: string,
    onClick?: () => void
}
export default function Button({children, className="", onClick = ()=>{}} : ButtonProps) {
    return <button onClick={onClick}
    className={`text-center rounded-3xl bg-black text-white px-4 py-2 ${className}`}>
        {children}
    </button>
}