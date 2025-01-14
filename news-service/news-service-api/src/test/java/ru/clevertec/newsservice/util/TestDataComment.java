package ru.clevertec.newsservice.util;

import lombok.experimental.UtilityClass;
import ru.clevertec.newsservice.adapter.input.web.comment.dto.CommentResponse;
import ru.clevertec.newsservice.adapter.input.web.comment.dto.CommentsPageResponse;
import ru.clevertec.newsservice.domain.comment.Comment;
import ru.clevertec.newsservice.domain.comment.CommentsPage;
import ru.clevertec.newsservice.port.input.comment.command.CommentUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentsUseCaseResult;

import java.util.List;

@UtilityClass
public class TestDataComment {


    public static CommentUseCase getCommentUseCase() {
        return new CommentUseCase(
                Constant.COMMENT_UUID,
                Constant.COMMENT_AUTHOR,
                Constant.COMMENT_NEWS_UUID,
                Constant.COMMENT_TEXT
        );
    }

    public static CommentResponse getCommentResponse() {
        return new CommentResponse(
                Constant.COMMENT_UUID,
                Constant.COMMENT_AUTHOR,
                Constant.COMMENT_NEWS_UUID,
                Constant.COMMENT_TEXT
        );
    }

    public static Comment getComment() {
        return new Comment(
                Constant.COMMENT_UUID,
                Constant.COMMENT_AUTHOR,
                Constant.COMMENT_NEWS_UUID,
                Constant.COMMENT_TEXT
        );
    }

    public static CommentsUseCaseResult getCommentUseCaseResult() {
        return new CommentsUseCaseResult(
                Constant.PAGE_NUMBER,
                Constant.PAGE_SIZE,
                1,
                1,
                List.of(
                        getCommentUseCase()
                )
        );
    }

    public static CommentsPageResponse getCommentsPageResponse() {
        return new CommentsPageResponse(
                Constant.PAGE_NUMBER,
                Constant.PAGE_SIZE,
                1,
                1,
                List.of(
                        getCommentResponse()
                )
        );
    }

    public static CommentsPage getCommentsPage() {
        return new CommentsPage(
                Constant.PAGE_NUMBER,
                Constant.PAGE_SIZE,
                1,
                1,
                List.of(
                        getComment()
                )
        );
    }
}