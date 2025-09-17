package com.bytes_colaborativos.api.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "JWT";

        return new OpenAPI()
                .info(new Info()
                        .title("Feature Flag API")
                        .version("1.0")
                        .description("API modular para la gestión dinámica de funcionalidades en entornos distribuidos.\n" +
                                "Diseñada para facilitar:\n" +
                                "- Activación condicional de features por entorno, cliente o rol\n" +
                                "- Resolución de flags en tiempo de ejecución con control de contexto\n" +
                                "- Integración con pipelines CI/CD y despliegues progresivos\n" +
                                "- Auditoría de cambios y trazabilidad de decisiones técnicas\n" +
                                "- Autonomía en la configuración mediante alias, compartimentos y control explícito\n" +
                                "- Seguridad basada en JWT y control granular de acceso\n" +
                                "Ideal para arquitecturas backend reproducibles y equipos que priorizan control técnico y modularidad.")
                        .contact(new Contact()
                                .name("André Garcia")
                                .url("https://github.com/AndreGarT"))
                        .contact(new Contact()
                                .name("Diógenes Quintero")
                                .email("dio-quincar@outlook.com")
                                .url("https://github.com/dio-quincarDev"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(HttpHeaders.AUTHORIZATION)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .description("JWT authentication required for protected endpoints.\n" +
                                                "Obtain token through authentication endpoints.\n" +
                                                "Format: `Bearer <your-token>`")));
    }
}