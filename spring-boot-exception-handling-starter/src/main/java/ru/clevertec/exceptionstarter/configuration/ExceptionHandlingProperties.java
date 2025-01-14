package ru.clevertec.exceptionstarter.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("exception-handling")
@Component
public class ExceptionHandlingProperties {
    private boolean enable;
}
