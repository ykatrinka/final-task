package ru.clevertec.commentservice.adapter.input.web.comment.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentCreateDto;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentPageableDto;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentResponse;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentUpdateDto;
import ru.clevertec.commentservice.util.Constant;
import ru.clevertec.commentservice.util.TestDataComment;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
@WireMockTest(httpPort = 8081)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerIntegrationTest {

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
    void shouldCreateCommentAndReturnStatus201() throws Exception {
        // given
        CommentCreateDto expectedComment = TestDataComment.getCommentCreateDto();
        HttpEntity<CommentCreateDto> httpRequest = new HttpEntity<>(expectedComment);

        stubFor(
                WireMock.get(urlPathEqualTo("/news/" + expectedComment.newsId() + "/exists"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(objectMapper.writeValueAsString(Boolean.TRUE))
                        )
        );

        // when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments",
                HttpMethod.POST,
                httpRequest,
                String.class);

        CommentResponse actualComment = objectMapper.readValue(
                responseEntity.getBody(),
                new TypeReference<>() {
                });

        // then
        assertAll(
                () -> assertEquals(CREATED, responseEntity.getStatusCode()),
                () -> assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> assertNotNull(actualComment.id()),
                () -> assertEquals(expectedComment.author(), actualComment.author()),
                () -> assertEquals(expectedComment.newsId(), actualComment.newsId()),
                () -> assertEquals(expectedComment.text(), actualComment.text())
        );
    }

    @Test
    void shouldNotCreateComment_whenNewsIsNotExists() throws Exception {
        // given
        CommentCreateDto expectedComment = TestDataComment.getCommentCreateDto();
        HttpEntity<CommentCreateDto> httpRequest = new HttpEntity<>(expectedComment);

        stubFor(
                WireMock.get(urlPathEqualTo("/news/" + expectedComment.newsId() + "/exists"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(objectMapper.writeValueAsString(Boolean.FALSE))
                        )
        );

        // when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments",
                HttpMethod.POST,
                httpRequest,
                String.class);

        // then
        assertAll(
                () -> assertEquals(NOT_FOUND, responseEntity.getStatusCode()),
                () -> assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );
    }

    @Nested
    class ReadComment {

        @Test
        void shouldReadComment() throws Exception {
            // given
            UUID commentId = Constant.COMMENT_UUID;
            CommentResponse expectedComment = TestDataComment.getCommentResponseForRead();

            // when
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    "/comments/" + commentId,
                    HttpMethod.GET,
                    null,
                    String.class);


            CommentResponse actualComment = objectMapper.readValue(
                    responseEntity.getBody(),
                    new TypeReference<>() {
                    });

            // then
            assertAll(
                    () -> assertEquals(OK, responseEntity.getStatusCode()),
                    () -> assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                    () -> assertNotNull(actualComment.id()),
                    () -> assertEquals(expectedComment.id(), actualComment.id()),
                    () -> assertEquals(expectedComment.author(), actualComment.author()),
                    () -> assertEquals(expectedComment.newsId(), actualComment.newsId()),
                    () -> assertEquals(expectedComment.text(), actualComment.text())
            );
        }

        @Test
        void shouldNotReadComment_whenCommentNotFound() {
            // given
            UUID commentId = Constant.COMMENT_UUID_FAIL;

            // when
            ResponseEntity<String> actualEntity = restTemplate.exchange(
                    "/comments/" + commentId,
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
    class UpdateComment {

        @Test
        void shouldUpdateComment() throws Exception {
            // given
            UUID commentId = Constant.COMMENT_UUID;
            CommentUpdateDto commentUpdateDto = TestDataComment.getCommentUpdateDto();
            CommentResponse expectedComment = TestDataComment.getCommentResponseForUpdate();
            HttpEntity<CommentUpdateDto> httpRequest = new HttpEntity<>(commentUpdateDto);

            stubFor(
                    WireMock.get(urlPathEqualTo("/news/" + expectedComment.newsId() + "/exists"))
                            .willReturn(aResponse()
                                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                    .withBody(objectMapper.writeValueAsString(Boolean.TRUE))
                            )
            );

            // when
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    "/comments/" + commentId,
                    HttpMethod.PUT,
                    httpRequest,
                    String.class);

            CommentResponse actualComment = objectMapper.readValue(
                    responseEntity.getBody(),
                    new TypeReference<>() {
                    });

            // then
            assertAll(
                    () -> assertEquals(OK, responseEntity.getStatusCode()),
                    () -> assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                    () -> assertNotNull(actualComment.id()),
                    () -> assertEquals(expectedComment.id(), actualComment.id()),
                    () -> assertEquals(expectedComment.author(), actualComment.author()),
                    () -> assertEquals(expectedComment.newsId(), actualComment.newsId()),
                    () -> assertEquals(expectedComment.text(), actualComment.text())
            );
        }

        @Test
        void shouldNotUpdateComment_whenCommentNotFound() throws JsonProcessingException {
            // given
            UUID commentId = Constant.COMMENT_UUID_FAIL;
            UUID newsId = Constant.NEWS_UUID;
            CommentUpdateDto commentUpdateDto = TestDataComment.getCommentUpdateDto();
            HttpEntity<CommentUpdateDto> httpRequest = new HttpEntity<>(commentUpdateDto);

            stubFor(
                    WireMock.get(urlPathEqualTo("/news/" + newsId + "/exists"))
                            .willReturn(aResponse()
                                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                    .withBody(objectMapper.writeValueAsString(Boolean.TRUE))
                            )
            );

            // when
            ResponseEntity<String> actualEntity = restTemplate.exchange(
                    "/comments/" + commentId,
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
    void shouldNotUpdateComment_whenNewsIsNotExists() throws Exception {
        // given
        UUID commentId = Constant.COMMENT_UUID;
        CommentUpdateDto commentUpdateDto = TestDataComment.getCommentUpdateDto();
        CommentResponse expectedComment = TestDataComment.getCommentResponseForUpdate();
        HttpEntity<CommentUpdateDto> httpRequest = new HttpEntity<>(commentUpdateDto);

        stubFor(
                WireMock.get(urlPathEqualTo("/news/" + expectedComment.newsId() + "/exists"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(objectMapper.writeValueAsString(Boolean.FALSE))
                        )
        );

        // when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments/" + commentId,
                HttpMethod.PUT,
                httpRequest,
                String.class);

        // then
        assertAll(
                () -> assertEquals(NOT_FOUND, responseEntity.getStatusCode()),
                () -> assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );
    }

    @Test
    void shouldDeleteComment() {
        // given
        UUID commentId = Constant.COMMENT_UUID;

        // when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments/" + commentId,
                HttpMethod.DELETE,
                null,
                String.class);

        // then
        assertEquals(NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void shouldReadCommentPage() throws Exception {
        // given
        CommentResponse expectedComment = TestDataComment.getCommentResponseForRead();

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments",
                HttpMethod.GET,
                null,
                String.class);


        CommentPageableDto actualCommentPage = objectMapper.readValue(
                responseEntity.getBody(),
                new TypeReference<>() {
                });

        //then
        assertAll(
                () -> assertEquals(OK, responseEntity.getStatusCode()),
                () -> assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> assertNotNull(actualCommentPage),
                () -> assertEquals(4, actualCommentPage.content().size()),
                () -> assertEquals(expectedComment.id(), actualCommentPage.content().get(1).id()),
                () -> assertEquals(expectedComment.author(), actualCommentPage.content().get(1).author()),
                () -> assertEquals(expectedComment.newsId(), actualCommentPage.content().get(1).newsId()),
                () -> assertEquals(expectedComment.text(), actualCommentPage.content().get(1).text())
        );

    }

    @Test
    void shouldReadCommentsByNews() throws Exception {
        // given
        UUID newsId = Constant.NEWS_UUID;
        CommentResponse expectedComment = TestDataComment.getCommentResponseForRead();

        stubFor(
                WireMock.get(urlPathEqualTo("/news/" + expectedComment.newsId() + "/exists"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(objectMapper.writeValueAsString(Boolean.TRUE))
                        )
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments/news/" + newsId,
                HttpMethod.GET,
                null,
                String.class);


        CommentPageableDto actualCommentPage = objectMapper.readValue(
                responseEntity.getBody(),
                new TypeReference<>() {
                });

        //then
        assertAll(
                () -> assertEquals(OK, responseEntity.getStatusCode()),
                () -> assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> assertNotNull(actualCommentPage),
                () -> assertEquals(2, actualCommentPage.content().size()),
                () -> assertEquals(expectedComment.id(), actualCommentPage.content().get(1).id()),
                () -> assertEquals(expectedComment.author(), actualCommentPage.content().get(1).author()),
                () -> assertEquals(expectedComment.newsId(), actualCommentPage.content().get(1).newsId()),
                () -> assertEquals(expectedComment.text(), actualCommentPage.content().get(1).text())
        );

    }

    @Test
    void shouldNotReadCommentsByNews_whenNewsIsNotExists() throws Exception {
        // given
        UUID newsId = Constant.NEWS_UUID;
        CommentResponse expectedComment = TestDataComment.getCommentResponseForRead();

        stubFor(
                WireMock.get(urlPathEqualTo("/news/" + expectedComment.newsId() + "/exists"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(objectMapper.writeValueAsString(Boolean.FALSE))
                        )
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments/news/" + newsId,
                HttpMethod.GET,
                null,
                String.class);

        //then
        assertAll(
                () -> assertEquals(NOT_FOUND, responseEntity.getStatusCode()),
                () -> assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );

    }


}
