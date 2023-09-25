package com.nineleaps.expense_management_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        // Create a new instance of the Swagger Docket
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any()) // Define to select all paths
                .apis(RequestHandlerSelectors.basePackage("com.nineleaps.expense_management_project")) // Define the base package for API controllers
                .build()
                .securitySchemes(Arrays.asList(apiKey())) // Configure security scheme
                .securityContexts(Arrays.asList(securityContext())) // Configure security context
                .apiInfo(apiInfo()) // Configure API information
                .pathMapping("/") // Define path mapping
                .useDefaultResponseMessages(false) // Disable default response messages
                .directModelSubstitute(LocalDate.class, String.class) // Substitute LocalDate with String
                .genericModelSubstitutes(ResponseEntity.class); // Substitute ResponseEntity in models
    }

    // Define an API key for JWT authorization
    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    // Define a security context for JWT
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    // Define default authorization with a global scope
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

    // Define API information
    public ApiInfo apiInfo() {
        return new ApiInfo(
                "BillFold", // Title
                "", // Description
                "", // Version
                "", // Terms of Service URL
                new Contact("", "", ""), // Contact information
                "", // License
                "", // License URL
                Collections.emptyList() // Vendor extensions
        );
    }
}
