package com.muyategna.backend.system.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger configuration for the Muyategna Backend API.
 * <p>
 * This configuration sets up OpenAPI documentation with security requirements for JWT authentication.
 */

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Muyategna Backend API",
                version = "1.0",
                description = "API documentation for Muyategna Backend"
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

//
//    @Bean
//    public OpenApiCustomizer countryCodeHeaderCustomizer() {
//        return openApi -> openApi.getPaths().values().forEach(pathItem ->
//                pathItem.readOperations().forEach(operation -> {
//                    Parameter countryCodeHeader = new Parameter()
//                            .in("header")
//                            .name("X-Country-Code")
//                            .description("Country code (e.g., ET, KE, TZ)")
//                            .required(true)
//                            .schema(new StringSchema()._default("ET"));
//                    Parameter languageHeader = new Parameter()
//                            .in("header")
//                            .name("X-Language-Code")
//                            .description("Language code (e.g., en, am, sw)")
//                            .required(true)
//                            .schema(new StringSchema()._default("am_ET"));
//
//                    operation.addParametersItem(languageHeader);
//                    operation.addParametersItem(countryCodeHeader);
//                })
//        );
//    }
}
