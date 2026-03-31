interface CardProps {
  imageUrl: string;
  name: string;
  shortDescription: string;
}
export default function Card({ imageUrl, name, shortDescription }: CardProps) {
  return (
    <div className="bg-white rounded-3xl p-5 hover:-rotate-z-12 transition-all hover:shadow-[0_0_10px_5px_#A086FF] hover:shadow-[#A086FF] cursor-pointer">
      <img
        src={imageUrl}
        alt={name}
        className="block rounded-3xl my-2 mx-auto h-[180px] w-full object-cover"
      />
      <h5 className="text-[18px] font-semibold mb-5">{name}</h5>
      <p className="text-[13px]">{shortDescription}</p>
    </div>
  );
}
