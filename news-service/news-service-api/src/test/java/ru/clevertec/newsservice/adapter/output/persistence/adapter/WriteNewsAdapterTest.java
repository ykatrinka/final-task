package ru.clevertec.newsservice.adapter.output.persistence.adapter;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;
import ru.clevertec.newsservice.adapter.input.web.news.NewsWebMapper;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.repository.NewsRepository;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.util.Constant;
import ru.clevertec.newsservice.util.TestDataNews;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriteNewsAdapterTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsWebMapper newsMapper;

    @InjectMocks
    private WriteNewsAdapter writeNewsAdapter;

    @Test
    void shouldCreateNews() {
        // given
        News news = TestDataNews.getNewsForCreate();
        NewsEntity newsEntity = TestDataNews.getNewsEntityForCreate();
        NewsEntity savedNewsEntity = TestDataNews.getNewsEntitySaved();
        News expectedNews = TestDataNews.getNewsSaved();

        when(newsMapper.domainToEntity(news)).thenReturn(newsEntity);
        when(newsRepository.save(newsEntity)).thenReturn(savedNewsEntity);
        when(newsMapper.entityToDomain(savedNewsEntity)).thenReturn(expectedNews);

        // when
        News actualNews = writeNewsAdapter.createNews(news);

        // then
        assertAll(
                () -> assertNotNull(actualNews),
                () -> assertNotNull(actualNews.getId()),
                () -> assertEquals(expectedNews.getTitle(), actualNews.getTitle()),
                () -> assertEquals(expectedNews.getText(), actualNews.getText()),
                () -> assertEquals(expectedNews.getAuthor(), actualNews.getAuthor())
        );

        verify(newsMapper, times(1)).domainToEntity(news);
        verify(newsRepository, times(1)).save(newsEntity);
        verify(newsMapper, times(1)).entityToDomain(savedNewsEntity);
    }

    @Nested
    class UpdateNews {

        @Test
        void shouldUpdateNews() {
            // given
            News news = TestDataNews.getNewsForUpdate();
            NewsEntity newsEntity = TestDataNews.getNewsEntityForUpdate();
            NewsEntity updatedNewsEntity = TestDataNews.getNewsEntityUpdated();
            News expectedNews = TestDataNews.getNewsUpdated();

            when(newsRepository.existsById(news.getId())).thenReturn(true);
            when(newsMapper.domainToEntity(news)).thenReturn(newsEntity);
            when(newsRepository.save(newsEntity)).thenReturn(updatedNewsEntity);
            when(newsMapper.entityToDomain(updatedNewsEntity)).thenReturn(expectedNews);

            // when
            News actualNews = writeNewsAdapter.updateNews(news);

            // then
            assertAll(
                    () -> assertNotNull(actualNews),
                    () -> assertNotNull(actualNews.getId()),
                    () -> assertEquals(expectedNews.getTitle(), actualNews.getTitle()),
                    () -> assertEquals(expectedNews.getText(), actualNews.getText()),
                    () -> assertEquals(expectedNews.getAuthor(), actualNews.getAuthor())
            );

            verify(newsRepository, times(1)).existsById(news.getId());
            verify(newsMapper, times(1)).domainToEntity(news);
            verify(newsRepository, times(1)).save(newsEntity);
            verify(newsMapper, times(1)).entityToDomain(updatedNewsEntity);
        }

        @Test
        void shouldNotUpdateNews_whenNewsNotFound() {
            //given
            UUID newsId = Constant.NEWS_UUID;
            News news = TestDataNews.getNewsForUpdate();

            when(newsRepository.existsById(newsId)).thenReturn(false);

            //when, then
            assertThrows(EntityNotFoundException.class, () -> writeNewsAdapter.updateNews(news));

            verify(newsRepository, times(1)).existsById(newsId);
        }

    }

    @Test
    void shouldDeleteNews() {
        //given
        UUID newsId = Constant.NEWS_UUID;

        //when

        //then
        writeNewsAdapter.deleteNews(newsId);
        verify(newsRepository, times(1)).deleteById(newsId);
    }

}