import { createBrowserRouter } from "react-router-dom";

import Layout from "./pages/layout/Layout";
import HomePage from "./pages/home/HomePage";
import TestimoniosPage from "./pages/testimonios/TestimoniosPage";
import NosotrosPage from "./pages/nosotros/NosotrosPage";
import LayoutLab from "./pages/layout-lab/LayoutLab";
import ChatPage from "./pages/chat/ChatPage";
import CarrerasPage from "./pages/carreras/CarrerasPage";
import RecursosPage from "./pages/recursos/RecursosPage";
import UniversidadPage from "./pages/universidad/UniversidadPage";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      {
        index: true,
        element: <HomePage />,
      },
      {
        path: "/testimonios",
        element: <TestimoniosPage />,
      },
      {
        path: "/nosotros",
        element: <NosotrosPage />,
      },
    ],
  },
  {
    path: "/lab",
    element: <LayoutLab />,
    children: [
      {
        index: true,
        element: <ChatPage />,
      },
      {
        path: "/lab/carreras",
        element: <CarrerasPage />,
      },
      {
        path: "/lab/universidad",
        element: <UniversidadPage />,
      },
      {
        path: "/lab/recursos",
        element: <RecursosPage />,
      },
    ],
  },
]);

export default router;
