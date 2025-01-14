package ru.clevertec.newsservice.port.input.news;


import ru.clevertec.newsservice.port.input.news.command.NewsPageUseCaseResult;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;

import java.util.List;
import java.util.UUID;

public interface ReadNewsUseCase {
    NewsUseCaseResult readNews(UUID newsId);

    NewsPageUseCaseResult readNewsPage(int pageNumber, int pageSize);

    boolean isNewsExists(UUID newsId);

    List<NewsUseCaseResult> searchNews(String text, List<String> fields, int limit);
}
