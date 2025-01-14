package ru.clevertec.newsservice.port.input.news;


import org.springframework.stereotype.Service;
import ru.clevertec.newsservice.port.input.news.command.NewsCreateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;

@Service
public interface CreateNewsUseCase {
    NewsUseCaseResult createNews(NewsCreateCommand createCommand);
}
