package com.hakutaku.service.api.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean(value = "defaultApi2")
    public Docket createRestApi() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                        .title("demo")
                        .description("demo采用SpringBoot开发，API文档集成Swagger")
                        .version("1.0")
                        .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hakutaku.service.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(getParameterList());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //swagger UI
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        //增强版UI
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

    public List<Parameter> getParameterList() {
        ParameterBuilder token = new ParameterBuilder();
        List<Parameter> params = new ArrayList<>();
        token.name("Authorization")
                .description("令牌")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("eyJhbGciOiJIUzUxMiJ9")
                .required(false)
                .build();
        ParameterBuilder lang = new ParameterBuilder();
        lang.name("lang")
                .description("国际化")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        params.add(token.build());
        params.add(lang.build());
        return params;
    }

}
