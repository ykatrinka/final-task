package ru.clevertec.commentservice.port.input;

import org.springframework.stereotype.Service;
import ru.clevertec.commentservice.port.input.command.CommentCreateCommand;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;

@Service
public interface CreateCommentUseCase {
    CommentUseCaseResult createComment(CommentCreateCommand createCommand);
}

