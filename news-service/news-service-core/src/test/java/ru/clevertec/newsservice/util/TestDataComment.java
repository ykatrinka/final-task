package ru.clevertec.newsservice.util;

import lombok.experimental.UtilityClass;
import ru.clevertec.newsservice.domain.comment.Comment;
import ru.clevertec.newsservice.domain.comment.CommentsPage;
import ru.clevertec.newsservice.port.input.comment.command.CommentUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentsUseCaseResult;

import java.util.List;

@UtilityClass
public class TestDataComment {

    public static Comment getComment() {
        return new Comment(
                Constant.COMMENT_UUID,
                Constant.COMMENT_AUTHOR,
                Constant.COMMENT_NEWS_UUID,
                Constant.COMMENT_TEXT
        );
    }

    public static CommentUseCase getCommentUseCase() {
        return new CommentUseCase(
                Constant.COMMENT_UUID,
                Constant.COMMENT_AUTHOR,
                Constant.COMMENT_NEWS_UUID,
                Constant.COMMENT_TEXT
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

    public static CommentsUseCaseResult getCommentsUseCaseResult() {
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
}
