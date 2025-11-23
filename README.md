# API Documentation

## Tabla de Contenidos
- [Autenticación](#autenticación)
- [Chat](#chat)

---

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

> **Nota:** Todos los endpoints de chat requieren autenticación. Usa Basic Authentication con tu username y password.

### 3. Enviar Mensaje
**Endpoint:** `POST /api/chat/message`

**Descripción:** Envía un mensaje al sistema de chat y recibe una respuesta procesada.

**Headers:**
```
Authorization: Basic <base64(username:password)>
Content-Type: application/json
```

**Request Body:**
```json
{
  "message": "string"
}
```

**Respuestas:**
- **200 OK:** Mensaje procesado correctamente

**Ejemplo de uso:**
```bash
curl -X POST http://localhost:8080/api/chat/message \
  -u usuario123:miPassword123 \
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
Authorization: Basic <base64(username:password)>
```

**Respuestas:**
- **200 OK:** Retorna la sesión actual del usuario

**Ejemplo de uso:**
```bash
curl -X GET http://localhost:8080/api/chat/session \
  -u usuario123:miPassword123
```

---

### 5. Obtener Carreras y Universidades
**Endpoint:** `GET /api/chat/careers-and-universities`

**Descripción:** Obtiene las recomendaciones de carreras y universidades basadas en el perfil del usuario.

**Headers:**
```
Authorization: Basic <base64(username:password)>
```

**Respuestas:**
- **200 OK:** Lista de carreras y universidades recomendadas
- **500 Internal Server Error:** Error al procesar la solicitud

**Ejemplo de uso:**
```bash
curl -X GET http://localhost:8080/api/chat/careers-and-universities \
  -u usuario123:miPassword123
```

---

### 6. Limpiar Sesión Actual
**Endpoint:** `DELETE /api/chat/session`

**Descripción:** Elimina todos los mensajes de la sesión actual del usuario.

**Headers:**
```
Authorization: Basic <base64(username:password)>
```

**Respuestas:**
- **200 OK:** Sesión limpiada exitosamente
- **404 Not Found:** No hay mensajes para borrar en la sesión actual

**Ejemplo de uso:**
```bash
curl -X DELETE http://localhost:8080/api/chat/session \
  -u usuario123:miPassword123
```

---

## Notas Importantes

1. **Autenticación en Chat:** Los endpoints de `/api/chat/*` usan **Basic Authentication**. Debes enviar tu username y password en cada petición usando el flag `-u` en curl o el header `Authorization: Basic <credentials>`.
2. **Autenticación en Auth:** Los endpoints de `/api/auth/*` (register y login) **NO requieren autenticación** ya que son para crear cuentas e iniciar sesión.
3. **Base URL:** Reemplaza `http://localhost:8080` con la URL de tu servidor.
4. **Content-Type:** Siempre usa `application/json` para las solicitudes POST.
5. **Manejo de Errores:** Todos los endpoints pueden retornar errores 500 en caso de problemas del servidor.

## Flujo Típico de Uso

1. Registrar usuario (`POST /api/auth/register`) - Sin autenticación
2. Los endpoints de chat usan las credenciales (username y password) directamente con Basic Auth
3. Enviar mensajes (`POST /api/chat/message`) - Con Basic Auth
4. Consultar carreras y universidades (`GET /api/chat/careers-and-universities`) - Con Basic Auth
5. Opcionalmente limpiar la sesión (`DELETE /api/chat/session`) - Con Basic Auth
