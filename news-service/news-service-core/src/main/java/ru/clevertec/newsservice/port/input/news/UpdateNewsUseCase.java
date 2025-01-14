package ru.clevertec.newsservice.port.input.news;

import ru.clevertec.newsservice.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;

import java.util.UUID;

public interface UpdateNewsUseCase {
    NewsUseCaseResult updateNews(UUID newsId, NewsUpdateCommand updateCommand);
}
