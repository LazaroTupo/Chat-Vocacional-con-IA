import React from "react";
import ResourceCard from "./components/ResourceCard";
import suneduLogo from "../../assets/svg-resources/resource-sunedu.svg";
import linkedinLogo from "../../assets/svg-resources/resource-linkedin.svg";
import cambridgeLogo from "../../assets/svg-resources/resource-cambridge.svg";

const resources = [
    {
        imageSrc: suneduLogo,
        title: "Plataforma de la SUNEDU",
        description: "Herramienta útil para el desarrollo académico y validación de títulos universitarios en Perú.",
    },
    {
        imageSrc: linkedinLogo,
        title: "LinkedIn",
        description: "Red profesional donde puedes construir tu perfil académico y conectar con oportunidades laborales.",
    },
    {
        imageSrc: cambridgeLogo,
        title: "Cambridge English",
        description: "Certificaciones en inglés reconocidas internacionalmente, útiles para estudios o empleo.",
    },
];

const Resources: React.FC = () => {
    return (
        <div className="h-full overflow-scroll no-scrollbar">
            <div className="py-10 text-center">
            <h2 className="font-sf font-semibold leading-[0.8] md:m-0 m-auto
            text-[40px] md:text-[40px] lg:text-[60px]">
                <span className="text-transparent bg-clip-text bg-gradient-to-r from-[#A629FF] to-[#1DD1CB]">
                    Recursos
                </span>
            </h2>
            <p className="py-10">
            Información adicional que te enseñará más sobre tu carrera profesional según tu perfil vocacional
            </p>
        </div>
            <div className="flex flex-col gap-6 items-center">
                {resources.map((resource, index) => (
                    <ResourceCard
                        key={index}
                        imageSrc={resource.imageSrc}
                        title={resource.title}
                        description={resource.description}
                    />
                ))}
            </div>
        </div>
    );
};

export default Resources;