package ru.clevertec.newsservice.adapter.input.web.news.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record NewsCreateDto(
        @NotBlank(message = "Title is null or empty")
        @Length(max = 255, message = "Title is more than 255 characters")
        String title,
        @NotBlank(message = "Text is null or empty")
        String text,
        @NotBlank(message = "Author is null or empty")
        String author
) {
}