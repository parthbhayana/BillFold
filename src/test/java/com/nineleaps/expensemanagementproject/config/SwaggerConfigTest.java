package com.nineleaps.expensemanagementproject.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import org.junit.jupiter.api.Assertions;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SwaggerConfigTest {

    @Autowired
    private SwaggerConfig swaggerConfig;

    @Autowired
    private Docket docket;

    @Test
    void api_ReturnsNonNullDocket() {
        assertNotNull(docket);
    }

    @Test
    void api_ConfiguresSwagger2DocumentationType() {
        DocumentationType documentationType = docket.getDocumentationType();
        assertEquals(DocumentationType.SWAGGER_2, documentationType);
    }


//    @Test
//    void api_ConfiguresBasePackage() {
//        String groupName = docket.getGroupName();
//        Assertions.assertEquals("com.nineleaps.expensemanagementproject", groupName,
//                "Base package configuration does not match");
//    }

//    @Test
//    void api_ConfiguresSecuritySchemes() {
//        List<SecurityConfiguration> securitySchemes = docket.getSecuritySchemes();
//        assertEquals(1, securitySchemes.size());
//        assertEquals("JWT", securitySchemes.get(0).getName());
//        assertEquals("Authorization", securitySchemes.get(0).getKeyName());
//        assertEquals("header", securitySchemes.get(0).getType());
//    }
//
//    @Test
//    void api_ConfiguresSecurityContext() {
//        List<SecurityContext> securityContexts = docket.getSecurityContexts();
//        assertEquals(1, securityContexts.size());
//        assertEquals("JWT", securityContexts.get(0).getSecurityReferences().get(0).getReference());
//    }
//
//    @Test
//    void api_ConfiguresApiInfo() {
//        ApiInfo apiInfo = docket.getApiInfo();
//        assertNotNull(apiInfo);
//        assertEquals("BillFold", apiInfo.getTitle());
//        // Add assertions for other properties of ApiInfo as needed
//    }
}
