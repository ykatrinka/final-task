package ru.clevertec.commentservice.port.input.command;

import java.util.UUID;

public record CommentCreateCommand(
        String author,
        UUID newsId,
        String text
) {
}
