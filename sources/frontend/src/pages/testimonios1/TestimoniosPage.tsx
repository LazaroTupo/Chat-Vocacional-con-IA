import Title from "../../components/Title"
import Slider from "./components/Slider"
import CommentBox from "./components/CommentBox"

export default function TestimoniosPage() {
    return <div>
        <div className="w-fit m-auto">
            <Title>
                <p className="text-center mb-3">
                    <span>Testimonios de</span>
                    <span className="text-transparent bg-clip-text bg-gradient-to-r from-main-500 to-secondary-600">
                        nuestros usuarios
                    </span>
                </p>
                <div className="w-full h-1 bg-gradient-to-r from-main-700 via-secondary-400 to-secondary-600"></div>
            </Title>
        </div>
        <div className="my-10">
            <Slider data={[
                {
                    profilePhotoUrl: 'user1.png',
                    comment: 'Lorem ipsum dolor sit amet consectetur adipiscing elit tempus, pharetra netus cras augue placerat cum duis commodo, magna mollis molestie ultricies odio aenean rhoncus. Gravida mauris malesuada litora velit tincidunt montes blandit platea ornare.',
                    username: 'Juanito del Campo'
                },
                {
                    profilePhotoUrl: 'user2.png',
                    comment: 'Lorem ipsum dolor sit amet consectetur adipiscing elit tempus, pharetra netus cras augue placerat cum duis commodo, magna mollis molestie ultricies odio aenean rhoncus. Gravida mauris malesuada litora velit tincidunt montes blandit platea ornare.',
                    username: 'Juanito del Campo'
                },
                {
                    profilePhotoUrl: 'user3.png',
                    comment: 'Lorem ipsum dolor sit amet consectetur adipiscing elit tempus, pharetra netus cras augue placerat cum duis commodo, magna mollis molestie ultricies odio aenean rhoncus. Gravida mauris malesuada litora velit tincidunt montes blandit platea ornare.',
                    username: 'Juanito del Campo'
                },
            ]}/>
        </div>
        <div className="my-10">
            <h3 className="font-inter mb-4 text-[25px] text-[#4c4c4c]">Deja tu testimonio</h3>
            <CommentBox/>
        </div>
    </div>
}