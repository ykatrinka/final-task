package ru.clevertec.commentservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Comment {
    private UUID id;
    private LocalDateTime createdAt;
    private String text;
    private String author;
    private UUID newsId;

}
