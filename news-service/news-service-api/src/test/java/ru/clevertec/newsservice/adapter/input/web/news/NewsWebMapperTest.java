package ru.clevertec.newsservice.adapter.input.web.news;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsCreateDto;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsPageableDto;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsResponse;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsUpdateDto;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.domain.NewsPageable;
import ru.clevertec.newsservice.port.input.news.command.NewsCreateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsPageUseCaseResult;
import ru.clevertec.newsservice.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.newsservice.util.TestDataNews;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class NewsWebMapperTest {

    @InjectMocks
    private NewsWebMapper newsWebMapper = new NewsWebMapperImpl();

    @Test
    void shouldConvertNewsCreateDtoToNewsCreateCommand() {
        //given
        NewsCreateDto expectedNews = TestDataNews.getNewsCreateDto();

        //when
        NewsCreateCommand actualNews = newsWebMapper.dtoToCommand(expectedNews);

        //then
        assertAll(
                () -> assertEquals(expectedNews.title(), actualNews.title()),
                () -> assertEquals(expectedNews.text(), actualNews.text()),
                () -> assertEquals(expectedNews.author(), actualNews.author())
        );
    }

    @Test
    void shouldConvertNewsUpdateDtoToNewsUpdateCommand() {
        //given
        NewsUpdateDto expectedNews = TestDataNews.getNewsUpdateDto();

        //when
        NewsUpdateCommand actualNews = newsWebMapper.dtoToCommand(expectedNews);

        //then
        assertAll(
                () -> assertEquals(expectedNews.title(), actualNews.title()),
                () -> assertEquals(expectedNews.text(), actualNews.text()),
                () -> assertEquals(expectedNews.author(), actualNews.author())
        );
    }


    @Test
    void shouldConvertNewsToNewsEntity() {
        //given
        News expectedNews = TestDataNews.getNewsForRead();

        //when
        NewsEntity actualNews = newsWebMapper.domainToEntity(expectedNews);

        //then
        assertAll(
                () -> assertEquals(expectedNews.getId(), actualNews.getId()),
                () -> assertEquals(expectedNews.getCreatedAt(), actualNews.getCreatedAt()),
                () -> assertEquals(expectedNews.getTitle(), actualNews.getTitle()),
                () -> assertEquals(expectedNews.getText(), actualNews.getText()),
                () -> assertEquals(expectedNews.getAuthor(), actualNews.getAuthor())
        );
    }

    @Test
    void shouldConvertNewsEntityToNews() {
        //given
        NewsEntity expectedNews = TestDataNews.getNewsEntityForRead();

        //when
        News actualNews = newsWebMapper.entityToDomain(expectedNews);

        //then
        assertAll(
                () -> assertEquals(expectedNews.getId(), actualNews.getId()),
                () -> assertEquals(expectedNews.getCreatedAt(), actualNews.getCreatedAt()),
                () -> assertEquals(expectedNews.getTitle(), actualNews.getTitle()),
                () -> assertEquals(expectedNews.getText(), actualNews.getText()),
                () -> assertEquals(expectedNews.getAuthor(), actualNews.getAuthor())
        );
    }

    @Test
    void shouldConvertNewsUseCaseResultToNewsResponse() {
        //given
        NewsUseCaseResult expectedNews = TestDataNews.getNewsUseCaseResultForRead();

        //when
        NewsResponse actualNews = newsWebMapper.useCaseToDto(expectedNews);

        //then
        assertAll(
                () -> assertEquals(expectedNews.id(), actualNews.id()),
                () -> assertEquals(expectedNews.title(), actualNews.title()),
                () -> assertEquals(expectedNews.text(), actualNews.text()),
                () -> assertEquals(expectedNews.author(), actualNews.author())
        );
    }

    @Test
    void shouldConvertNewsPageUseCaseResultToNewsPageableDto() {
        //given
        NewsPageUseCaseResult expectedNewsPage = TestDataNews.getNewsPageUseCaseResult();

        //when
        NewsPageableDto actualNewsPage = newsWebMapper.useCaseToDto(expectedNewsPage);

        //then
        assertAll(
                () -> assertEquals(expectedNewsPage.number(), actualNewsPage.number()),
                () -> assertEquals(expectedNewsPage.size(), actualNewsPage.size()),
                () -> assertEquals(expectedNewsPage.totalPages(), actualNewsPage.totalPages()),
                () -> assertEquals(expectedNewsPage.totalElements(), actualNewsPage.totalElements()),
                () -> assertEquals(expectedNewsPage.content().size(), actualNewsPage.content().size()),
                () -> assertEquals(expectedNewsPage.content().getFirst().id(), actualNewsPage.content().getFirst().id()),
                () -> assertEquals(expectedNewsPage.content().getFirst().title(), actualNewsPage.content().getFirst().title()),
                () -> assertEquals(expectedNewsPage.content().getFirst().text(), actualNewsPage.content().getFirst().text()),
                () -> assertEquals(expectedNewsPage.content().getFirst().author(), actualNewsPage.content().getFirst().author())
        );
    }

    @Test
    void shouldConvertPageNewsEntityToNewsPageable() {
        //given
        Page<NewsEntity> expectedNewsPage = TestDataNews.getPageNewsEntity();

        //when
        NewsPageable actualNewsPage = newsWebMapper.pageToDomain(expectedNewsPage);

        //then
        assertAll(
                () -> assertEquals(expectedNewsPage.getNumber(), actualNewsPage.getNumber() - 1),
                () -> assertEquals(expectedNewsPage.getSize(), actualNewsPage.getSize()),
                () -> assertEquals(expectedNewsPage.getTotalPages(), actualNewsPage.getTotalPages()),
                () -> assertEquals(expectedNewsPage.getTotalElements(), actualNewsPage.getTotalElements()),
                () -> assertEquals(expectedNewsPage.getContent().size(), actualNewsPage.getContent().size()),
                () -> assertEquals(expectedNewsPage.getContent().getFirst().getId(), actualNewsPage.getContent().getFirst().getId()),
                () -> assertEquals(expectedNewsPage.getContent().getFirst().getTitle(), actualNewsPage.getContent().getFirst().getTitle()),
                () -> assertEquals(expectedNewsPage.getContent().getFirst().getText(), actualNewsPage.getContent().getFirst().getText()),
                () -> assertEquals(expectedNewsPage.getContent().getFirst().getAuthor(), actualNewsPage.getContent().getFirst().getAuthor())
        );
    }


}