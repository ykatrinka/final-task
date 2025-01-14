package ru.clevertec.loggingstarter.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

@Component
public class LoggingLevelEnvironmentPostProcessor implements EnvironmentPostProcessor {

    public static final String INFO = "INFO";
    public static final String DEBUG = "DEBUG";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication application) {
        setLevel(env, "logging.level.root", INFO);
        setLevel(env, "logging.level.org.springframework.web", INFO);
        setLevel(env, "logging.level.ru.clevertec", DEBUG);

        String property = "aop-logging.file-name";
        String fileName = "./logs/log-file.log";
        if (env.containsProperty(property)
                && env.getProperty(property) != null) {
            fileName = env.getProperty(property);
        }
        setLevel(env, "logging.file.name", fileName);
    }

    private static void setLevel(ConfigurableEnvironment env, String key, String value) {
        if (!env.containsProperty(key)) {
            env.getSystemProperties().put(key, value);
        }
    }
}
