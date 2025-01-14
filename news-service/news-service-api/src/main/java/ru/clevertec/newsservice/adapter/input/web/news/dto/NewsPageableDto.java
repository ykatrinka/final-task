package ru.clevertec.newsservice.adapter.input.web.news.dto;

import java.util.List;

public record NewsPageableDto(
        int number,
        int size,
        int totalPages,
        long totalElements,
        List<NewsResponse> content
) {
}