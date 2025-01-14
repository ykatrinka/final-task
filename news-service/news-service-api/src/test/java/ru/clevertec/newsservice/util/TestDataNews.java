package ru.clevertec.newsservice.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsCreateDto;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsPageableDto;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsResponse;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsUpdateDto;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.newsservice.cache.impl.LFUCache;
import ru.clevertec.newsservice.cache.impl.LRUCache;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.domain.NewsPageable;
import ru.clevertec.newsservice.port.input.news.command.NewsCreateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsPageUseCaseResult;
import ru.clevertec.newsservice.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class TestDataNews {
    /**
     * FOR CREATE NEW
     */

    public static NewsEntity getNewsEntityForCreate() {
        return NewsEntity.builder()
                .title(Constant.NEWS_TITLE)
                .text(Constant.NEWS_TEXT)
                .author(Constant.NEWS_AUTHOR)
                .build();
    }

    public static NewsEntity getNewsEntitySaved() {
        return NewsEntity.builder()
                .id(Constant.NEWS_UUID)
                .title(Constant.NEWS_TITLE)
                .text(Constant.NEWS_TEXT)
                .author(Constant.NEWS_AUTHOR)
                .build();
    }

    public static NewsCreateDto getNewsCreateDto() {
        return new NewsCreateDto(
                Constant.NEWS_TITLE,
                Constant.NEWS_TEXT,
                Constant.NEWS_AUTHOR
        );
    }

    public static News getNewsForCreate() {
        News news = new News();
        news.setTitle(Constant.NEWS_TITLE);
        news.setText(Constant.NEWS_TEXT);
        news.setAuthor(Constant.NEWS_AUTHOR);
        return news;

    }

    public static News getNewsSaved() {
        News news = new News();
        news.setId(Constant.NEWS_UUID);
        news.setCreatedAt(LocalDateTime.now());
        news.setTitle(Constant.NEWS_TITLE);
        news.setText(Constant.NEWS_TEXT);
        news.setAuthor(Constant.NEWS_AUTHOR);
        return news;
    }

    public static NewsCreateCommand getNewsCreateCommand() {
        return new NewsCreateCommand(
                Constant.NEWS_TITLE,
                Constant.NEWS_TEXT,
                Constant.NEWS_AUTHOR
        );
    }

    /**
     * READ NEWS
     */
    public static NewsEntity getNewsEntityForRead() {
        return NewsEntity.builder()
                .id(Constant.NEWS_UUID)
                .title(Constant.NEWS_TITLE)
                .text(Constant.NEWS_TEXT)
                .author(Constant.NEWS_AUTHOR)
                .build();
    }

    public static News getNewsForRead() {
        News news = new News();
        news.setId(Constant.NEWS_UUID);
        news.setCreatedAt(LocalDateTime.now());
        news.setTitle(Constant.NEWS_TITLE);
        news.setText(Constant.NEWS_TEXT);
        news.setAuthor(Constant.NEWS_AUTHOR);
        return news;
    }

    /**
     * NEWS USE CASE RESULT
     */
    public static NewsUseCaseResult getNewsUseCaseResultForCreate() {
        return new NewsUseCaseResult(
                Constant.NEWS_UUID,
                Constant.NEWS_TITLE,
                Constant.NEWS_TEXT,
                Constant.NEWS_AUTHOR
        );
    }

    public static NewsUseCaseResult getNewsUseCaseResultForRead() {
        return new NewsUseCaseResult(
                Constant.NEWS_UUID,
                Constant.NEWS_TITLE,
                Constant.NEWS_TEXT,
                Constant.NEWS_AUTHOR
        );
    }

    public static NewsUseCaseResult getNewsUseCaseResultForUpdate() {
        return new NewsUseCaseResult(
                Constant.NEWS_UUID,
                Constant.NEWS_TITLE_UPDATE,
                Constant.NEWS_TEXT_UPDATE,
                Constant.NEWS_AUTHOR
        );
    }

    public static NewsPageUseCaseResult getNewsPageUseCaseResult() {
        return new NewsPageUseCaseResult(
                Constant.PAGE_NUMBER,
                Constant.PAGE_SIZE,
                1,
                1,
                List.of(
                        getNewsUseCaseResultForRead()
                )
        );
    }

    /**
     * UPDATE NEWS
     */
    public static NewsEntity getNewsEntityForUpdate() {
        return NewsEntity.builder()
                .id(Constant.NEWS_UUID)
                .title(Constant.NEWS_TITLE_UPDATE)
                .text(Constant.NEWS_TEXT_UPDATE)
                .author(Constant.NEWS_AUTHOR)
                .build();
    }

    public static NewsEntity getNewsEntityUpdated() {
        return NewsEntity.builder()
                .id(Constant.NEWS_UUID)
                .title(Constant.NEWS_TITLE_UPDATE)
                .text(Constant.NEWS_TEXT_UPDATE)
                .author(Constant.NEWS_AUTHOR)
                .build();
    }

    public static NewsUpdateDto getNewsUpdateDto() {
        return new NewsUpdateDto(
                Constant.NEWS_TITLE_UPDATE,
                Constant.NEWS_TEXT_UPDATE,
                Constant.NEWS_AUTHOR
        );
    }

    public static NewsUpdateCommand getNewsUpdateCommand() {
        return new NewsUpdateCommand(
                Constant.NEWS_TITLE_UPDATE,
                Constant.NEWS_TEXT_UPDATE,
                Constant.NEWS_AUTHOR
        );
    }

    public static News getNewsForUpdate() {
        News news = new News();
        news.setId(Constant.NEWS_UUID);
        news.setCreatedAt(LocalDateTime.now());
        news.setTitle(Constant.NEWS_TITLE_UPDATE);
        news.setText(Constant.NEWS_TEXT_UPDATE);
        news.setAuthor(Constant.NEWS_AUTHOR);
        return news;
    }

    public static News getNewsUpdated() {
        News news = new News();
        news.setId(Constant.NEWS_UUID);
        news.setCreatedAt(LocalDateTime.now());
        news.setTitle(Constant.NEWS_TITLE_UPDATE);
        news.setText(Constant.NEWS_TEXT_UPDATE);
        news.setAuthor(Constant.NEWS_AUTHOR);
        return news;
    }

    /**
     * PAGEABLE
     */
    public static PageRequest getPageRequest(int page, int size) {
        return PageRequest.of(page, size);
    }

    public static Page<NewsEntity> getPageNewsEntity() {
        return new PageImpl<>(List.of(getNewsEntityForRead()),
                getPageRequest(0, 10), 1);
    }

    public static NewsPageable getNewsPageable() {
        return NewsPageable.builder()
                .number(Constant.PAGE_NUMBER)
                .size(Constant.PAGE_SIZE)
                .totalPages(1)
                .totalElements(1)
                .content(List.of(getNewsForRead()))
                .build();

    }

    /**
     * NEWS RESPONSE
     */
    public static NewsResponse getNewsResponseForCreate() {
        return new NewsResponse(
                Constant.NEWS_UUID,
                Constant.NEWS_TITLE,
                Constant.NEWS_TEXT,
                Constant.NEWS_AUTHOR
        );
    }

    public static NewsResponse getNewsResponseForRead() {
        return new NewsResponse(
                Constant.NEWS_UUID,
                Constant.NEWS_TITLE,
                Constant.NEWS_TEXT,
                Constant.NEWS_AUTHOR
        );
    }

    public static NewsResponse getNewsResponseForUpdate() {
        return new NewsResponse(
                Constant.NEWS_UUID,
                Constant.NEWS_TITLE_UPDATE,
                Constant.NEWS_TEXT_UPDATE,
                Constant.NEWS_AUTHOR
        );
    }

    public static NewsPageableDto getNewsPageableDto() {
        return new NewsPageableDto(
                Constant.PAGE_NUMBER,
                Constant.PAGE_SIZE,
                1,
                1,
                List.of(
                        getNewsResponseForRead()
                )
        );
    }

    /**
     * CACHE
     */

    public static void fillLfuCache(LFUCache<UUID, NewsEntity> cache) {
        for (int i = 0; i < 10; i++) {
            NewsEntity news = getNewsEntityForCreate();
            news.setId(UUID.randomUUID());
            cache.put(news.getId(), news);
        }
    }

    public static void fillLruCache(LRUCache<UUID, NewsEntity> cache) {
        for (int i = 0; i < 10; i++) {
            NewsEntity news = getNewsEntityForCreate();
            news.setId(UUID.randomUUID());
            cache.put(news.getId(), news);
        }
    }

    //search
    public static List<NewsUseCaseResult> getListNewsUseCaseResult() {
        return List.of(
                new NewsUseCaseResult(
                        Constant.NEWS_UUID,
                        Constant.NEWS_TITLE,
                        Constant.NEWS_TEXT,
                        Constant.NEWS_AUTHOR
                ),
                new NewsUseCaseResult(
                        Constant.NEWS_UUID,
                        Constant.NEWS_TITLE,
                        Constant.NEWS_TEXT,
                        Constant.NEWS_AUTHOR
                )
        );
    }

    public static List<NewsResponse> getListNewsResponse() {
        return List.of(
                new NewsResponse(
                        Constant.NEWS_UUID,
                        Constant.NEWS_TITLE,
                        Constant.NEWS_TEXT,
                        Constant.NEWS_AUTHOR
                ),
                new NewsResponse(
                        Constant.NEWS_UUID,
                        Constant.NEWS_TITLE,
                        Constant.NEWS_TEXT,
                        Constant.NEWS_AUTHOR
                )
        );
    }


}