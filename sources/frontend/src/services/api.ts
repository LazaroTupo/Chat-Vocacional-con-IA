import { getGlobalCredentials } from '../components/ModalLogin';

const API_BASE_URL = 'http://localhost:8080/api';

// Función para obtener la sesión actual del usuario
export const getCurrentSession = async () => {
  const { username, password } = getGlobalCredentials();
  
  if (!username || !password) {
    throw new Error('Usuario no autenticado');
  }

  const response = await fetch(`${API_BASE_URL}/chat/session`, {
    method: 'GET',
    headers: {
      'Authorization': 'Basic ' + btoa(`${username}:${password}`),
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    if (response.status === 404) {
      // No hay sesión activa, retornar array vacío
      return { messages: [] };
    }
    throw new Error('Error al obtener la sesión');
  }

  return response.json();
};

// Función para enviar mensaje al chat
export const sendChatMessage = async (message: string) => {
  const { username, password } = getGlobalCredentials();
  
  if (!username || !password) {
    throw new Error('Usuario no autenticado');
  }

  const response = await fetch(`${API_BASE_URL}/chat/message`, {
    method: 'POST',
    headers: {
      'Authorization': 'Basic ' + btoa(`${username}:${password}`),
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ message }),
  });

  if (!response.ok) {
    throw new Error('Error al enviar mensaje');
  }

  return response.json();
};

// Función para limpiar la sesión
export const clearChatSession = async () => {
  const { username, password } = getGlobalCredentials();
  
  if (!username || !password) {
    throw new Error('Usuario no autenticado');
  }

  const response = await fetch(`${API_BASE_URL}/chat/session`, {
    method: 'DELETE',
    headers: {
      'Authorization': 'Basic ' + btoa(`${username}:${password}`),
    },
  });

  if (!response.ok && response.status !== 404) {
    throw new Error('Error al limpiar la sesión');
  }

  return { success: true };
};

// Función para obtener recomendaciones de carreras y universidades
export const getCareerAndUniversityRecommendations = async () => {
  const { username, password } = getGlobalCredentials();
  
  if (!username || !password) {
    throw new Error('Usuario no autenticado');
  }

  const response = await fetch(`${API_BASE_URL}/chat/careers-and-universities`, {
    method: 'GET',
    headers: {
      'Authorization': 'Basic ' + btoa(`${username}:${password}`),
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    throw new Error('Error al obtener recomendaciones');
  }

  return response.json();
};