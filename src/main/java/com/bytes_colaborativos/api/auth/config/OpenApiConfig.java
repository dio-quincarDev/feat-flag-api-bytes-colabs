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
                        .description("Una solución backend modular que permite activar o desactivar funcionalidades dinámicamente según el entorno, cliente o rol. Ideal para pruebas A/B, despliegues controlados y apagado rápido de features en producción.\n\n**Funcionalidades principales:**\n- **Activación condicional:** Por entorno (`dev`, `staging`, `prod`), cliente (`id-cliente`) o rol de usuario.\n- **Resolución en tiempo de ejecución:** Consulta el estado de un flag con control de contexto.\n- **Seguridad:** Protegida con JWT y control de acceso granular basado en roles.\n- **Auditoría y Trazabilidad:** Facilita el seguimiento de cambios en la configuración de los flags.\n- **Arquitectura Extensible:** Diseñada para una fácil integración con sistemas de terceros y frontend.")
                        .contact(new Contact()
                                .name("André Garcia y Diógenes Quintero")
                                .url("https://github.com/dio-quincarDev/feat-flag-api-bytes-colabs.git"))
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