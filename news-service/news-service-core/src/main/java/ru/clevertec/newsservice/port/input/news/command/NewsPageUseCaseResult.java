package ru.clevertec.newsservice.port.input.news.command;

import java.util.List;

public record NewsPageUseCaseResult(
        int number,
        int size,
        int totalPages,
        long totalElements,
        List<NewsUseCaseResult> content
) {
}