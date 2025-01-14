package ru.clevertec.commentservice.adapter.output.persistence.jpa.repository;

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
import ru.clevertec.commentservice.adapter.output.persistence.jpa.entity.CommentEntity;
import ru.clevertec.commentservice.util.Constant;
import ru.clevertec.commentservice.util.TestDataComment;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Container
    private static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Sql(scripts = "classpath:db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void shouldCreateComment() {
        // given
        CommentEntity expectedComment = TestDataComment.getCommentEntityForCreate();

        // when
        CommentEntity actualComment = commentRepository.save(expectedComment);
        commentRepository.flush();

        // then
        assertAll(
                () -> assertNotNull(actualComment),
                () -> assertNotNull(actualComment.getId()),
                () -> assertNotNull(actualComment.getCreatedAt()),
                () -> assertEquals(expectedComment.getAuthor(), actualComment.getAuthor()),
                () -> assertEquals(expectedComment.getNewsId(), actualComment.getNewsId()),
                () -> assertEquals(expectedComment.getText(), actualComment.getText())
        );
    }

    @Test
    @Sql(scripts = "classpath:db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReadComment() {
        // given
        CommentEntity expectedComment = TestDataComment.getCommentEntityForRead();

        // when
        CommentEntity actualComment = commentRepository.findById(expectedComment.getId()).orElse(null);

        // then
        assertAll(
                () -> assertNotNull(actualComment),
                () -> {
                    assert actualComment != null;
                    assertNotNull(actualComment.getId());
                },
                () -> assertEquals(expectedComment.getId(), actualComment.getId()),
                () -> assertEquals(expectedComment.getAuthor(), actualComment.getAuthor()),
                () -> assertEquals(expectedComment.getNewsId(), actualComment.getNewsId()),
                () -> assertEquals(expectedComment.getText(), actualComment.getText())
        );
    }

    @Test
    @Sql(scripts = "classpath:db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldUpdateComment() {
        // given
        CommentEntity expectedComment = TestDataComment.getCommentEntityForUpdate();

        // when
        CommentEntity actualComment = commentRepository.save(expectedComment);

        // then
        assertAll(
                () -> assertNotNull(actualComment),
                () -> assertEquals(expectedComment.getId(), actualComment.getId()),
                () -> assertEquals(expectedComment.getAuthor(), actualComment.getAuthor()),
                () -> assertEquals(expectedComment.getNewsId(), actualComment.getNewsId()),
                () -> assertEquals(expectedComment.getText(), actualComment.getText())
        );
    }

    @Test
    @Sql(scripts = "classpath:db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldDeleteComment() {
        // given
        UUID commentId = Constant.COMMENT_UUID;

        // when
        commentRepository.deleteById(commentId);
        CommentEntity actualComment = commentRepository.findById(commentId).orElse(null);

        // then
        Assertions.assertNull(actualComment);
    }

    @Test
    @Sql(scripts = "classpath:db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReadCommentPage() {
        // given
        CommentEntity expectedSecondComment = TestDataComment.getCommentEntityForRead();
        PageRequest pageable = TestDataComment.getPageRequest(Constant.PAGE_NUMBER - 1, Constant.PAGE_SIZE);

        // when
        Page<CommentEntity> actualCommentPage = commentRepository.findAll(pageable);
        CommentEntity actualComment = actualCommentPage.getContent().get(1);

        // then

        assertAll(
                () -> assertNotNull(actualCommentPage.getContent()),
                () -> assertEquals(1, actualCommentPage.getTotalPages()),
                () -> assertEquals(4, actualCommentPage.getTotalElements()),
                () -> assertEquals(4, actualCommentPage.getContent().size()),
                () -> assertEquals(expectedSecondComment.getId(), actualComment.getId()),
                () -> assertEquals(expectedSecondComment.getAuthor(), actualComment.getAuthor()),
                () -> assertEquals(expectedSecondComment.getNewsId(), actualComment.getNewsId()),
                () -> assertEquals(expectedSecondComment.getText(), actualComment.getText())
        );
    }

    @Test
    @Sql(scripts = "classpath:db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReadCommentsByNews() {
        // given
        UUID newsId = Constant.NEWS_UUID;
        CommentEntity expectedSecondComment = TestDataComment.getCommentEntityForRead();
        PageRequest pageable = TestDataComment.getPageRequest(Constant.PAGE_NUMBER - 1, Constant.PAGE_SIZE);

        // when
        Page<CommentEntity> actualCommentPage = commentRepository.findByNewsId(newsId, pageable);
        CommentEntity actualComment = actualCommentPage.getContent().get(1);

        // then

        assertAll(
                () -> assertNotNull(actualCommentPage.getContent()),
                () -> assertEquals(1, actualCommentPage.getTotalPages()),
                () -> assertEquals(2, actualCommentPage.getTotalElements()),
                () -> assertEquals(2, actualCommentPage.getContent().size()),
                () -> assertEquals(expectedSecondComment.getId(), actualComment.getId()),
                () -> assertEquals(expectedSecondComment.getAuthor(), actualComment.getAuthor()),
                () -> assertEquals(expectedSecondComment.getNewsId(), actualComment.getNewsId()),
                () -> assertEquals(expectedSecondComment.getText(), actualComment.getText())
        );
    }


}