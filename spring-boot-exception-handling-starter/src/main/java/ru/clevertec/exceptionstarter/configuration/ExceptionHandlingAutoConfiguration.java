package ru.clevertec.exceptionstarter.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.exceptionstarter.handler.GlobalExceptionHandling;

@Configuration
@EnableConfigurationProperties(ExceptionHandlingProperties.class)
public class ExceptionHandlingAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "exception-handling.enable", havingValue = "true")
    public GlobalExceptionHandling globalExceptionHandling() {
        return new GlobalExceptionHandling();
    }


}
