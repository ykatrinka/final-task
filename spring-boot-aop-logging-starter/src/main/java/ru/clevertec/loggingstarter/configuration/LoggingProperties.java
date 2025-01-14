package ru.clevertec.loggingstarter.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("aop-logging")
public class LoggingProperties {
    private boolean enable;
    private String fileName;
}
