import { useState } from "react";
import { Outlet } from "react-router-dom";
import Header from "./components/Header.tsx"
import ModalLogin from "../../components/ModalLogin.tsx";

export default function Layout() {

    const [showModal, setShowModal] = useState(false);

    return (
        <div className="flex flex-col h-full">
            <Header setShowModal={setShowModal}/>
            <main className="grow">
                <Outlet context={{ setShowModal }} />
            </main>
            {showModal && <ModalLogin setShowModal={setShowModal}/>}
        </div>
    )
}