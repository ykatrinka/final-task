package ru.clevertec.newsservice.port.input.comment.command;

import java.util.UUID;

public record CommentUseCase(
        UUID id,
        String author,
        UUID newsId,
        String text
) {
}