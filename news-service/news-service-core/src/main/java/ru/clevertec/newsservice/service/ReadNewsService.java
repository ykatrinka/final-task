package ru.clevertec.newsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.domain.NewsPageable;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.port.input.news.ReadNewsUseCase;
import ru.clevertec.newsservice.port.input.news.command.NewsPageUseCaseResult;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.newsservice.port.output.ReadNewsPort;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReadNewsService implements ReadNewsUseCase {

    private final ReadNewsPort newsAdapter;
    private final NewsMapper newsMapper;


    @Override
    public NewsUseCaseResult readNews(UUID newsId) {
        News news = newsAdapter.readNews(newsId);
        return newsMapper.domainToUseCase(news);
    }

    @Override
    public NewsPageUseCaseResult readNewsPage(int pageNumber, int pageSize) {
        NewsPageable newsPageable = newsAdapter.readNewsPage(pageNumber, pageSize);
        return newsMapper.pageToUseCase(newsPageable);
    }

    @Override
    public boolean isNewsExists(UUID newsId) {
        return newsAdapter.isNewsExists(newsId);
    }

    @Override
    public List<NewsUseCaseResult> searchNews(String text, List<String> fields, int limit) {
        List<News> news = newsAdapter.searchNews(text, fields, limit);
        return news.stream()
                .map(newsMapper::domainToUseCase)
                .toList();
    }

}
