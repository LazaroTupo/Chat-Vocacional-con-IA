import { useState, useEffect } from "react";
import Card from "./components/Card";
import { getCareerAndUniversityRecommendations } from "../../services/api";

interface University {
  name: string;
  careerOffered: string;
  imageUrl: string;
}

export default function UniversidadPage() {
  const [universities, setUniversities] = useState<University[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [hasLoaded, setHasLoaded] = useState(false);
  const [error, setError] = useState("");

  // Cargar recomendaciones
  const loadRecommendations = async () => {
    setIsLoading(true);
    setError("");
    
    try {
      const response = await getCareerAndUniversityRecommendations();
      
      // Asumiendo que el backend retorna { careers: [], universities: [] }
      if (response.universities && response.universities.length > 0) {
        setUniversities(response.universities);
        setHasLoaded(true);
        
        // Guardar también en localStorage para la página de carreras
        localStorage.setItem('universityRecommendations', JSON.stringify(response.universities));
        localStorage.setItem('careerRecommendations', JSON.stringify(response.careers || []));
      } else {
        setError("No se encontraron universidades recomendadas para tu perfil.");
      }
    } catch (err) {
      console.error("Error cargando recomendaciones:", err);
      setError("Error al cargar las recomendaciones. Por favor, intenta nuevamente.");
    } finally {
      setIsLoading(false);
    }
  };

  // Cargar datos desde localStorage si existen
  useEffect(() => {
    const savedUniversities = localStorage.getItem('universityRecommendations');
    if (savedUniversities) {
      setUniversities(JSON.parse(savedUniversities));
      setHasLoaded(true);
    }
  }, []);

  return (
    <div className="h-full overflow-scroll no-scrollbar">
      <div className="py-10 text-center">
        <h2 className="font-sf font-semibold leading-[0.8] md:m-0 m-auto text-[40px] md:text-[40px] lg:text-[60px]">
          <span className="">Universidades</span>&nbsp;
          <span className="text-transparent bg-clip-text bg-gradient-to-r from-[#A629FF] to-[#1DD1CB]">
            profesionales
          </span>
        </h2>
        
        <p className="py-4">
          {hasLoaded 
            ? "Estas son las principales universidades que ofrecen carreras de tu vocación"
            : "Descubre las universidades que ofrecen las carreras ideales para ti"
          }
        </p>

        {/* Botón para cargar recomendaciones */}
        {!hasLoaded && (
          <div className="mb-6">
            <button
              onClick={loadRecommendations}
              disabled={isLoading}
              className="px-6 py-3 bg-gradient-to-r from-[#A629FF] to-[#1DD1CB] text-white rounded-lg font-bold hover:opacity-90 transition disabled:opacity-50 flex items-center gap-2 mx-auto"
            >
              {isLoading ? (
                <>
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                  Cargando...
                </>
              ) : (
                <>
                  <i className="fa-solid fa-wand-magic-sparkles"></i>
                  Cargar universidades según tu historial
                </>
              )}
            </button>
          </div>
        )}

        {/* Botón para actualizar recomendaciones */}
        {hasLoaded && (
          <div className="mb-6">
            <p className="text-sm text-gray-600 mb-3">
              ¿Quieres ver nuevas universidades según tu historial actualizado?
            </p>
            <button
              onClick={loadRecommendations}
              disabled={isLoading}
              className="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg font-bold hover:bg-gray-300 transition disabled:opacity-50 flex items-center gap-2 mx-auto text-sm"
            >
              {isLoading ? (
                <>
                  <div className="animate-spin rounded-full h-3 w-3 border-b-2 border-gray-700"></div>
                  Actualizando...
                </>
              ) : (
                <>
                  <i className="fa-solid fa-arrows-rotate"></i>
                  Actualizar universidades
                </>
              )}
            </button>
          </div>
        )}

        {/* Mensaje de error */}
        {error && (
          <div className="mb-6 p-4 bg-red-100 border border-red-400 text-red-700 rounded-lg max-w-md mx-auto">
            {error}
          </div>
        )}
      </div>

      {/* Lista de universidades */}
      {hasLoaded ? (
        universities.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5 px-12 text-center flex-wrap">
            {universities.map((university, index) => (
              <Card 
                key={index} 
                name={university.name}
                shortDescription={university.careerOffered}
                imageUrl={university.imageUrl}
              />
            ))}
          </div>
        ) : (
          <div className="text-center py-10">
            <p className="text-gray-500">No se encontraron universidades recomendadas.</p>
          </div>
        )
      ) : (
        <div className="text-center py-20">
          <div className="max-w-md mx-auto">
            <i className="fa-solid fa-building-columns text-6xl text-gray-300 mb-4"></i>
            <h3 className="text-xl font-bold mb-2">Universidades recomendadas</h3>
            <p className="text-gray-600 mb-6">
              Haz clic en "Cargar universidades según tu historial" para descubrir las universidades que ofrecen las carreras ideales para tu perfil vocacional.
            </p>
          </div>
        </div>
      )}
    </div>
  );
}