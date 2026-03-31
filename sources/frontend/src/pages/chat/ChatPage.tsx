import { useState, useEffect, useRef } from "react";
import CommentBox from "./components/CommentBox";
import UserMessage from "./components/UserMessage";
import { getCurrentSession, clearChatSession } from "../../services/api";

interface MessageData {
  id: string;
  content: string;
  type: string;
  timestamp: string;
  metadata: {
    model: string;
    timestamp: string;
  };
}

export interface ChatHistoryItem {
  role: "user" | "assistant";
  content: string;
  isLoading?: boolean;
}

export default function ChatPage() {
  const [chatHistory, setChatHistory] = useState<ChatHistoryItem[]>([]);
  const [isLoadingHistory, setIsLoadingHistory] = useState(true);
  const chatContainerRef = useRef<HTMLDivElement>(null);

  // Cargar historial al montar el componente
  useEffect(() => {
    loadChatHistory();
  }, []);

  // Scroll automático cuando cambia el historial
  useEffect(() => {
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
    }
  }, [chatHistory]);

  const loadChatHistory = async () => {
    try {
      setIsLoadingHistory(true);
      const session = await getCurrentSession();
      
      if (session.messages && session.messages.length > 0) {
        // Convertir los mensajes del backend al formato del frontend
        const formattedHistory: ChatHistoryItem[] = session.messages.map((msg: MessageData) => ({
          role: msg.type === 'USER' ? 'user' : 'assistant',
          content: msg.content
        }));
        setChatHistory(formattedHistory);
      }
    } catch (error) {
      console.error('Error cargando historial:', error);
      // Si hay error de autenticación, redirigir al login
      if (error instanceof Error && error.message === 'Usuario no autenticado') {
        console.log('Usuario no autenticado, redirigiendo...');
      }
    } finally {
      setIsLoadingHistory(false);
    }
  };

  const handleClearChat = async () => {
    try {
      await clearChatSession();
      setChatHistory([]);
      console.log('Chat limpiado exitosamente');
    } catch (error) {
      console.error('Error limpiando chat:', error);
    }
  };

  if (isLoadingHistory) {
    return (
      <div className="h-full flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto"></div>
          <p className="mt-4">Cargando conversación...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col">
      {/* Header con botón de limpiar chat */}
      {chatHistory.length > 0 && (
        <div className="flex justify-between items-center p-6">
          <h1 className="text-2xl font-bold">Test Vocacional</h1>
          <button
            onClick={handleClearChat}
            className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition flex items-center gap-2 text-sm"
          >
            <i className="fa-solid fa-trash"></i>
            Limpiar Chat
          </button>
        </div>
      )}

      {/* Contenedor principal del chat */}
      <div className="flex-1 flex flex-col min-h-0">
        
        {chatHistory.length === 0 ? (
          // Estado cuando no hay mensajes - Layout centrado
          <div className="flex-1 flex flex-col justify-center items-center p-6">
            <div className="text-center px-4 w-full max-w-4xl">
              <h2 className="text-4xl md:text-[60px] mb-3 font-bold">¿Empezamos con el test?</h2>
              <p className="mb-10 text-lg">Descubre tu verdadera vocación en una conversación con el coach vocacional</p>
              
              {/* CommentBox en estado centrado - mismo ancho que en estado con mensajes */}
              <div className="w-full max-w-4xl mx-auto">
                <CommentBox 
                  chatHistory={chatHistory} 
                  setChatHistory={setChatHistory} 
                />
              </div>
            </div>
          </div>
        ) : (
          // Estado cuando hay mensajes - Layout normal con scroll
          <>
            {/* Contenedor del historial con scroll */}
            <div 
              ref={chatContainerRef}
              className="flex-1 overflow-y-auto p-6 space-y-6 min-h-0"
            >
              {chatHistory.map((msg, index) => (
                <UserMessage 
                  key={index} 
                  user={msg.role === "user"} 
                  isLoading={msg.isLoading}
                >
                  {msg.content}
                </UserMessage>
              ))}
            </div>

            {/* CommentBox en estado con mensajes */}
            <div className="p-6">
              <CommentBox 
                chatHistory={chatHistory} 
                setChatHistory={setChatHistory} 
              />
            </div>
          </>
        )}
      </div>
    </div>
  );
}