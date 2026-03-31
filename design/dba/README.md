# Data Dictionary

## User
Campo | Tipo | Clave | Descripción
----- | ---- | ----- | ------------
id_user | Integer | PK | Identificador único de usuario
username | Varchar(100) |  | Nombre de usuario
password | Varchar(100) |  | Contraseña del usuario
email | Varchar(200) |  | Correo electrónico
name | Varchar(100) |  | Nombre
surname | Varchar(100) |  | Apellido
birth | Date |  | Fecha de nacimiento

## City
Campo | Tipo | Clave | Descripción
----- | ---- | ----- | ------------
id_city | Integer | PK | Identificador de ciudad
name | Varchar(100) |  | Nombre de la ciudad

## University
Campo | Tipo | Clave | Descripción
----- | ---- | ----- | ------------
id_university | Integer | PK | ID de universidad
full_name | Varchar(300) |  | Nombre completo
short_name | Varchar(50) |  | Nombre corto
photo | Image |  | Foto representativa
logo | Image |  | Logo
description | Text |  | Descripción general

## Adress
Campo | Tipo | Clave | Descripción
----- | ---- | ----- | ------------
id_city | Integer | PK, FK | ID de ciudad
id_university | Integer | PK, FK | ID de universidad
address | Varchar(1024) |  | Dirección específica

## Career
Campo | Tipo | Clave | Descripción
----- | ---- | ----- | ------------
id_career | Integer | PK | ID de carrera
name | Varchar(300) |  | Nombre de la carrera
description | Text |  | Descripción de la carrera

## UniversityHasCareers
Campo | Tipo | Clave | Descripción
----- | ---- | ----- | ------------
id_university | Integer | PK, FK | Universidad asociada
id_career | Integer | PK, FK | Carrera ofrecida por la universidad

## Test
Campo | Tipo | Clave | Descripción
----- | ---- | ----- | ------------
id_test | Integer | PK | ID del test
name | Varchar(300) |  | Nombre del test
description | Text |  | Descripción general

## Question
Campo | Tipo | Clave | Descripción
----- | ---- | ----- | ------------
id_question | Integer | PK | ID de la pregunta
id_test | Integer | FK | Test al que pertenece
question | Varchar(500) |  | Enunciado de la pregunta

## QuestionFitsIn
Campo | Tipo | Clave | Descripción
----- | ---- | ----- | ------------
id_question | Integer | PK, FK | Pregunta asociada
id_career | Integer | PK, FK | Carrera a la que aplica la pregunta

## Answer
Campo | Tipo | Clave | Descripción
----- | ---- | ----- | ------------
id_answer | Integer | PK | ID de la respuesta
id_question | Integer | FK | Pregunta a la que responde
id_taken_test | Integer | FK | Test realizado al que pertenece
answer | Text |  | Contenido de la respuesta

## TakenTest
Campo | Tipo | Clave | Descripción
----- | ---- | ----- | ------------
id_taken_test | Integer | PK | ID del test tomado
id_user | Integer | FK | Usuario que realizó el test