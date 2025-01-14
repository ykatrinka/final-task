package ru.clevertec.newsservice.adapter.output.persistence.adapter;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;
import ru.clevertec.newsservice.adapter.input.web.news.NewsWebMapper;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.repository.NewsRepository;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.domain.NewsPageable;
import ru.clevertec.newsservice.util.Constant;
import ru.clevertec.newsservice.util.TestDataNews;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadNewsAdapterTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsWebMapper newsMapper;

    @InjectMocks
    private ReadNewsAdapter readNewsAdapter;

    @Nested
    class ReadNews {

        @Test
        void shouldReadNews() {
            //given
            UUID newsId = Constant.NEWS_UUID;
            NewsEntity newsEntity = TestDataNews.getNewsEntityForRead();
            News expectedNews = TestDataNews.getNewsForRead();

            when(newsRepository.findById(newsId)).thenReturn(Optional.ofNullable(newsEntity));
            when(newsMapper.entityToDomain(newsEntity)).thenReturn(expectedNews);

            //when
            News actualNews = readNewsAdapter.readNews(newsId);

            //then
            assertAll(
                    () -> assertNotNull(actualNews),
                    () -> assertEquals(expectedNews.getId(), actualNews.getId()),
                    () -> assertEquals(expectedNews.getTitle(), actualNews.getTitle()),
                    () -> assertEquals(expectedNews.getText(), actualNews.getText()),
                    () -> assertEquals(expectedNews.getAuthor(), actualNews.getAuthor())
            );

            verify(newsRepository, times(1)).findById(newsId);
            verify(newsMapper, times(1)).entityToDomain(newsEntity);
        }

        @Test
        void shouldNotReadNews_whenNewsNotFound() {
            //given
            UUID newsId = Constant.NEWS_UUID;

            when(newsRepository.findById(newsId)).thenReturn(Optional.empty());

            //when, then
            assertThrows(EntityNotFoundException.class, () -> readNewsAdapter.readNews(newsId));

            verify(newsRepository, times(1)).findById(newsId);
        }

    }

    @Test
    void shouldReadNewsPage() {
        //given
        PageRequest pageable = TestDataNews.getPageRequest(
                Constant.PAGE_NUMBER - 1,
                Constant.PAGE_SIZE
        );
        Page<NewsEntity> pageNewsEntity = TestDataNews.getPageNewsEntity();
        NewsPageable expectedNewsPage = TestDataNews.getNewsPageable();

        when(newsRepository.findAll(pageable)).thenReturn(pageNewsEntity);
        when(newsMapper.pageToDomain(pageNewsEntity)).thenReturn(expectedNewsPage);

        //when
        NewsPageable actualNewsPage = readNewsAdapter.readNewsPage(Constant.PAGE_NUMBER, Constant.PAGE_SIZE);

        //then
        assertAll(
                () -> assertNotNull(actualNewsPage),
                () -> assertEquals(expectedNewsPage.getContent().size(), actualNewsPage.getContent().size()),
                () -> assertNotNull(actualNewsPage.getContent().getFirst()),
                () -> assertEquals(expectedNewsPage.getContent().getFirst().getId(), actualNewsPage.getContent().getFirst().getId()),
                () -> assertEquals(expectedNewsPage.getContent().getFirst().getTitle(), actualNewsPage.getContent().getFirst().getTitle()),
                () -> assertEquals(expectedNewsPage.getContent().getFirst().getText(), actualNewsPage.getContent().getFirst().getText()),
                () -> assertEquals(expectedNewsPage.getContent().getFirst().getAuthor(), actualNewsPage.getContent().getFirst().getAuthor())
        );

        verify(newsRepository, times(1)).findAll(pageable);
        verify(newsMapper, times(1)).pageToDomain(pageNewsEntity);
    }

    @Test
    void shouldReturnTrue_ifNewsIsExists() {
        //given
        UUID newsId = Constant.NEWS_UUID;

        when(newsRepository.existsById(newsId)).thenReturn(Boolean.TRUE);

        //when
        boolean actualValue = readNewsAdapter.isNewsExists(newsId);

        //then
        assertTrue(actualValue);

        verify(newsRepository, times(1)).existsById(newsId);
    }

    @Test
    void shouldReturnFalse_ifNewsIsNotExists() {
        //given
        UUID newsId = Constant.NEWS_UUID_FAIL;

        when(newsRepository.existsById(newsId)).thenReturn(Boolean.FALSE);

        //when
        boolean actualValue = readNewsAdapter.isNewsExists(newsId);

        //then
        assertFalse(actualValue);

        verify(newsRepository, times(1)).existsById(newsId);
    }


}