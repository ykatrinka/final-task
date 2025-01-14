package ru.clevertec.commentservice.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentCreateDto;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentPageableDto;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentResponse;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentUpdateDto;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.entity.CommentEntity;
import ru.clevertec.commentservice.cache.impl.LFUCache;
import ru.clevertec.commentservice.cache.impl.LRUCache;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.domain.CommentPageable;
import ru.clevertec.commentservice.port.input.command.CommentCreateCommand;
import ru.clevertec.commentservice.port.input.command.CommentPageUseCaseResult;
import ru.clevertec.commentservice.port.input.command.CommentUpdateCommand;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class TestDataComment {

    /**
     * FOR CREATE COMMENT
     */

    public static CommentEntity getCommentEntityForCreate() {
        return CommentEntity.builder()
                .author(Constant.COMMENT_AUTHOR)
                .newsId(Constant.NEWS_UUID)
                .text(Constant.COMMENT_TEXT)
                .build();
    }

    public static CommentEntity getCommentEntitySaved() {
        return CommentEntity.builder()
                .id(Constant.COMMENT_NEW_UUID)
                .author(Constant.COMMENT_AUTHOR)
                .newsId(Constant.NEWS_UUID)
                .text(Constant.COMMENT_TEXT)
                .build();
    }

    public static CommentCreateDto getCommentCreateDto() {
        return new CommentCreateDto(
                Constant.COMMENT_AUTHOR,
                Constant.NEWS_UUID,
                Constant.COMMENT_TEXT
        );
    }

    public static Comment getCommentForCreate() {
        Comment comment = new Comment();
        comment.setAuthor(Constant.COMMENT_AUTHOR);
        comment.setNewsId(Constant.NEWS_UUID);
        comment.setText(Constant.COMMENT_TEXT);
        return comment;
    }

    public static Comment getCommentSaved() {
        Comment comment = new Comment();
        comment.setId(Constant.COMMENT_NEW_UUID);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAuthor(Constant.COMMENT_AUTHOR);
        comment.setNewsId(Constant.NEWS_UUID);
        comment.setText(Constant.COMMENT_TEXT);
        return comment;
    }

    public static CommentCreateCommand getCommentCreateCommand() {
        return new CommentCreateCommand(
                Constant.COMMENT_AUTHOR,
                Constant.NEWS_UUID,
                Constant.COMMENT_TEXT
        );
    }

    /**
     * READ COMMENT
     */
    public static CommentEntity getCommentEntityForRead() {
        return CommentEntity.builder()
                .id(Constant.COMMENT_UUID)
                .author(Constant.COMMENT_AUTHOR)
                .newsId(Constant.NEWS_UUID)
                .text(Constant.COMMENT_TEXT)
                .build();
    }

    public static Comment getCommentForRead() {
        Comment comment = new Comment();
        comment.setId(Constant.COMMENT_UUID);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAuthor(Constant.COMMENT_AUTHOR);
        comment.setNewsId(Constant.NEWS_UUID);
        comment.setText(Constant.COMMENT_TEXT);
        return comment;
    }

    /**
     * COMMENT USE CASE RESULT
     */
    public static CommentUseCaseResult getCommentUseCaseResultForCreate() {
        return new CommentUseCaseResult(
                Constant.COMMENT_NEW_UUID,
                Constant.COMMENT_AUTHOR,
                Constant.NEWS_UUID,
                Constant.COMMENT_TEXT
        );
    }

    public static CommentUseCaseResult getCommentUseCaseResultForRead() {
        return new CommentUseCaseResult(
                Constant.COMMENT_UUID,
                Constant.COMMENT_AUTHOR,
                Constant.NEWS_UUID,
                Constant.COMMENT_TEXT
        );
    }

    public static CommentUseCaseResult getCommentUseCaseResultForUpdate() {
        return new CommentUseCaseResult(
                Constant.COMMENT_UUID,
                Constant.COMMENT_AUTHOR,
                Constant.NEWS_UUID,
                Constant.COMMENT_TEXT_UPDATE
        );
    }

    public static CommentPageUseCaseResult getCommentPageUseCaseResult() {
        return new CommentPageUseCaseResult(
                Constant.PAGE_NUMBER,
                Constant.PAGE_SIZE,
                1,
                1,
                List.of(
                        getCommentUseCaseResultForRead()
                )
        );
    }

    /**
     * UPDATE COMMENT
     */
    public static CommentEntity getCommentEntityForUpdate() {
        return CommentEntity.builder()
                .id(Constant.COMMENT_UUID)
                .author(Constant.COMMENT_AUTHOR)
                .newsId(Constant.NEWS_UUID)
                .text(Constant.COMMENT_TEXT_UPDATE)
                .build();
    }

    public static CommentEntity getCommentEntityUpdated() {
        return CommentEntity.builder()
                .id(Constant.COMMENT_UUID)
                .author(Constant.COMMENT_AUTHOR)
                .newsId(Constant.NEWS_UUID)
                .text(Constant.COMMENT_TEXT_UPDATE)
                .build();
    }

    public static CommentUpdateDto getCommentUpdateDto() {
        return new CommentUpdateDto(
                Constant.COMMENT_AUTHOR,
                Constant.NEWS_UUID,
                Constant.COMMENT_TEXT_UPDATE
        );
    }

    public static CommentUpdateCommand getCommentUpdateCommand() {
        return new CommentUpdateCommand(
                Constant.COMMENT_AUTHOR,
                Constant.NEWS_UUID,
                Constant.COMMENT_TEXT_UPDATE
        );
    }

    public static Comment getCommentForUpdate() {
        Comment comment = new Comment();
        comment.setId(Constant.COMMENT_UUID);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAuthor(Constant.COMMENT_AUTHOR);
        comment.setNewsId(Constant.NEWS_UUID);
        comment.setText(Constant.COMMENT_TEXT_UPDATE);
        return comment;
    }

    public static Comment getCommentForUpdateFail() {
        Comment comment = new Comment();
        comment.setId(Constant.COMMENT_UUID_FAIL);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAuthor(Constant.COMMENT_AUTHOR);
        comment.setNewsId(Constant.NEWS_UUID);
        comment.setText(Constant.COMMENT_TEXT_UPDATE);
        return comment;
    }


    public static Comment getCommentUpdated() {
        Comment comment = new Comment();
        comment.setId(Constant.COMMENT_UUID);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAuthor(Constant.COMMENT_AUTHOR);
        comment.setNewsId(Constant.NEWS_UUID);
        comment.setText(Constant.COMMENT_TEXT_UPDATE);
        return comment;
    }

    /**
     * PAGEABLE
     */
    public static PageRequest getPageRequest(int page, int size) {
        return PageRequest.of(page, size);
    }

    public static Page<CommentEntity> getPageCommentEntity() {
        return new PageImpl<>(List.of(getCommentEntityForRead()),
                getPageRequest(0, 10), 1);
    }

    public static CommentPageable getCommentPageable() {
        return CommentPageable.builder()
                .number(Constant.PAGE_NUMBER)
                .size(Constant.PAGE_SIZE)
                .totalPages(1)
                .totalElements(1)
                .content(List.of(getCommentForRead()))
                .build();

    }

    /**
     * COMMENT RESPONSE
     */
    public static CommentResponse getCommentResponseForCreate() {
        return new CommentResponse(
                Constant.COMMENT_NEW_UUID,
                Constant.COMMENT_AUTHOR,
                Constant.NEWS_UUID,
                Constant.COMMENT_TEXT
        );
    }

    public static CommentResponse getCommentResponseForRead() {
        return new CommentResponse(
                Constant.COMMENT_UUID,
                Constant.COMMENT_AUTHOR,
                Constant.NEWS_UUID,
                Constant.COMMENT_TEXT
        );
    }

    public static CommentResponse getCommentResponseForUpdate() {
        return new CommentResponse(
                Constant.COMMENT_UUID,
                Constant.COMMENT_AUTHOR,
                Constant.NEWS_UUID,
                Constant.COMMENT_TEXT_UPDATE
        );
    }

    public static CommentPageableDto getCommentPageableDto() {
        return new CommentPageableDto(
                Constant.PAGE_NUMBER,
                Constant.PAGE_SIZE,
                1,
                1,
                List.of(
                        getCommentResponseForRead()
                )
        );
    }

    /**
     * CACHE
     */

    public static void fillLfuCache(LFUCache<UUID, CommentEntity> cache) {
        for (int i = 0; i < 10; i++) {
            CommentEntity comment = getCommentEntityForCreate();
            comment.setId(UUID.randomUUID());
            cache.put(comment.getId(), comment);
        }
    }

    public static void fillLruCache(LRUCache<UUID, CommentEntity> cache) {
        for (int i = 0; i < 10; i++) {
            CommentEntity comment = getCommentEntityForCreate();
            comment.setId(UUID.randomUUID());
            cache.put(comment.getId(), comment);
        }
    }
}
