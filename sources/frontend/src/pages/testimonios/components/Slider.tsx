import { useEffect, useState } from "react"
import { Swiper, SwiperSlide } from 'swiper/react';
import { Autoplay, FreeMode } from "swiper/modules";
import blurSvg from "../../../assets/blur-swipper.svg"

import 'swiper/swiper-bundle.css';

interface CardProps {
  profilePhotoUrl: string,
  name: string,
  username: string,
  comment: string,
}
function Card({ profilePhotoUrl, name, username, comment }: CardProps) {
  return (
    <div className="rounded-3xl bg-white font-inter text-black w-full z-10 p-4 hover:-rotate-z-12 transition-all hover:shadow-[0_0_10px_5px_#A086FF] hover:shadow-[#A086FF] cursor-pointer">
      <img src={profilePhotoUrl} alt="Usuario" className="block m-auto mb-2" width={50} />
      <div className="text-center">
        <p className="font-bold text-[18px]">{name}</p>
        <p className="text-[10px]">{username}</p>
        <p className="text-[13px] my-4">{comment}</p>
        <p className="text-[20px] text-[#BDCFE2] shadow rounded-full w-[30px] m-auto">
          <i className="fa-solid fa-quote-left"></i>
        </p>
      </div>
    </div>
  );
}

interface SliderProps {
  data: CardProps[]
}
export default function Slider({ data }: SliderProps) {
  const [firstData, setFirstData] = useState<CardProps[]>([]);
  const [secondData, setSecondData] = useState<CardProps[]>([]);

  useEffect(() => {
    const half = Math.ceil(data.length / 2);
    setFirstData(data.slice(0, half));
    setSecondData(data.slice(half));
  }, [data]);

  const swiperSettings = {
    slidesPerView: "auto" as const,
    spaceBetween: 20,
    loop: true,
    // freeMode: true,
    speed: 6000,
    autoplay: {
      delay: 1000,
      disableOnInteraction: false,
      pauseOnMouseEnter: false,
    },
    allowTouchMove: false,
    simulateTouch: false,
    modules: [Autoplay, FreeMode],
    className: "mySwiper",
  };

  return (
    <div className="overflow-hidden">
      <img
        src={blurSvg}
        alt="bg"
        className="absolute z-50 pointer-events-none w-full h-auto absolute top-0 left-0 -z-10"
      />
      {/* Primera fila */}
      <Swiper {...swiperSettings}>
        {firstData.map((user, index) => (
          <SwiperSlide key={index} className="!w-[326px] py-8">
            <Card {...user} />
          </SwiperSlide>
        ))}
      </Swiper>

      {/* Segunda fila (intercalada visualmente) */}
      <Swiper {...swiperSettings} className="mySwiper">
        {secondData.map((user, index) => (
          <SwiperSlide key={index} className="!w-[326px] translate-x-[100px] py-8">
            <Card {...user} />
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
}