package ru.clevertec.commentservice.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommentPageable {
    private int number;
    private int size;
    private int totalPages;
    private long totalElements;
    private List<Comment> content;
}