package ru.clevertec.newsservice.adapter.output.persistence.jpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.newsservice.util.Constant;
import ru.clevertec.newsservice.util.TestDataNews;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NewsRepositoryTest {

    @Autowired
    private NewsRepository newsRepository;

    @Container
    private static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Test
    void shouldCreateNews() {
        // given
        NewsEntity expectedNews = TestDataNews.getNewsEntityForCreate();

        // when
        NewsEntity actualNews = newsRepository.save(expectedNews);
        newsRepository.flush();

        // then
        assertAll(
                () -> assertNotNull(actualNews),
                () -> assertNotNull(actualNews.getId()),
                () -> assertNotNull(actualNews.getCreatedAt()),
                () -> assertEquals(expectedNews.getTitle(), actualNews.getTitle()),
                () -> assertEquals(expectedNews.getText(), actualNews.getText()),
                () -> assertEquals(expectedNews.getAuthor(), actualNews.getAuthor())
        );
    }

    @Test
    @Sql(scripts = "classpath:db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReadNews() {
        // given
        NewsEntity expectedNews = TestDataNews.getNewsEntityForRead();

        // when
        NewsEntity actualNews = newsRepository.findById(expectedNews.getId()).orElse(null);

        // then
        assertAll(
                () -> assertNotNull(actualNews),
                () -> {
                    assert actualNews != null;
                    assertNotNull(actualNews.getId());
                },
                () -> assertEquals(expectedNews.getId(), actualNews.getId()),
                () -> assertEquals(expectedNews.getTitle(), actualNews.getTitle()),
                () -> assertEquals(expectedNews.getText(), actualNews.getText()),
                () -> assertEquals(expectedNews.getAuthor(), actualNews.getAuthor())
        );
    }

    @Test
    @Sql(scripts = "classpath:db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldUpdateNews() {
        // given
        NewsEntity expectedNews = TestDataNews.getNewsEntityForUpdate();

        // when
        NewsEntity actualNews = newsRepository.save(expectedNews);

        // then
        assertAll(
                () -> assertNotNull(actualNews),
                () -> assertEquals(expectedNews.getId(), actualNews.getId()),
                () -> assertEquals(expectedNews.getTitle(), actualNews.getTitle()),
                () -> assertEquals(expectedNews.getText(), actualNews.getText()),
                () -> assertEquals(expectedNews.getAuthor(), actualNews.getAuthor())
        );
    }

    @Test
    @Sql(scripts = "classpath:db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldDeleteNews() {
        // given
        UUID newsId = Constant.NEWS_UUID;

        // when
        newsRepository.deleteById(newsId);
        NewsEntity actualNews = newsRepository.findById(newsId).orElse(null);

        // then
        Assertions.assertNull(actualNews);
    }

    @Test
    @Sql(scripts = "classpath:db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReadNewsPage() {
        // given
        NewsEntity expectedFirstNews = TestDataNews.getNewsEntityForRead();
        PageRequest pageable = TestDataNews.getPageRequest(Constant.PAGE_NUMBER - 1, Constant.PAGE_SIZE);

        // when
        Page<NewsEntity> actualNewsPage = newsRepository.findAll(pageable);
        NewsEntity actualNews = actualNewsPage.getContent().getFirst();

        // then

        assertAll(
                () -> assertNotNull(actualNewsPage.getContent()),
                () -> assertEquals(1, actualNewsPage.getTotalPages()),
                () -> assertEquals(3, actualNewsPage.getTotalElements()),
                () -> assertEquals(3, actualNewsPage.getContent().size()),
                () -> assertEquals(expectedFirstNews.getId(), actualNews.getId()),
                () -> assertEquals(expectedFirstNews.getTitle(), actualNews.getTitle()),
                () -> assertEquals(expectedFirstNews.getText(), actualNews.getText())
        );
    }

}