# 🧩 FeatureFlag API

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.1-green?logo=springboot)
![Java](https://img.shields.io/badge/Java-21-blue?logo=java&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)
![JWT Auth](https://img.shields.io/badge/Auth-JWT-yellow?logo=jsonwebtokens)
![Tests](https://img.shields.io/badge/Tests-JUnit%20%2B%20Mockito-orange?logo=testing)
![API Docs](https://img.shields.io/badge/Swagger-UI-blue?logo=swagger)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue?logo=apache)](https://opensource.org/licenses/Apache-2.0)

---

## 📖 Resumen

**FeatureFlag API** es una solución de backend que permite gestionar funcionalidades (feature flags) de forma dinámica. Permite activar o desactivar características específicas según el entorno (`dev`, `staging`, `prod`) o por identificador de cliente (`clientId`), facilitando despliegues controlados (canary releases), pruebas A/B y la desactivación rápida de funcionalidades en producción sin necesidad de un nuevo despliegue.

---

## ✨ Características Principales

- **Gestión de Usuarios:** Registro y autenticación de usuarios con roles (`ADMIN`, `USER`).
- **Seguridad:** Implementación de JSON Web Tokens (JWT) para proteger los endpoints.
- **Roles y Permisos:** Sistema de autorización para restringir operaciones críticas a roles específicos.
- **Feature Flags Dinámicos:**
  - Registrar funcionalidades con un nombre, descripción y estado por defecto.
  - Activar o desactivar flags a nivel global.
  - Configurar el estado de un flag para un entorno (`dev`, `staging`, `prod`) o `clientId` específico.
- **Consulta de Flags:** Endpoint para verificar si una funcionalidad está activa para un contexto determinado (cliente y/o entorno).
- **Documentación de API:** Documentación interactiva y autogenerada con Swagger (OpenAPI).

---

## 🛠️ Stack Tecnológico

| Componente           | Herramienta / Versión     |
|----------------------|---------------------------|
| Lenguaje             | Java 21                   |
| Framework            | Spring Boot 3.3.1         |
| Base de Datos        | PostgreSQL 15+            |
| Seguridad            | Spring Security 6 + JWT   |
| Pruebas Unitarias    | JUnit 5 + Mockito         |
| Documentación API    | SpringDoc (Swagger UI)    |
| Control de Versiones | Git + GitHub              |

---

## 🚀 Guía de Inicio Rápido

Sigue estos pasos para configurar y ejecutar el proyecto en tu entorno local.

### 1. Prerrequisitos

- **Java 21** o superior.
- **Apache Maven** 3.8+
- **PostgreSQL** 15 o superior.
- Un cliente de API como [Postman](https://www.postman.com/) o `curl`.

### 2. Instalación y Configuración

**a. Clona el repositorio:**

```bash
git clone https://github.com/dio-quincarDev/feat-flag-api-bytes-colabs.git
cd feature-flag-api
```

**b. Configura la base de datos:**

1.  Abre `psql` o tu cliente de base de datos preferido.
2.  Crea una nueva base de datos para el proyecto.

    ```sql
    CREATE DATABASE featuredb;
    ```

**c. Configura las variables de entorno:**

Crea un archivo `application.properties` dentro de `src/main/resources` (o modifica el existente) con las siguientes variables:

```properties
# Configuración de la Base de Datos
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/featuredb
SPRING_DATASOURCE_USERNAME=tu_usuario_postgres
SPRING_DATASOURCE_PASSWORD=tu_contraseña_postgres

# Clave secreta para firmar los tokens JWT
JWT_SECRET=clave_super_secreta_de_al_menos_256_bits

# Configuración de Spring
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

> **Nota de Seguridad:** En un entorno de producción, utiliza siempre variables de entorno del sistema operativo o un servicio de gestión de secretos en lugar de archivos de propiedades.

### 3. Ejecución

Utiliza el wrapper de Maven incluido para compilar y ejecutar la aplicación:

```bash
# En Windows
./mvnw spring-boot:run

# En macOS / Linux
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`.

---

## 🔌 Uso de la API

### Documentación Interactiva

La forma más sencilla de explorar y probar la API es a través de la interfaz de Swagger UI, disponible en:

**http://localhost:8080/swagger-ui/index.html**

### Ejemplos con `curl`

A continuación, se muestran algunos ejemplos de cómo interactuar con los endpoints principales.

**1. Registrar un nuevo usuario (rol `USER` por defecto):**

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
-H "Content-Type: application/json" \
-d \
'{
  "firstName": "Juan",
  "lastName": "Perez",
  "email": "juan.perez@example.com",
  "password": "password123"
}'
```

**2. Iniciar sesión para obtener un token JWT:**

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
-H "Content-Type: application/json" \
-d \
'{
  "email": "juan.perez@example.com",
  "password": "password123"
}'
```

*Respuesta esperada:*

```json
{
  "token": "ey...",
  "expiresIn": 3600
}
```

**3. Verificar si un Feature Flag está activo:**

Reemplaza `TU_TOKEN_JWT` con el token obtenido en el paso anterior.

```bash
curl -X GET "http://localhost:8080/api/v1/features/check?featureName=mi-feature&clientId=cliente-abc" \
-H "Authorization: Bearer TU_TOKEN_JWT"
```

*Respuesta esperada:*

```json
{
  "isActive": true
}
```

---

## 🧪 Testing

El proyecto utiliza **JUnit 5** y **Mockito** para las pruebas unitarias. Para ejecutar las pruebas, utiliza el siguiente comando de Maven:

```bash
./mvnw test
```

Los informes de las pruebas se generan en el directorio `target/surefire-reports`.

---

## 🏗️ Arquitectura

- **Modular:** El código está organizado por funcionalidades (`auth`, `feature`) para mantener una separación clara de responsabilidades.
- **Capas:** Sigue una arquitectura de capas clásica (Controlador, Servicio, Repositorio) para facilitar el mantenimiento y la escalabilidad.
- **Manejo de Excepciones Centralizado:** Utiliza `@ControllerAdvice` para gestionar errores de forma global y devolver respuestas consistentes.

---

## 📄 Licencia

Este proyecto está distribuido bajo la **Licencia Apache 2.0**. Consulta el archivo `LICENSE` para más detalles.