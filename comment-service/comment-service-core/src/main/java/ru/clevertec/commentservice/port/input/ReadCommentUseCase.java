package ru.clevertec.commentservice.port.input;

import ru.clevertec.commentservice.port.input.command.CommentPageUseCaseResult;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;

import java.util.List;
import java.util.UUID;

public interface ReadCommentUseCase {
    CommentUseCaseResult readComment(UUID commentId);

    CommentPageUseCaseResult readCommentPage(int page, int size);

    CommentPageUseCaseResult readCommentsByNews(UUID newsId, int page, int size);

    List<CommentUseCaseResult> searchComment(String text, List<String> fields, int limit);
}
