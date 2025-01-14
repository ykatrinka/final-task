package ru.clevertec.commentservice.adapter.input.web.comment.dto;

import java.util.List;

public record CommentPageableDto(
        int number,
        int size,
        int totalPages,
        long totalElements,
        List<CommentResponse> content
) {
}
