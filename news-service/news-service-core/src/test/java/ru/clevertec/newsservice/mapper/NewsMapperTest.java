package ru.clevertec.newsservice.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.domain.NewsPageable;
import ru.clevertec.newsservice.port.input.news.command.NewsCreateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsPageUseCaseResult;
import ru.clevertec.newsservice.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.newsservice.util.TestDataNews;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class NewsMapperTest {

    @InjectMocks
    private NewsMapper newsMapper = new NewsMapperImpl();

    @Test
    void shouldConvertNewsCreateCommandToNews() {
        //given
        NewsCreateCommand expectedNews = TestDataNews.getNewsCreateCommand();

        //when
        News actualNews = newsMapper.commandToDomain(expectedNews);

        //then
        assertAll(
                () -> assertNull(actualNews.getId()),
                () -> assertNull(actualNews.getCreatedAt()),
                () -> assertEquals(expectedNews.title(), actualNews.getTitle()),
                () -> assertEquals(expectedNews.text(), actualNews.getText()),
                () -> assertEquals(expectedNews.author(), actualNews.getAuthor())
        );
    }

    @Test
    void shouldConvertNewsUpdateCommandToNews() {
        //given
        NewsUpdateCommand expectedNews = TestDataNews.getNewsUpdateCommand();

        //when
        News actualNews = newsMapper.commandToDomain(expectedNews);

        //then
        assertAll(
                () -> assertNull(actualNews.getId()),
                () -> assertNull(actualNews.getCreatedAt()),
                () -> assertEquals(expectedNews.title(), actualNews.getTitle()),
                () -> assertEquals(expectedNews.text(), actualNews.getText()),
                () -> assertEquals(expectedNews.author(), actualNews.getAuthor())
        );
    }


    @Test
    void shouldConvertNewsToNewsUseCaseResult() {
        //given
        News expectedNews = TestDataNews.getNewsForRead();

        //when
        NewsUseCaseResult actualNews = newsMapper.domainToUseCase(expectedNews);

        //then
        assertAll(
                () -> assertEquals(expectedNews.getId(), actualNews.id()),
                () -> assertEquals(expectedNews.getTitle(), actualNews.title()),
                () -> assertEquals(expectedNews.getText(), actualNews.text()),
                () -> assertEquals(expectedNews.getAuthor(), actualNews.author())
        );
    }

    @Test
    void shouldConvertNewsPageableToNewsPageUseCaseResult() {
        //given
        NewsPageable expectedNewsPage = TestDataNews.getNewsPageable();

        //when
        NewsPageUseCaseResult actualNewsPage = newsMapper.pageToUseCase(expectedNewsPage);

        //then
        assertAll(
                () -> assertNotNull(actualNewsPage),
                () -> assertEquals(expectedNewsPage.getContent().size(), actualNewsPage.content().size()),
                () -> assertNotNull(actualNewsPage.content().getFirst()),
                () -> assertEquals(expectedNewsPage.getContent().getFirst().getId(), actualNewsPage.content().getFirst().id()),
                () -> assertEquals(expectedNewsPage.getContent().getFirst().getTitle(), actualNewsPage.content().getFirst().title()),
                () -> assertEquals(expectedNewsPage.getContent().getFirst().getText(), actualNewsPage.content().getFirst().text()),
                () -> assertEquals(expectedNewsPage.getContent().getFirst().getAuthor(), actualNewsPage.content().getFirst().author())
        );
    }


}