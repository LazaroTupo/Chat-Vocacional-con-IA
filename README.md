# API Documentation

## Autenticación

### 1. Registro de Usuario
**Endpoint:** `POST /api/auth/register`

**Descripción:** Registra un nuevo usuario en el sistema.

**Request Body:**
```json
{
  "username": "string",
  "password": "string",
  "email": "string"
}
```

**Respuestas:**
- **201 Created:** Usuario registrado exitosamente
- **400 Bad Request:** Error en el registro (usuario ya existe, datos inválidos, etc.)

**Ejemplo de uso:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario123",
    "password": "miPassword123",
    "email": "usuario@example.com"
  }'
```

---

### 2. Login de Usuario
**Endpoint:** `POST /api/auth/login`

**Descripción:** Autentica un usuario y devuelve un token de acceso.

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Respuestas:**
- **200 OK:** Login exitoso, retorna token de autenticación
- **401 Unauthorized:** Credenciales inválidas

**Ejemplo de uso:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario123",
    "password": "miPassword123"
  }'
```

---

## Chat

> **Nota:** Todos los endpoints de chat requieren autenticación. Incluye el token JWT en el header `Authorization: Bearer <token>`.

### 3. Enviar Mensaje
**Endpoint:** `POST /api/chat/message`

**Descripción:** Envía un mensaje al sistema de chat y recibe una respuesta procesada.

**Headers:**
```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "message": "string"
}
```

**Respuesta:**

- **200 OK:** Mensaje procesado correctamente
```bash
{
  "response": "string - Respuesta del asistente vocacional",
  "sessionId": "string - ID único de la sesión de chat",
  "modelUsed": "string - Modelo de IA utilizado para generar la respuesta",
  "totalMessages": "number - Número total de mensajes en la sesión",
  "timestamp": "string - Fecha y hora de la respuesta"
}
```

**Ejemplo de uso:**
```bash
curl -X POST http://localhost:8080/api/chat/message \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Hola, necesito ayuda con orientación vocacional"
  }'
```

---

### 4. Obtener Sesión Actual
**Endpoint:** `GET /api/chat/session`

**Descripción:** Obtiene la sesión de chat activa más reciente del usuario autenticado.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Respuestas:**
- **200 OK:** Retorna la sesión actual del usuario
```bash
{
  "id": "string - ID único de la sesión",
  "userId": "string - ID del usuario propietario",
  "title": "string - Título descriptivo de la sesión",
  "createdAt": "string - Fecha de creación",
  "updatedAt": "string - Fecha de última actualización",
  "metadata": {
    "messageCount": "number - Cantidad total de mensajes",
    "lastModelUsed": "string - Último modelo de IA utilizado"
  },
  "messages": [
    {
      "id": "string - ID único del mensaje",
      "content": "string - Contenido del mensaje",
      "type": "string - Tipo (USER o SYSTEM)",
      "timestamp": "string - Fecha y hora del mensaje",
      "metadata": "object - Metadatos adicionales del mensaje"
    }
  ]
}
```

**Ejemplo de uso:**
```bash
curl -X GET http://localhost:8080/api/chat/session \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

### 5. Obtener Carreras y Universidades
**Endpoint:** `GET /api/chat/careers-and-universities`

**Descripción:** Obtiene las recomendaciones de carreras y universidades basadas en el perfil del usuario.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Respuestas:**
- **200 OK:** Lista de carreras y universidades recomendadas
```bash
{
  "careers": [
    {
      "name": "string - Nombre de la carrera",
      "description": "string - Descripción de la carrera",
      "matchReason": "string - Razón por la que fue recomendada",
      "imageUrl": "string - URL de imagen representativa",
      "keywords": "array - Palabras clave relacionadas"
    }
  ],
  "universities": [
    {
      "name": "string - Nombre de la universidad",
      "location": "string - Ubicación de la universidad",
      "careerOffered": "string - Carreras ofrecidas relacionadas",
      "proximity": "string - Información de proximidad/ubicación",
      "imageUrl": "string - URL de imagen de la universidad",
      "country": "string - País donde se encuentra"
    }
  ],
  "sessionId": "string - ID de la sesión asociada",
  "generatedAt": "string - Fecha y hora de generación"
}
```

- **500 Internal Server Error:** Error al procesar la solicitud

**Ejemplo de uso:**
```bash
curl -X GET http://localhost:8080/api/chat/careers-and-universities \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

### 6. Limpiar Sesión Actual
**Endpoint:** `DELETE /api/chat/session`

**Descripción:** Elimina todos los mensajes de la sesión actual del usuario.

**Headers:**
```
Authorization: Bearer <jwt_token>
```

**Respuestas:**
- **200 OK:** Sesión limpiada exitosamente
- **404 Not Found:** No hay mensajes para borrar en la sesión actual

**Ejemplo de uso:**
```bash
curl -X DELETE http://localhost:8080/api/chat/session \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## Notas Importantes

1. **Autenticación:** Los endpoints de chat requieren un token JWT válido obtenido tras el login.
2. **Base URL:** Reemplaza `http://localhost:8080` con la URL de tu servidor.
3. **Content-Type:** Siempre usa `application/json` para las solicitudes POST.
4. **Manejo de Errores:** Todos los endpoints pueden retornar errores 500 en caso de problemas del servidor.

## Flujo Típico de Uso

1. Registrar usuario (`POST /api/auth/register`)
2. Hacer login (`POST /api/auth/login`) y guardar el token
3. Usar el token para interactuar con los endpoints de chat
4. Enviar mensajes (`POST /api/chat/message`)
5. Consultar carreras y universidades (`GET /api/chat/careers-and-universities`)
6. Opcionalmente limpiar la sesión (`DELETE /api/chat/session`)
