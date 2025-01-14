package ru.clevertec.newsservice.adapter.input.web.comment.dto;

import java.util.List;

public record CommentsPageResponse(
        int number,
        int size,
        int totalPages,
        long totalElements,
        List<CommentResponse> content
) {
}