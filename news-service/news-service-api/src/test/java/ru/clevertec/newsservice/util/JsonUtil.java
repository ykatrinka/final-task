package ru.clevertec.newsservice.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@UtilityClass
public class JsonUtil {

    @SneakyThrows
    public static byte[] getRequestBodyFromFile(String fileName) throws IOException {
        return new ClassPathResource("__files/" + fileName)
                .getContentAsByteArray();
    }
}

