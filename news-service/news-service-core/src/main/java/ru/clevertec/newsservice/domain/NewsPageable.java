package ru.clevertec.newsservice.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NewsPageable {
    private int number;
    private int size;
    private int totalPages;
    private long totalElements;
    private List<News> content;
}