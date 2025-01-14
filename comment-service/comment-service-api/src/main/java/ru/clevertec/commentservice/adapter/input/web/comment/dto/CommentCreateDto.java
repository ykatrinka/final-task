package ru.clevertec.commentservice.adapter.input.web.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record CommentCreateDto(
        @NotBlank(message = "Author is null or empty")
        @Length(max = 50, message = "Author is more than 50 characters")
        String author,
        @NotNull(message = "News id is null or empty")
        UUID newsId,
        @NotBlank(message = "Text is null or empty")
        String text
) {
}