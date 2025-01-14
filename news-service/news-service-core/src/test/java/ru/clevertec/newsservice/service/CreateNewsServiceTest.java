package ru.clevertec.newsservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.port.input.news.command.NewsCreateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.newsservice.port.output.WriteNewsPort;
import ru.clevertec.newsservice.util.TestDataNews;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateNewsServiceTest {

    @Mock
    private WriteNewsPort newsAdapter;

    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private CreateNewsService createNewsService;

    @Test
    void shouldCreateNews() {
        // given
        NewsCreateCommand newsCommand = TestDataNews.getNewsCreateCommand();
        News news = TestDataNews.getNewsForCreate();
        News savedNews = TestDataNews.getNewsSaved();
        NewsUseCaseResult expectedNews = TestDataNews.getNewsUseCaseResult();

        when(newsMapper.commandToDomain(newsCommand)).thenReturn(news);
        when(newsAdapter.createNews(news)).thenReturn(savedNews);
        when(newsMapper.domainToUseCase(savedNews)).thenReturn(expectedNews);

        // when
        NewsUseCaseResult actualNews = createNewsService.createNews(newsCommand);

        // then
        assertAll(
                () -> assertNotNull(actualNews),
                () -> assertNotNull(actualNews.id()),
                () -> assertEquals(expectedNews.title(), actualNews.title()),
                () -> assertEquals(expectedNews.text(), actualNews.text()),
                () -> assertEquals(expectedNews.author(), actualNews.author())
        );

        verify(newsMapper, times(1)).commandToDomain(newsCommand);
        verify(newsAdapter, times(1)).createNews(news);
        verify(newsMapper, times(1)).domainToUseCase(savedNews);
    }


}