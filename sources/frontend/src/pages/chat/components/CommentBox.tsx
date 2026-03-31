import { useState } from "react";
import Picker from "@emoji-mart/react";
import data from "@emoji-mart/data";
import { sendChatMessage } from "../../../services/api";
import { ChatHistoryItem } from "../ChatPage";

interface EmojiData {
  native: string;
  unified: string;
  id: string;
  name: string;
}

interface Props {
  chatHistory: ChatHistoryItem[];
  setChatHistory: React.Dispatch<React.SetStateAction<ChatHistoryItem[]>>;
}

export default function CommentBox({ chatHistory, setChatHistory }: Props) {
  const [comment, setComment] = useState("");
  const [showEmojiPicker, setShowEmojiPicker] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const addEmoji = (emoji: EmojiData) => {
    setComment((prev) => prev + emoji.native);
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  const handleSend = async () => {
    const userMessage = comment.trim();
    if (!userMessage || isLoading) return;

    setComment("");
    setShowEmojiPicker(false);
    setIsLoading(true);

    // Agregar mensaje del usuario inmediatamente
    const userMessageItem: ChatHistoryItem = { 
      role: "user", 
      content: userMessage 
    };
    
    const updatedHistory = [...chatHistory, userMessageItem];
    setChatHistory(updatedHistory);

    // Agregar mensaje de carga para la IA
    const loadingMessage: ChatHistoryItem = { 
      role: "assistant", 
      content: "", 
      isLoading: true 
    };
    
    setChatHistory([...updatedHistory, loadingMessage]);

    try {
      // Enviar mensaje al backend
      const response = await sendChatMessage(userMessage);
      
      // Reemplazar el mensaje de carga con la respuesta real
      const botMessage: ChatHistoryItem = { 
        role: "assistant", 
        content: response.response || response.message || "No se pudo obtener una respuesta" 
      };
      
      const finalHistory = [...updatedHistory, botMessage];
      setChatHistory(finalHistory);

    } catch (err) {
      console.error("Error al obtener respuesta del bot", err);
      
      // En caso de error, remover el mensaje de carga y mostrar error
      const errorMessage: ChatHistoryItem = { 
        role: "assistant", 
        content: "Lo siento, hubo un error al procesar tu mensaje. Por favor, intenta nuevamente." 
      };
      
      setChatHistory([...updatedHistory, errorMessage]);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={`relative w-full mx-auto ${chatHistory.length === 0 ? 'max-w-2xl' : ''}`}>
      <div className="bg-[#8379F5] rounded-4xl text-white p-4 shadow-[0_0_20px_rgba(0,0,0,0.1)]">
        <textarea
          rows={2}
          className="w-full resize-none outline-none placeholder:text-gray-200 text-white bg-transparent"
          placeholder={chatHistory.length === 0 ? "Comienza comentando tu situación..." : "Escribe tu respuesta..."}
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          onKeyDown={handleKeyDown}
          disabled={isLoading}
        />
        <div className="flex justify-end items-center gap-4">
          <button
            onClick={() => setShowEmojiPicker(!showEmojiPicker)}
            className="cursor-pointer text-lg hover:text-gray-200 transition disabled:opacity-50"
            disabled={isLoading}
          >
            <i className="fa-regular fa-face-smile"></i>
          </button>
          <button
            onClick={handleSend}
            className="cursor-pointer text-lg text-white-400 hover:text-gray-200 transition disabled:opacity-50"
            disabled={isLoading || !comment.trim()}
          >
            <i className="fa-solid fa-paper-plane"></i>
          </button>
        </div>
      </div>

      {showEmojiPicker && (
        <div className="absolute bottom-16 right-0 z-10">
          <Picker data={data} onEmojiSelect={addEmoji} theme="light" />
        </div>
      )}
    </div>
  );
}