package ru.clevertec.commentservice.cache.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.entity.CommentEntity;
import ru.clevertec.commentservice.util.Constant;
import ru.clevertec.commentservice.util.TestDataComment;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LFUCacheTest {

    private UUID COMMENT_ID = UUID.fromString("45f1ab38-8678-4271-a0b2-82f5c4de549c");
    private UUID COMMENT_ID_SECOND = UUID.fromString("cabd0405-200a-4624-b064-2bce6d604709");
    private String CHANGED_COMMENT = "Changed comment";

    private LFUCache<UUID, CommentEntity> cache;

    @BeforeEach
    void setUp() {
        cache = new LFUCache<>();
    }

    @Test
    void shouldPutCommentInEmptyCache() {
        // given
        CommentEntity comment = TestDataComment.getCommentEntitySaved();

        // when
        cache.put(comment.getId(), comment);

        // then
        assertNotNull(cache);
        assertTrue(cache.get(comment.getId()).isPresent());
    }

    @Test
    void shouldPutCommentInNotEmptyCache() {
        // given
        CommentEntity firstComment = TestDataComment.getCommentEntitySaved();
        CommentEntity secondComment = TestDataComment.getCommentEntitySaved();
        firstComment.setId(COMMENT_ID);
        secondComment.setId(COMMENT_ID_SECOND);

        // when
        cache.put(firstComment.getId(), firstComment);
        cache.put(secondComment.getId(), secondComment);

        // then
        assertNotNull(cache);
        assertTrue(cache.get(secondComment.getId()).isPresent());
    }

    @Test
    void shouldPutCommentInCache_whenCommentIsExists() {
        // given
        CommentEntity firstComment = TestDataComment.getCommentEntitySaved();
        CommentEntity secondComment = TestDataComment.getCommentEntitySaved();
        firstComment.setId(COMMENT_ID);
        secondComment.setId(COMMENT_ID_SECOND);

        // when
        secondComment.setText(CHANGED_COMMENT);
        cache.put(secondComment.getId(), secondComment);

        // then
        assertNotNull(cache);
        assertTrue(cache.get(secondComment.getId()).isPresent());
        assertEquals(CHANGED_COMMENT, cache.get(secondComment.getId()).get().getText());
    }

    @Test
    void shouldPutCommentInNotEmptyCache_whenCapacityIsMax() {
        // given
        TestDataComment.fillLfuCache(cache);
        CommentEntity comment = TestDataComment.getCommentEntityForCreate();

        // when
        cache.put(comment.getId(), comment);

        // then
        assertNotNull(cache);
        assertTrue(cache.get(comment.getId()).isPresent());
    }

    @Test
    void shouldGetCommentInCache() {
        // given
        CommentEntity comment = TestDataComment.getCommentEntityForCreate();
        cache.put(comment.getId(), comment);

        // when
        Optional<CommentEntity> actualComment = cache.get(comment.getId());

        // then
        assertTrue(actualComment.isPresent());
    }

    @Test
    void shouldNotGetCommentInCache_whenCommentNotFound() {
        // given

        // when
        Optional<CommentEntity> actualComment = cache.get(Constant.COMMENT_UUID);

        // then
        assertTrue(actualComment.isEmpty());
    }

    @Test
    void shouldDeleteCommentInCache() {
        // given
        CommentEntity comment = TestDataComment.getCommentEntityForCreate();
        cache.put(comment.getId(), comment);

        // when
        cache.delete(comment.getId());
        Optional<CommentEntity> actualComment = cache.get(COMMENT_ID);

        // then
        assertTrue(actualComment.isEmpty());
    }

    @Test
    void shouldNotDeleteCommentInCache_whenCommentNotFound() {
        // given
        // when
        // then
        Assertions.assertDoesNotThrow(() -> cache.delete(COMMENT_ID));

    }
}