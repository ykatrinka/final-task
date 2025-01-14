package ru.clevertec.newsservice.domain.comment;

import java.util.List;

public record CommentsPage(
        int number,
        int size,
        int totalPages,
        long totalElements,
        List<Comment> content
) {
}