package ru.clevertec.newsservice.cache.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.newsservice.util.Constant;
import ru.clevertec.newsservice.util.TestDataNews;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LRUCacheTest {

    private static final UUID NEWS_ID = UUID.fromString("45f1ab38-8678-4271-a0b2-82f5c4de549c");
    private static final UUID NEWS_ID_SECOND = UUID.fromString("45f1ab38-8678-4271-a0b2-82f5c4de544c");
    private static final String CHANGED_NEWS_TITLE = "Changed title in news";

    private LRUCache<UUID, NewsEntity> cache;

    @BeforeEach
    void setUp() {
        cache = new LRUCache<>();
    }

    @Test
    void shouldPutNewsInEmptyCache() {
        // given
        NewsEntity news = TestDataNews.getNewsEntitySaved();

        // when
        cache.put(news.getId(), news);

        // then
        assertNotNull(cache);
        assertTrue(cache.get(news.getId()).isPresent());
    }

    @Test
    void shouldPutNewsInNotEmptyCache() {
        // given
        NewsEntity firstNews = TestDataNews.getNewsEntitySaved();
        NewsEntity secondNews = TestDataNews.getNewsEntitySaved();
        firstNews.setId(NEWS_ID);
        secondNews.setId(NEWS_ID_SECOND);

        // when
        cache.put(firstNews.getId(), firstNews);
        cache.put(secondNews.getId(), secondNews);

        // then
        assertNotNull(cache);
        assertTrue(cache.get(secondNews.getId()).isPresent());
    }

    @Test
    void shouldPutNewsInCache_whenNewsIsExists() {
        // given
        NewsEntity firstNews = TestDataNews.getNewsEntitySaved();
        NewsEntity secondNews = TestDataNews.getNewsEntitySaved();
        firstNews.setId(NEWS_ID);
        secondNews.setId(NEWS_ID_SECOND);

        // when
        secondNews.setTitle(CHANGED_NEWS_TITLE);
        cache.put(secondNews.getId(), secondNews);

        // then
        assertNotNull(cache);
        assertTrue(cache.get(secondNews.getId()).isPresent());
        assertEquals(CHANGED_NEWS_TITLE, cache.get(secondNews.getId()).get().getTitle());
    }

    @Test
    void shouldPutNewsInNotEmptyCache_whenCapacityIsMax() {
        // given
        TestDataNews.fillLruCache(cache);
        NewsEntity news = TestDataNews.getNewsEntityForCreate();

        // when
        cache.put(news.getId(), news);

        // then
        assertNotNull(cache);
        assertTrue(cache.get(news.getId()).isPresent());
    }

    @Test
    void shouldGetNewsInCache() {
        // given
        NewsEntity news = TestDataNews.getNewsEntityForCreate();
        cache.put(news.getId(), news);

        // when
        Optional<NewsEntity> actualNews = cache.get(news.getId());

        // then
        assertTrue(actualNews.isPresent());
    }

    @Test
    void shouldNotGetNewsInCache_whenNewsNotFound() {
        // given

        // when
        Optional<NewsEntity> actualNews = cache.get(Constant.NEWS_UUID);

        // then
        assertTrue(actualNews.isEmpty());
    }

    @Test
    void shouldDeleteNewsInCache() {
        // given
        NewsEntity news = TestDataNews.getNewsEntityForCreate();
        cache.put(news.getId(), news);

        // when
        cache.delete(news.getId());
        Optional<NewsEntity> actualComment = cache.get(NEWS_ID);

        // then
        assertTrue(actualComment.isEmpty());
    }

    @Test
    void shouldNotDeleteNewsInCache_whenNewsNotFound() {
        // given
        // when
        // then
        Assertions.assertDoesNotThrow(() -> cache.delete(NEWS_ID));

    }

}