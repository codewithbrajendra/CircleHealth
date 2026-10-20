package com.circlesHealth.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).
                select()
                .apis(RequestHandlerSelectors.basePackage("com.circleHealth.admin")).
                        paths(PathSelectors.any()).build()
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfo("Auth",
                "Auth service rest end points documentation", "1.0",
                "Terms of service", "s@techment.com",
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0");
    }

    private ApiKey jwtKey() {
        return new ApiKey("JWT",
                "Authorization", "header");
    }

    private ApiKey sessionKey() {
        return new ApiKey("Session",
                "SessionId", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("JWT", authorizationScopes));
        securityReferences.add(new SecurityReference("Session", authorizationScopes));
        return securityReferences;
    }
}