import Slider from "./components/Slider";
import bgSvg from "../../assets/bg-svg/bg-testimonials.svg";

export default function TestimoniosPage() {
  return (
    <div className="relative w-full h-full">
      <img
        src={bgSvg}
        alt="bg"
        className="w-full h-auto absolute top-0 left-0 -z-10"
      />
      <div className="py-10 w-full  mx-auto flex flex-col h-full">
        <h1
          className="font-sf font-semibold leading-[0.8] text-center
            text-[30px] md:text-[40px] lg:text-[60px]"
        >
          <span className="">Nuestros</span>{" "}
          <span className="text-transparent bg-clip-text bg-gradient-to-r from-[#A629FF] to-[#1DD1CB]">
            usuarios
          </span>{" "}
          opinan
        </h1>
        <p className="text-center my-8">
          Nuestra mision es brindar una herramienta efectiva y una experiencia
          satisfactoria a nuestros usuarios
        </p>
        <Slider
          data={[
            {
              profilePhotoUrl: "user1.png",
              name: "María González",
              username: "mariag_dev",
              comment:
                "Esta plataforma me ayudó a descubrir mi verdadera vocación. Los recursos son increíbles y la orientación personalizada fue clave para elegir mi carrera en ingeniería de software.",
            },
            {
              profilePhotoUrl: "user2.png",
              name: "Carlos Méndez",
              username: "carlosm2024",
              comment:
                "Excelente herramienta para estudiantes. Pude explorar diferentes opciones de carrera y universidades. Los testimonios de otros estudiantes me dieron mucha confianza en mi decisión.",
            },
            {
              profilePhotoUrl: "user1.png",
              name: "Sofía Ramírez",
              username: "sofiaramirez",
              comment:
                "Me encanta la interfaz y lo fácil que es navegar. Encontré información valiosa sobre las carreras que me interesan y pude comparar diferentes universidades sin complicaciones.",
            },
            {
              profilePhotoUrl: "user2.png",
              name: "Andrés Torres",
              username: "andres_tech",
              comment:
                "La mejor plataforma de orientación vocacional que he usado. Los tests son precisos y los resultados me sorprendieron gratamente. Ahora estoy estudiando lo que realmente me apasiona.",
            },
            {
              profilePhotoUrl: "user1.png",
              name: "Valentina Flores",
              username: "valeflores_",
              comment:
                "Increíble experiencia. Pude resolver todas mis dudas sobre las carreras universitarias. El chat con mentores fue súper útil y me dio una perspectiva real del campo laboral.",
            },
            {
              profilePhotoUrl: "user2.png",
              name: "Diego Paredes",
              username: "diegoparedes",
              comment:
                "Recomiendo esta plataforma al 100%. Me ayudó a tomar la mejor decisión para mi futuro. Los recursos educativos son de alta calidad y muy actualizados.",
            },
            {
              profilePhotoUrl: "user1.png",
              name: "Isabella Cruz",
              username: "isabellacruz",
              comment:
                "Una herramienta esencial para cualquier estudiante. La información sobre becas y programas de intercambio fue invaluable. Logré conseguir una beca gracias a esta plataforma.",
            },
            {
              profilePhotoUrl: "user2.png",
              name: "Javier Salazar",
              username: "javisalazar",
              comment:
                "Fantástico servicio de orientación. Me ayudó a descubrir carreras que ni siquiera había considerado. Ahora estoy emocionado por mi futuro en ciencia de datos.",
            },
            {
              profilePhotoUrl: "user1.png",
              name: "Camila Vega",
              username: "cami_vega",
              comment:
                "La plataforma superó mis expectativas. Los videos explicativos sobre cada carrera son excelentes y me dieron una visión clara de lo que podría ser mi vida profesional.",
            },
            {
              profilePhotoUrl: "user2.png",
              name: "Roberto Castillo",
              username: "robercast",
              comment:
                "Muy completa y fácil de usar. Pude conectar con estudiantes de diferentes universidades y conocer sus experiencias de primera mano. Esto fue fundamental para mi decisión.",
            },
            {
              profilePhotoUrl: "user1.png",
              name: "Ana Morales",
              username: "anamorales_",
              comment:
                "Excelente plataforma para explorar opciones académicas. Los filtros de búsqueda son muy útiles y pude encontrar exactamente lo que buscaba. ¡Totalmente recomendable!",
            },
            {
              profilePhotoUrl: "user2.png",
              name: "Fernando Díaz",
              username: "ferdiaz_dev",
              comment:
                "Una herramienta transformadora. Me ayudó a aclarar mis objetivos profesionales y encontrar la universidad perfecta. El equipo de soporte es muy atento y resolutivo.",
            },
          ]}
        />
      </div>
    </div>
  );
}
