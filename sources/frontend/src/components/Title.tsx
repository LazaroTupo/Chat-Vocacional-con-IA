import { ReactNode } from "react";

interface TitleProps {
    children: ReactNode;
}

export default function Title({ children }: TitleProps) {
    return (
        <div className="text-center">
            {children}
        </div>
    );
}