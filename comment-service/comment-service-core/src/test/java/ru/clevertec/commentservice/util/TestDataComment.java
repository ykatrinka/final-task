package ru.clevertec.commentservice.util;

import lombok.experimental.UtilityClass;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.domain.CommentPageable;
import ru.clevertec.commentservice.port.input.command.CommentCreateCommand;
import ru.clevertec.commentservice.port.input.command.CommentPageUseCaseResult;
import ru.clevertec.commentservice.port.input.command.CommentUpdateCommand;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class TestDataComment {
    /**
     * CREATE COMMENT
     */
    public static Comment getCommentForCreate() {
        Comment comment = new Comment();
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

    public static Comment getCommentSaved() {
        Comment comment = new Comment();
        comment.setId(Constant.COMMENT_UUID);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAuthor(Constant.COMMENT_AUTHOR);
        comment.setNewsId(Constant.NEWS_UUID);
        comment.setText(Constant.COMMENT_TEXT);
        return comment;
    }

    /**
     * UPDATE COMMENT
     */
    public static Comment getCommentForUpdate() {
        Comment comment = new Comment();
        comment.setAuthor(Constant.COMMENT_AUTHOR);
        comment.setNewsId(Constant.NEWS_UUID);
        comment.setText(Constant.COMMENT_TEXT_UPDATE);
        return comment;
    }

    public static CommentUpdateCommand getCommentUpdateCommand() {
        return new CommentUpdateCommand(
                Constant.COMMENT_AUTHOR,
                Constant.NEWS_UUID,
                Constant.COMMENT_TEXT_UPDATE
        );
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
     * COMMENT USE CASE RESULT
     */
    public static CommentUseCaseResult getCommentUseCaseResult() {
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

    /**
     * READ COMMENT
     */
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
     * PAGEABLE
     */
    public static CommentPageable getCommentPageable() {
        return CommentPageable.builder()
                .number(Constant.PAGE_NUMBER)
                .size(Constant.PAGE_SIZE)
                .totalPages(1)
                .totalElements(1)
                .content(List.of(getCommentForRead()))
                .build();

    }

    public static CommentPageUseCaseResult getCommentPageUseCaseResult() {
        return new CommentPageUseCaseResult(
                1,
                10,
                1,
                1,
                List.of(
                        getCommentUseCaseResult()
                )
        );
    }
}

