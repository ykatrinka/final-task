package ru.clevertec.newsservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.newsservice.port.output.WriteNewsPort;
import ru.clevertec.newsservice.util.Constant;
import ru.clevertec.newsservice.util.TestDataNews;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateNewsServiceTest {

    @Mock
    private WriteNewsPort newsAdapter;

    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private UpdateNewsService updateNewsService;

    @Test
    void shouldUpdateNews() {
        // given
        UUID newsId = Constant.NEWS_UUID;
        NewsUpdateCommand newsCommand = TestDataNews.getNewsUpdateCommand();
        News news = TestDataNews.getNewsForUpdate();
        News updatedNews = TestDataNews.getNewsUpdated();
        NewsUseCaseResult expectedNews = TestDataNews.getNewsUseCaseResultForUpdate();

        when(newsMapper.commandToDomain(newsCommand)).thenReturn(news);
        when(newsAdapter.updateNews(news)).thenReturn(updatedNews);
        when(newsMapper.domainToUseCase(updatedNews)).thenReturn(expectedNews);

        // when
        NewsUseCaseResult actualNews = updateNewsService.updateNews(newsId, newsCommand);

        // then
        assertAll(
                () -> assertNotNull(actualNews),
                () -> assertNotNull(actualNews.id()),
                () -> assertEquals(expectedNews.title(), actualNews.title()),
                () -> assertEquals(expectedNews.text(), actualNews.text()),
                () -> assertEquals(expectedNews.author(), actualNews.author())
        );

        verify(newsMapper, times(1)).commandToDomain(newsCommand);
        verify(newsAdapter, times(1)).updateNews(news);
        verify(newsMapper, times(1)).domainToUseCase(updatedNews);
    }

}