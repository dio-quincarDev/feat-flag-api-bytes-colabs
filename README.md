
# 🧩 FeatureFlag API

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.1-green?logo=springboot)  
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)  
![JWT Auth](https://img.shields.io/badge/Auth-JWT-yellow?logo=jsonwebtokens)  
![License](https://img.shields.io/badge/License-MIT-lightgrey)  
![Build](https://github.com/dio-quincarDev/feat-flag-api-bytes-colabs/actions/workflows/ci.yml/badge.svg)  
![Tests](https://img.shields.io/badge/Tests-JUnit%20%2B%20Mockito-orange?logo=testing)  
![API Docs](https://img.shields.io/badge/Swagger-UI-blue?logo=swagger)

## 📌 Descripción

FeatureFlag API es una solución backend modular que permite activar o desactivar funcionalidades dinámicamente según el entorno (`dev`, `staging`, `prod`) o cliente (`acme123`, etc). Ideal para pruebas A/B, despliegues controlados y apagado rápido de features en producción.

## 🎯 Objetivos

- Registrar funcionalidades con nombre, descripción y estado por defecto
- Activarlas/desactivarlas por entorno o cliente
- Consultar si una funcionalidad está activa para un cliente y entorno
- Seguridad robusta con JWT
- Arquitectura limpia y extensible

## 🛠️ Tecnologías

| Componente       | Herramienta               |
|------------------|---------------------------|
| Lenguaje         | Java 21                   |
| Framework        | Spring Boot 3.1           |
| Base de datos    | PostgreSQL                |
| Seguridad        | Spring Security + JWT     |
| Documentación    | Swagger UI                |
| Testing          | JUnit + Mockito           |
| API Testing      | Postman                   |
| Control de versiones | Git + GitHub              |



## 🧪 Testing

- Unitario: JUnit + Mockito
- Funcional: Postman
- Manejo global de errores con `@ControllerAdvice`
- Respuestas estructuradas: `timestamp`, `code`, `message`

## 📚 Documentación

Swagger UI disponible en:
```
http://localhost:8080/swagger-ui/index.html
```

Incluye modelos de request/response, ejemplos y descripciones detalladas.

## 🧱 Arquitectura

- Modular: separación clara entre autenticación, gestión de features y configuración
- Reproducible: compatible con entornos locales sin dependencias externas
- Extensible: fácil integración con frontend o sistemas de terceros

## 🚀 Uso de la API

A continuación se muestran ejemplos de cómo interactuar con los endpoints principales de la API utilizando `curl`.

### 1. Registrar un nuevo usuario

Crea un nuevo usuario con el rol `USER`.

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
-H "Content-Type: application/json" \
-d '{
  "firstName": "Juan",
  "lastName": "Perez",
  "email": "juan.perez@example.com",
  "password": "password123"
}'
```

### 2. Iniciar sesión

Autentica a un usuario y obtén un token JWT.

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
-H "Content-Type: application/json" \
-d '{
  "email": "juan.perez@example.com",
  "password": "password123"
}'
```

**Respuesta esperada:**

```json
{
  "token": "ey...",
  "expiresIn": 3600
}
```

### 3. Verificar si un Feature Flag está activo

Consulta el estado de un feature flag para un contexto específico (en este caso, por `clientId`).

```bash
curl -X GET "http://localhost:8080/api/v1/features/check?feature=nombre-del-feature&clientId=cliente-123" \
-H "Authorization: Bearer TU_TOKEN_JWT"
```

**Respuesta esperada:**

```json
{
  "isActive": true
}
```


## 📦 Instalación rápida

```bash
git clone https://github.com/dio-quincarDev/feat-flag-api-bytes-colabs.git
cd featureflag-api
./mvnw spring-boot:run
```

## 🧪 Variables de entorno

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/featuredb  
SPRING_DATASOURCE_USERNAME=postgres  
SPRING_DATASOURCE_PASSWORD=tu_password  
JWT_SECRET=clave_super_secreta  
```

---

