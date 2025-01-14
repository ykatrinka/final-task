package ru.clevertec.newsservice.util;

import lombok.experimental.UtilityClass;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.domain.NewsPageable;
import ru.clevertec.newsservice.port.input.news.command.NewsCreateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsPageUseCaseResult;
import ru.clevertec.newsservice.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class TestDataNews {
    /**
     * CREATE NEWS
     */
    public static News getNewsForCreate() {
        News news = new News();
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

    public static News getNewsSaved() {
        News news = new News();
        news.setId(Constant.NEWS_UUID);
        news.setCreatedAt(LocalDateTime.now());
        news.setTitle(Constant.NEWS_TITLE);
        news.setText(Constant.NEWS_TEXT);
        news.setAuthor(Constant.NEWS_AUTHOR);
        return news;
    }

    /**
     * UPDATE NEWS
     */
    public static News getNewsForUpdate() {
        News news = new News();
        news.setTitle(Constant.NEWS_TITLE_UPDATE);
        news.setText(Constant.NEWS_TEXT_UPDATE);
        news.setAuthor(Constant.NEWS_AUTHOR);
        return news;
    }

    public static NewsUpdateCommand getNewsUpdateCommand() {
        return new NewsUpdateCommand(
                Constant.NEWS_TITLE_UPDATE,
                Constant.NEWS_TEXT_UPDATE,
                Constant.NEWS_AUTHOR
        );
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
     * NEWS USE CASE RESULT
     */
    public static NewsUseCaseResult getNewsUseCaseResult() {
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

    /**
     * READ NEWS
     */
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
     * PAGEABLE
     */
    public static NewsPageable getNewsPageable() {
        return NewsPageable.builder()
                .number(Constant.PAGE_NUMBER)
                .size(Constant.PAGE_SIZE)
                .totalPages(1)
                .totalElements(1)
                .content(List.of(getNewsForRead()))
                .build();

    }

    public static NewsPageUseCaseResult getNewsPageUseCaseResult() {
        return new NewsPageUseCaseResult(
                1,
                10,
                1,
                1,
                List.of(
                        getNewsUseCaseResult()
                )
        );
    }
}
