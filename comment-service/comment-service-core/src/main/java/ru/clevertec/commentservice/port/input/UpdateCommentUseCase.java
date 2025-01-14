package ru.clevertec.commentservice.port.input;

import ru.clevertec.commentservice.port.input.command.CommentUpdateCommand;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;

import java.util.UUID;

public interface UpdateCommentUseCase {
    CommentUseCaseResult updateComment(UUID commentId, CommentUpdateCommand updateCommand);
}
