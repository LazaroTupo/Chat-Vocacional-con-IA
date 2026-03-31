interface CardProps {
  imageUrl: string;
  name: string;
  shortDescription: string;
}

export default function Card({ imageUrl, name, shortDescription }: CardProps) {
  return (
    <div className="bg-white rounded-2xl shadow-lg overflow-hidden hover:shadow-xl transition-shadow">
      <img 
        src={imageUrl} 
        alt={name}
        className="w-full h-48 object-cover"
        onError={(e) => {
          // Imagen de respaldo si falla la carga
          e.currentTarget.src = "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=500";
        }}
      />
      <div className="p-6">
        <h3 className="text-xl font-bold mb-3">{name}</h3>
        <p className="text-gray-600 text-sm leading-relaxed">{shortDescription}</p>
      </div>
    </div>
  );
}