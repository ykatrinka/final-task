package ru.clevertec.commentservice.adapter.input.web.comment.dto;

import java.util.UUID;

public record CommentResponse(
        UUID id,
        String author,
        UUID newsId,
        String text
) {
}
