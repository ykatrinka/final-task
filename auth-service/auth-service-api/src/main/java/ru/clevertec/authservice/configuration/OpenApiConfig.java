package ru.clevertec.authservice.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenApi() {
        Components securitySchemes = new Components()
                .addSecuritySchemes("JWT authorization",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization"));

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("JWT authorization", Arrays.asList("read", "write"));

        return new OpenAPI()
                .components(securitySchemes)
                .addSecurityItem(securityRequirement);
    }
}
