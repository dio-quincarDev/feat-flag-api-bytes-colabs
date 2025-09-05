
# 🧩 FeatureFlag API

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)  
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1-green?logo=springboot)  
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)  
![JWT Auth](https://img.shields.io/badge/Auth-JWT-yellow?logo=jsonwebtokens)  
![License](https://img.shields.io/badge/License-MIT-lightgrey)  
![Build](https://img.shields.io/badge/Build-Passing-brightgreen)  
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

