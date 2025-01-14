package ru.clevertec.newsservice.port.input.comment.command;

import java.util.List;

public record CommentsUseCaseResult(
        int number,
        int size,
        int totalPages,
        long totalElements,
        List<CommentUseCase> content
) {
}