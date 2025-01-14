package ru.clevertec.commentservice.port.input.command;

import java.util.List;

public record CommentPageUseCaseResult(
        int number,
        int size,
        int totalPages,
        long totalElements,
        List<CommentUseCaseResult> content
) {
}
