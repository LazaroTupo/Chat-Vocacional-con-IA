import { Swiper, SwiperSlide } from 'swiper/react';
import { EffectCoverflow, Pagination } from 'swiper/modules';
import 'swiper/swiper-bundle.css';

interface CardProps {
    profilePhotoUrl: string,
    comment: string,
    username: string,
}
function Card({ profilePhotoUrl, comment, username }: CardProps) {
  return (
    <div className="rounded-3xl font-inter text-white py-4 bg-gradient-to-tr from-secondary-700 to-main-900 px-12">
      <img src={profilePhotoUrl} alt="Usuario" className="block" width={70} />
      <p className="my-4 text-[25px] line-clamp-3">{comment}</p>
      <div style={{fontSize: 24}}>
        <p className="font-bold">{username}</p>
        <p>Estudiante</p>
      </div>
    </div>
  );
}

interface CoverflowSwiperProps {
    data: CardProps[]
}
export default function CoverflowSwiper({data} : CoverflowSwiperProps) {
  return (
    <Swiper
      effect={'coverflow'}
      grabCursor={true}
      centeredSlides={true}
      slidesPerView={'auto'}
      spaceBetween={80}
      coverflowEffect={{
        rotate: 0,
        stretch: 0,
        depth: 0,
        modifier: 2,
        slideShadows: false,
      }}
      modules={[EffectCoverflow, Pagination]}
      className="mySwiper"
    >
      {data.map((element, idx) => (
        <SwiperSlide key={idx} style={{width: 752}}>
          <Card 
          profilePhotoUrl={element.profilePhotoUrl}
          comment={element.comment}
          username={element.username}/>
        </SwiperSlide>
      ))}
    </Swiper>
  );
}
