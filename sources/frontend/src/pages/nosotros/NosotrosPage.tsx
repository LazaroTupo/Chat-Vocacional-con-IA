import Card from "./components/Card";
import bgSvg from "../../assets/bg-svg/bg-about.svg";

export default function NosotrosPage() {
  const data = [
    {
      profilePhotoUrl: "/yo_mejorado.png",
      name: "Calongos Jara, Leonid",
      position: "Frontend Developer",
    },
    {
      profilePhotoUrl: "/black.png",
      name: "Lorenzo Ramos Daniel",
      position: "Backend Developer",
    },
    {
      profilePhotoUrl: "/bruno.png",
      name: "Jordan Rodas Panaifo",
      position: "Frontend Developer",
    },
    {
      profilePhotoUrl: "/laura.png",
      name: "Mendoza Morales, Laura",
      position: "UX/UI Designer",
    },
    {
      profilePhotoUrl: "/ever.png",
      name: "Chochoca Yarleque, Bruno",
      position: "Project Manager",
    },
  ];
  return (
    <div className="relative w-full px-8 md:px-8">
      <img
        src={bgSvg}
        alt="bg"
        className="w-full h-auto absolute top-0 left-0 -z-10"
      />
      <div className="text-center py-5 w-full md:w-10/12 mx-auto flex flex-col h-full">
        <h1
          className="font-sf font-semibold leading-[0.8] text-center
            text-[30px] md:text-[40px] lg:text-[60px]"
        >
          Nuestro
          <span className="text-transparent bg-clip-text bg-gradient-to-r from-[#A629FF] to-[#1DD1CB]">
            &nbsp;equipo
          </span>
        </h1>
        <p className="text-[#BDCFE2] text-[20px] md:text-[30px] lg:text-[40px] font-sf font-ligth mt-1 mb-4">
          Apasionados. Creativos. Trabajadores.
        </p>
        <div className="grid grid-cols-2 md:grid-cols-3 gap-10 gap-y-6 justify-items-center w-fit m-auto my-6">
          {data.map((usuario, index) => {
            return <Card {...usuario} key={index} />;
          })}
        </div>
        <div className="font-inter font-ligth text-[18px] text-[#92A1B0] max-w-[775px] m-auto my-10">
          <p className="py-5">
            VocationLab nació al reflexionar sobre el proceso de preparación
            preuniversitaria que siguió cada integrante de nuestro equipo. Nos
            dimos cuenta que al escoger una carrera universitaria, no se cuenta
            con una herramienta que cuente con la efectividad del servicio de un
            profesional en guía vocacional, en un entorno digital y de forma
            gratuita.{" "}
          </p>
          <p>
            Este proyecto es de código abierto, por lo que contamos con las
            sugerencias provenientes de la comunidad académica para brindar una
            mayor efectividad en nuestro test.
          </p>
        </div>
      </div>
    </div>
  );
}
