package ru.clevertec.loggingstarter.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.clevertec.loggingstarter.aop.LoggingAspect;

@AutoConfiguration
@EnableConfigurationProperties(LoggingProperties.class)
public class LoggingAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "aop-logging.enable", havingValue = "true")
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

}
