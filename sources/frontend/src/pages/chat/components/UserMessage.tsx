interface UserMessageProps {
  user: boolean;
  isLoading?: boolean;
  children: string;
}

export default function UserMessage({ user, isLoading, children }: UserMessageProps) {
  return (
    <div className={`flex ${user ? 'justify-end' : 'justify-start'}`}>
      <div className={`max-w-[70%] rounded-3xl p-4 ${
        user 
          ? 'bg-[#8379F5] text-white rounded-br-none' 
          : 'bg-gray-100 text-gray-800 rounded-bl-none'
      }`}>
        {isLoading ? (
          <div className="flex space-x-2">
            <div className="w-2 h-2 bg-current rounded-full animate-bounce"></div>
            <div className="w-2 h-2 bg-current rounded-full animate-bounce" style={{ animationDelay: '0.2s' }}></div>
            <div className="w-2 h-2 bg-current rounded-full animate-bounce" style={{ animationDelay: '0.4s' }}></div>
          </div>
        ) : (
          <p className="whitespace-pre-wrap">{children}</p>
        )}
      </div>
    </div>
  );
}