package ru.clevertec.newsservice.port.input.news.command;

public record NewsUpdateCommand(
        String title,
        String text,
        String author
) {
}