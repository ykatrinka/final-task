package ru.clevertec.newsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.port.input.news.UpdateNewsUseCase;
import ru.clevertec.newsservice.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.newsservice.port.output.WriteNewsPort;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateNewsService implements UpdateNewsUseCase {

    private final WriteNewsPort newsAdapter;
    private final NewsMapper newsMapper;

    @Override
    public NewsUseCaseResult updateNews(UUID newsId, NewsUpdateCommand updateCommand) {
        News news = newsMapper.commandToDomain(updateCommand);
        news.setId(newsId);
        News updatedNews = newsAdapter.updateNews(news);
        return newsMapper.domainToUseCase(updatedNews);
    }

}
