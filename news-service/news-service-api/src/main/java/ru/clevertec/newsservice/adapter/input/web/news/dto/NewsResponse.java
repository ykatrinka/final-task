package ru.clevertec.newsservice.adapter.input.web.news.dto;

import java.util.UUID;

public record NewsResponse(
        UUID id,
        String title,
        String text,
        String author
) {
}