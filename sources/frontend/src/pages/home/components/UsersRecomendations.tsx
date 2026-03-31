
interface UsersRecomendationsProps {
    usuarios: number;
    recomiendan: number;
    avatars: string[]; // URLs de las imágenes
}

export default function UsersRecomendations({ usuarios, recomiendan, avatars } :UsersRecomendationsProps ) {
  return (
    <div className="flex items-center py-2 px-0 md:px-4 font-inter text-main-800 rounded-full w-fit min-w-fit bg-gradient-to-r from-main-100 to-white overflow-hidden text-sm divide-x divide-main-800">
      <div className="px-4 py-2"style={{fontSize: "12px"}}>
        <span className="font-black" style={{fontSize: "20px"}}>+{usuarios}</span> <br /> usuarios
      </div>
      <div className="px-4 py-2"style={{fontSize: "12px"}}>
        <span className="font-black" style={{fontSize: "20px"}}>+{recomiendan}</span> <br /> recomiendan
      </div>
      <div className="px-4 py-2 flex items-center space-x-[-15px] pl-4">
        {avatars.map((avatar, index) => (
          <img
            width={45}
            key={index}
            src={avatar}
            alt={`Usuario ${index + 1}`}
            className="rounded-full border-2 border-white shadow-sm"
          />
        ))}
      </div>
    </div>
  );
};