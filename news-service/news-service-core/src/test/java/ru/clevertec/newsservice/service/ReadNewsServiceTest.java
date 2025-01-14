package ru.clevertec.newsservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.domain.NewsPageable;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.port.input.news.command.NewsPageUseCaseResult;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.newsservice.port.output.ReadNewsPort;
import ru.clevertec.newsservice.util.Constant;
import ru.clevertec.newsservice.util.TestDataNews;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadNewsServiceTest {

    @Mock
    private ReadNewsPort newsAdapter;

    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private ReadNewsService readNewsService;

    @Test
    void shouldReadNews() {
        //given
        UUID newsId = Constant.NEWS_UUID;
        News news = TestDataNews.getNewsForRead();
        NewsUseCaseResult expectedNews = TestDataNews.getNewsUseCaseResult();

        when(newsAdapter.readNews(newsId)).thenReturn(news);
        when(newsMapper.domainToUseCase(news)).thenReturn(expectedNews);

        //when
        NewsUseCaseResult actualNews = readNewsService.readNews(newsId);

        //then
        assertAll(
                () -> assertNotNull(actualNews),
                () -> assertEquals(expectedNews.id(), actualNews.id()),
                () -> assertEquals(expectedNews.title(), actualNews.title()),
                () -> assertEquals(expectedNews.text(), actualNews.text()),
                () -> assertEquals(expectedNews.author(), actualNews.author())
        );

        verify(newsAdapter, times(1)).readNews(newsId);
        verify(newsMapper, times(1)).domainToUseCase(news);
    }


    @Test
    void shouldReadNewsPage() {
        //given
        NewsPageable newsPage = TestDataNews.getNewsPageable();
        NewsPageUseCaseResult expectedNewsPage = TestDataNews.getNewsPageUseCaseResult();

        when(newsAdapter.readNewsPage(Constant.PAGE_NUMBER, Constant.PAGE_SIZE)).thenReturn(newsPage);
        when(newsMapper.pageToUseCase(newsPage)).thenReturn(expectedNewsPage);

        //when
        NewsPageUseCaseResult actualNewsPage = readNewsService.readNewsPage(Constant.PAGE_NUMBER, Constant.PAGE_SIZE);

        //then
        assertAll(
                () -> assertNotNull(actualNewsPage),
                () -> assertEquals(expectedNewsPage.content().size(), actualNewsPage.content().size()),
                () -> assertNotNull(actualNewsPage.content().getFirst()),
                () -> assertEquals(expectedNewsPage.content().getFirst().id(), actualNewsPage.content().getFirst().id()),
                () -> assertEquals(expectedNewsPage.content().getFirst().title(), actualNewsPage.content().getFirst().title()),
                () -> assertEquals(expectedNewsPage.content().getFirst().text(), actualNewsPage.content().getFirst().text()),
                () -> assertEquals(expectedNewsPage.content().getFirst().author(), actualNewsPage.content().getFirst().author())
        );

        verify(newsAdapter, times(1)).readNewsPage(Constant.PAGE_NUMBER, Constant.PAGE_SIZE);
        verify(newsMapper, times(1)).pageToUseCase(newsPage);
    }

    @Test
    void shouldReturnTrue_ifNewsIsExists() {
        //given
        UUID newsId = Constant.NEWS_UUID;

        when(newsAdapter.isNewsExists(newsId)).thenReturn(Boolean.TRUE);

        //when
        boolean actualValue = readNewsService.isNewsExists(newsId);

        //then
        assertTrue(actualValue);

        verify(newsAdapter, times(1)).isNewsExists(newsId);
    }

    @Test
    void shouldReturnFalse_ifNewsIsNotExists() {
        //given
        UUID newsId = Constant.NEWS_UUID;

        when(newsAdapter.isNewsExists(newsId)).thenReturn(Boolean.FALSE);

        //when
        boolean actualValue = readNewsService.isNewsExists(newsId);

        //then
        assertFalse(actualValue);

        verify(newsAdapter, times(1)).isNewsExists(newsId);
    }
}