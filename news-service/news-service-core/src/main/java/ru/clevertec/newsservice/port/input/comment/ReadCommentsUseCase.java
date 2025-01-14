package ru.clevertec.newsservice.port.input.comment;


import ru.clevertec.newsservice.port.input.comment.command.CommentUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentsUseCaseResult;

import java.util.UUID;

public interface ReadCommentsUseCase {
    CommentsUseCaseResult readComments(UUID newsId, int page, int size);

    CommentUseCase readComment(UUID newsId, UUID commentId);
}
