package ru.clevertec.commentservice.port.input.command;

import java.util.UUID;

public record CommentUseCaseResult(
        UUID id,
        String author,
        UUID newsId,
        String text
) {
}
