package com.nineleaps.expensemanagementproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@EnableSwagger2
@Configuration
public class SwaggerConfig  {
	public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String JWT="JWT";
    public static final String HEADERS="headers";
    public static final String GLOBAL= "global";
    public static final String ACCESS_EVERYTHING= "accessEverything";
	

	    @Bean
	    public Docket api(){
	        return new Docket(DocumentationType.SWAGGER_2)
	                .apiInfo(null)
	                .securityContexts(Arrays.asList(securityContext()))
	                .securitySchemes(Arrays.asList(apiKey()))
	                .select()
	                .apis(RequestHandlerSelectors.any())
	                .paths(PathSelectors.any())
	                .build();
	    }

	    private SecurityContext securityContext(){
	        return SecurityContext.builder().securityReferences(defaultAuth()).build();
	    }

	    private ApiKey apiKey(){
	    	
	        return new ApiKey(JWT, AUTHORIZATION_HEADER, HEADERS);
	    }


	    private List<SecurityReference> defaultAuth(){
	        AuthorizationScope authorizationScope = new AuthorizationScope(GLOBAL, ACCESS_EVERYTHING);
	        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	        authorizationScopes[0] = authorizationScope;
	        return Arrays.asList(new SecurityReference(JWT, authorizationScopes));
	    }
	}
//	   Parameter authHeader = new ParameterBuilder()
//	            .parameterType("header")
//	            .name("Authorization").required(true)
//	            .modelRef(new ModelRef("string"))
//	            .build();
//
//	    @Bean
//	    public Docket api() {
//	        return new Docket(DocumentationType.SWAGGER_2).select()
//	                .apis(RequestHandlerSelectors.basePackage("com.nineleaps.expensemanagementproject"))
//	                .build()
//	                .apiInfo(apiInfo())
//	                .securitySchemes(securitySchemes()).securityContexts(List.of(securityContext()));
//	    }
//
//	    private List<SecurityScheme> securitySchemes() {
//	        return List.of(new ApiKey("Bearer","Authorization" , "header"));
//	        
//	    }
//
//	    private SecurityContext securityContext() {
//	        return SecurityContext.builder().securityReferences(List.of(bearerAuthReference())).forPaths(PathSelectors.any()).build();
//	    }
//
//	    private SecurityReference bearerAuthReference() {
//	        return new SecurityReference("Bearer", new AuthorizationScope[0]);
//	    }
//
//	    @Bean
//	    public ApiInfo apiInfo() {
//	        final ApiInfoBuilder builder = new ApiInfoBuilder();
//	        builder.title("BILLFOLD").version("1")
//	                .description("MONEY DETAILS");
//	        return builder.build();
//	    }
//
//	}
//}