package ru.clevertec.newsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.port.input.news.CreateNewsUseCase;
import ru.clevertec.newsservice.port.input.news.command.NewsCreateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.newsservice.port.output.WriteNewsPort;

@Component
@RequiredArgsConstructor
public class CreateNewsService implements CreateNewsUseCase {

    private final WriteNewsPort newsAdapter;
    private final NewsMapper newsMapper;

    @Override
    public NewsUseCaseResult createNews(NewsCreateCommand createCommand) {
        News news = newsMapper.commandToDomain(createCommand);
        News savedNews = newsAdapter.createNews(news);
        return newsMapper.domainToUseCase(savedNews);
    }

}
