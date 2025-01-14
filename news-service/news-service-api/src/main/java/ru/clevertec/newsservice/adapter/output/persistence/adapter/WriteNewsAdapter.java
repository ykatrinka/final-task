package ru.clevertec.newsservice.adapter.output.persistence.adapter;


import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;
import ru.clevertec.newsservice.adapter.input.web.news.NewsWebMapper;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.repository.NewsRepository;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.port.output.WriteNewsPort;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WriteNewsAdapter implements WriteNewsPort {

    public static final String NEWS_NOT_FOUND = "News with id %s not found";

    private final NewsRepository newsRepository;
    private final NewsWebMapper newsMapper;

    @CachePut(value = "news", key = "#result.id.toString()")
    @Override
    public News createNews(News news) {
        NewsEntity newsEntity = newsMapper.domainToEntity(news);
        NewsEntity savedNews = newsRepository.save(newsEntity);
        return newsMapper.entityToDomain(savedNews);
    }

    @CachePut(value = "news", key = "#result.id.toString()")
    @Override
    public News updateNews(News news) {
        if (!newsRepository.existsById(news.getId())) {
            throw EntityNotFoundException.getInstance(
                    String.format(NEWS_NOT_FOUND, news.getId())
            );
        }
        NewsEntity newsEntity = newsMapper.domainToEntity(news);
        NewsEntity updatedNews = newsRepository.save(newsEntity);
        return newsMapper.entityToDomain(updatedNews);
    }

    @CacheEvict(value = "news", key = "#newsId.toString()")
    @Override
    public void deleteNews(UUID newsId) {
        Optional.ofNullable(newsId)
                .map(newsRepository::findById)
                .ifPresent(news -> newsRepository.deleteById(newsId));
    }

}
