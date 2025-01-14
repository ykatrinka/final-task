package ru.clevertec.newsservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class News {
    private UUID id;
    private LocalDateTime createdAt;
    private String title;
    private String text;
    private String author;
}
