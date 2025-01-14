package ru.clevertec.newsservice.port.input.news.command;

import java.util.UUID;

public record NewsUseCaseResult(
        UUID id,
        String title,
        String text,
        String author
) {
}