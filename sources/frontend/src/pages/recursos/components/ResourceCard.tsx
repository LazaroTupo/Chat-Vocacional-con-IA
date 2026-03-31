import React from "react";

interface ResourceCardProps {
  imageSrc: string;
  title: string;
  description: string;
}

const ResourceCard: React.FC<ResourceCardProps> = ({ imageSrc, title, description }) => {
  return (
    <div className="bg-white/50 backdrop-blur-md p-6 rounded-xl flex items-center gap-8 w-full max-w-3xl mx-auto">
      <img src={imageSrc} alt={title} className="w-36 h-auto object-contain" />
      <div className="flex flex-col gap-1">
        <h3 className="text-lg font-semibold">{title}</h3>
        <p className="text-sm text-gray-700">{description}</p>
      </div>
    </div>
  );
};

export default ResourceCard;