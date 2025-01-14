package ru.clevertec.newsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.newsservice.port.input.news.DeleteNewsUseCase;
import ru.clevertec.newsservice.port.output.WriteNewsPort;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteNewsService implements DeleteNewsUseCase {

    private final WriteNewsPort newsAdapter;

    @Override
    public void deleteNews(UUID newsId) {
        newsAdapter.deleteNews(newsId);
    }

}
