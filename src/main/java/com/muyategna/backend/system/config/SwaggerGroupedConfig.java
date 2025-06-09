package com.muyategna.backend.system.config;

import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerGroupedConfig {

    /**
     * Customizes the OpenAPI documentation to include global headers for country and language codes.
     *
     * @return an OpenApiCustomizer that adds the headers to all operations.
     */
    private OpenApiCustomizer globalHeaderCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    operation.addParametersItem(new Parameter()
                            .in("header")
                            .name("X-Country-Code")
                            .description("Country code (e.g., ET, KE, TZ)")
                            .required(true)
                            .schema(new StringSchema()._default("ET")));

                    operation.addParametersItem(new Parameter()
                            .in("header")
                            .name("X-Language-Code")
                            .description("Language code (e.g., en, am, sw)")
                            .required(true)
                            .schema(new StringSchema()._default("am_ET")));
                }));
    }

    @Bean
    public GroupedOpenApi adminApiGroup() {
        return GroupedOpenApi.builder()
                .group("Admin")
                .pathsToMatch("/api/v1/admin/**")
                .addOpenApiCustomizer(globalHeaderCustomizer())
                .build();
    }

    @Bean
    public GroupedOpenApi publicApiGroup() {
        return GroupedOpenApi.builder()
                .group("Public")
                .pathsToMatch("/api/v1/public/**")
                .addOpenApiCustomizer(globalHeaderCustomizer())
                .build();
    }
}
