import { Link } from "react-router-dom";

interface CardProbs {
    profilePhotoUrl: string;
    name: string;
    position: string;
}

export default function Card({profilePhotoUrl, name, position}: CardProbs){
    return <div className="w-[200px] h-[210px] py-4 px-1 rounded-4xl bg-white">
        <div className="pb-5">
            <img className="block m-auto mb-2 rounded-full" src={profilePhotoUrl} alt="" />
            <p className="font-inter text-[18px] font-semibold">{name}</p>
            <p className="font-inter text-[16px] font-medium text-[#7B8C9F]">{position}</p>
        </div>
        <div className="flex gap-7 items-center justify-center">
            <Link to="#" style={{fontSize: '20px'}}>
                <i className="fa-brands fa-github"></i>
            </Link>
            <Link to="#" style={{fontSize: '20px'}}>
                <i className="fa-brands fa-linkedin-in"></i>
            </Link>
            <Link to="#" style={{fontSize: '20px'}}>
                <i className="fa-brands fa-x-twitter"></i>
            </Link>
        </div>
    </div>
}