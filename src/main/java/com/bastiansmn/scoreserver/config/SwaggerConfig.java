package com.bastiansmn.scoreserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(paths())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Score Server API")
                .description("A simple score server API to help you keep track of your users scores")
                .contact(new Contact(
                        "Bastian SOMON",
                        "https://bastian-somon.fr",
                        "bastian.somon@gmail.com"
                ))
                .version("1.0")
                .build();
    }

    private Predicate<String> paths() {
        return PathSelectors.regex("/error").negate().and(PathSelectors.regex("/").negate());
    }

}
