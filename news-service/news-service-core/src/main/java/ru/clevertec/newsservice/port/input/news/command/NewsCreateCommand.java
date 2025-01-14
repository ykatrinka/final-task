package ru.clevertec.newsservice.port.input.news.command;

public record NewsCreateCommand(
        String title,
        String text,
        String author
) {
}