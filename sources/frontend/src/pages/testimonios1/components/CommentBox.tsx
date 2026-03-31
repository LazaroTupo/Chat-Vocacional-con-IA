// CommentInput.tsx
import { useState } from 'react';
import Picker from '@emoji-mart/react';
import data from '@emoji-mart/data';

export default function CommentBox() {
  const [comment, setComment] = useState('');
  const [showEmojiPicker, setShowEmojiPicker] = useState(false);

  const addEmoji = (emoji: any) => {
    setComment(prev => prev + emoji.native);

  };

  const handleSend = () => {
    if (comment.trim()) {
      console.log('Comentario enviado:', comment);
      setShowEmojiPicker(false)
      setComment('');
    }
  };

  return (
    <div className="relative w-full mx-auto">
      <div className="bg-white rounded-xl p-4 shadow-[0_0_20px_rgba(0,0,0,0.1)]">
        <textarea
          rows={3}
          className="w-full resize-none outline-none text-gray-700"
          placeholder="Añade tu comentario aquí..."
          value={comment}
          onChange={e => setComment(e.target.value)}
        />
        <div className="flex justify-end items-center gap-4">
          <button
            onClick={() => setShowEmojiPicker(!showEmojiPicker)}
            className={`cursor-pointer text-lg hover:text-main-500 transition ${showEmojiPicker ? 'text-main-500' : 'text-gray-400'}`}
          >
            <i className="fa-regular fa-face-smile"></i>
          </button>
          <button
            onClick={handleSend}
            className="cursor-pointer text-lg text-gray-400 hover:text-main-500 transition"
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
