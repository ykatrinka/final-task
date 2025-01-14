package ru.clevertec.newsservice.port.output;


import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.domain.NewsPageable;

import java.util.List;
import java.util.UUID;

public interface ReadNewsPort {

    News readNews(UUID newsId);

    NewsPageable readNewsPage(int page, int size);

    boolean isNewsExists(UUID newsId);

    List<News> searchNews(String text, List<String> fields, int limit);
}
