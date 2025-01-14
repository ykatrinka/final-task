package ru.clevertec.newsservice.port.output;


import ru.clevertec.newsservice.domain.News;

import java.util.UUID;

public interface WriteNewsPort {

    News createNews(News news);

    News updateNews(News news);

    void deleteNews(UUID newsId);

}
