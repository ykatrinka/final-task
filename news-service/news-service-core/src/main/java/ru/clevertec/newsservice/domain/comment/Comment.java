package ru.clevertec.newsservice.domain.comment;

import java.util.UUID;

public record Comment(
        UUID id,
        String author,
        UUID newsId,
        String text
) {
}