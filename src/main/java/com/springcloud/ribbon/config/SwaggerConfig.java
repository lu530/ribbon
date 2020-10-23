package com.springcloud.ribbon.config;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.annotation.Annotation;

/**
 * @Author: YuanZheng
 * @Description:
 * @Date: 2018/1/14 10:54
 * @Modifier:
 * @Version 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.title}")
    private String swaggerTitle;

    @Value("${swagger.description}")
    private String swaggerDescription;

    @Value("${swagger.version}")
    private String swaggerVersion;

    /**
     * 添加摘要信息(Docket)
     */
    @Bean
    public Docket controllerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title(swaggerTitle)
                        .description(swaggerDescription)
                        .contact(new Contact("xx", null, "xx@aa.com"))
                        .version(swaggerVersion)
                        .licenseUrl("/api-doc")
                        .build()
                )
                .select()
                .apis(scanApis("com.suntek.spring.cas.controller", ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 因为swagger不支持多路径和多注解同时存在的情况下扫描,所以这么写
     * 自定义swagger扫描api接口
     * @param basePackages 包路径,多个以;隔开
     * @param clazzs 注解类
     * @return
     */
    private Predicate<RequestHandler> scanApis(final String basePackages, final Class<? extends Annotation> ...clazzs) {
        return new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler requestHandler) {
                Optional<Boolean> booleanOptional = declaringClass(requestHandler)
                        .transform(handlerPackage(basePackages));

                if (booleanOptional.isPresent()) {
                    if (Boolean.TRUE.equals(booleanOptional.get())) {
                        return true;
                    }
                }

                for (Class<? extends Annotation> clazz : clazzs) {
                    if (requestHandler.isAnnotatedWith(clazz)) {
                        return true;
                    }
                }

                return false;
            }

            @Override
            public boolean test(RequestHandler requestHandler) {
                   return true;
            }
        };
    }

    private Function<Class<?>, Boolean> handlerPackage(final String basePackages) {

        return new Function<Class<?>, Boolean>() {
            @Override
            public Boolean apply(Class<?> aClass) {
                for (String strPackage : basePackages.split(";")) {
                    if (ClassUtils.getPackageName(aClass).startsWith(strPackage)) {
                        return Boolean.TRUE;
                    }
                }

                return Boolean.FALSE;
            }
        };
    }

    private Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
}