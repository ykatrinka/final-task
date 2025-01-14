package ru.clevertec.newsservice.adapter.input.web.news.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.newsservice.adapter.input.web.comment.dto.CommentResponse;
import ru.clevertec.newsservice.adapter.input.web.comment.dto.CommentsPageResponse;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsCreateDto;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsPageableDto;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsResponse;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsUpdateDto;
import ru.clevertec.newsservice.domain.comment.Comment;
import ru.clevertec.newsservice.domain.comment.CommentsPage;
import ru.clevertec.newsservice.util.Constant;
import ru.clevertec.newsservice.util.TestDataComment;
import ru.clevertec.newsservice.util.TestDataNews;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Sql(
        scripts = "classpath:db/data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NewsControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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
    void shouldCreateNewsAndReturnStatus201() throws Exception {
        // given
        NewsCreateDto expectedNews = TestDataNews.getNewsCreateDto();
        HttpEntity<NewsCreateDto> httpRequest = new HttpEntity<>(expectedNews);

        // when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/news",
                HttpMethod.POST,
                httpRequest,
                String.class);

        NewsResponse actualNews = objectMapper.readValue(
                responseEntity.getBody(),
                new TypeReference<>() {
                });

        // then
        assertAll(
                () -> assertEquals(CREATED, responseEntity.getStatusCode()),
                () -> assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> assertNotNull(actualNews.id()),
                () -> assertEquals(expectedNews.title(), actualNews.title()),
                () -> assertEquals(expectedNews.text(), actualNews.text()),
                () -> assertEquals(expectedNews.author(), actualNews.author())
        );
    }

    @Nested
    class ReadNews {

        @Test
        void shouldReadNews() throws Exception {
            // given
            UUID newsId = Constant.NEWS_UUID;
            NewsResponse expectedNews = TestDataNews.getNewsResponseForRead();

            // when
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    "/news/" + newsId,
                    HttpMethod.GET,
                    null,
                    String.class);


            NewsResponse actualNews = objectMapper.readValue(
                    responseEntity.getBody(),
                    new TypeReference<>() {
                    });

            // then
            assertAll(
                    () -> assertEquals(OK, responseEntity.getStatusCode()),
                    () -> assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                    () -> assertNotNull(actualNews.id()),
                    () -> assertEquals(expectedNews.id(), actualNews.id()),
                    () -> assertEquals(expectedNews.title(), actualNews.title()),
                    () -> assertEquals(expectedNews.text(), actualNews.text()),
                    () -> assertEquals(expectedNews.author(), actualNews.author())
            );
        }

        @Test
        void shouldNotReadNews_whenNewsNotFound() {
            // given
            UUID newsId = Constant.NEWS_UUID_FAIL;

            // when
            ResponseEntity<String> actualEntity = restTemplate.exchange(
                    "/news/" + newsId,
                    HttpMethod.GET,
                    null,
                    String.class);

            // then
            assertAll(
                    () -> assertEquals(NOT_FOUND, actualEntity.getStatusCode()),
                    () -> assertEquals(APPLICATION_JSON, actualEntity.getHeaders().getContentType())
            );
        }

    }

    @Nested
    class UpdateNews {

        @Test
        void shouldUpdateNews() throws Exception {
            // given
            UUID newsId = Constant.NEWS_UUID;
            NewsUpdateDto newsUpdateDto = TestDataNews.getNewsUpdateDto();
            NewsResponse expectedNews = TestDataNews.getNewsResponseForUpdate();
            HttpEntity<NewsUpdateDto> httpRequest = new HttpEntity<>(newsUpdateDto);

            // when
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    "/news/" + newsId,
                    HttpMethod.PUT,
                    httpRequest,
                    String.class);

            NewsResponse actualNews = objectMapper.readValue(
                    responseEntity.getBody(),
                    new TypeReference<>() {
                    });

            // then
            assertAll(
                    () -> assertEquals(OK, responseEntity.getStatusCode()),
                    () -> assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                    () -> assertEquals(expectedNews.id().toString(), actualNews.id().toString()),
                    () -> assertEquals(expectedNews.title(), actualNews.title()),
                    () -> assertEquals(expectedNews.text(), actualNews.text()),
                    () -> assertEquals(expectedNews.author(), actualNews.author())
            );
        }

        @Test
        void shouldNotUpdateNews_whenNewsNotFound() {
            // given
            UUID newsId = Constant.NEWS_UUID_FAIL;
            NewsUpdateDto newsUpdateDto = TestDataNews.getNewsUpdateDto();
            HttpEntity<NewsUpdateDto> httpRequest = new HttpEntity<>(newsUpdateDto);

            // when
            ResponseEntity<String> actualEntity = restTemplate.exchange(
                    "/news/" + newsId,
                    HttpMethod.PUT,
                    httpRequest,
                    String.class);

            // then
            assertAll(
                    () -> assertEquals(NOT_FOUND, actualEntity.getStatusCode()),
                    () -> assertEquals(APPLICATION_JSON, actualEntity.getHeaders().getContentType())
            );
        }
    }

    @Test
    void shouldDeleteNews() {
        // given
        UUID newsId = Constant.NEWS_UUID;

        // when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/news/" + newsId,
                HttpMethod.DELETE,
                null,
                String.class);

        // then
        assertEquals(NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void shouldReadNewsPage() throws Exception {
        // given

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/news",
                HttpMethod.GET,
                null,
                String.class);


        NewsPageableDto actualNewsPage = objectMapper.readValue(
                responseEntity.getBody(),
                new TypeReference<>() {
                });

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> assertNotNull(actualNewsPage),
                () -> assertEquals(3, actualNewsPage.content().size())
        );

    }

    @Nested
    class ExistsNews {

        @Test
        void shouldReturnTrue_ifNewsIsExists() throws Exception {
            // given
            UUID newsId = Constant.NEWS_UUID;

            // when
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    "/news/" + newsId + "/exists",
                    HttpMethod.GET,
                    null,
                    String.class);

            Boolean actualValue = objectMapper.readValue(
                    responseEntity.getBody(),
                    new TypeReference<>() {
                    });

            // then
            assertTrue(actualValue);
        }

        @Test
        void shouldReturnFalse_ifNewsIsNotExists() throws Exception {
            // given
            UUID newsId = Constant.NEWS_UUID_FAIL;

            // when
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    "/news/" + newsId + "/exists",
                    HttpMethod.GET,
                    null,
                    String.class);


            Boolean actualValue = objectMapper.readValue(
                    responseEntity.getBody(),
                    new TypeReference<>() {
                    });

            // then
            assertFalse(actualValue);
        }
    }

}
