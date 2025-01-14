package ru.clevertec.commentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.mapper.CommentMapper;
import ru.clevertec.commentservice.port.input.CreateCommentUseCase;
import ru.clevertec.commentservice.port.input.command.CommentCreateCommand;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;
import ru.clevertec.commentservice.port.output.NewsClientPort;
import ru.clevertec.commentservice.port.output.WriteCommentPort;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;

@Component
@RequiredArgsConstructor
public class CreateCommentService implements CreateCommentUseCase {

    public static final String NEWS_NOT_FOUND = "News with id '%s' not found";

    private final WriteCommentPort commentAdapter;
    private final CommentMapper commentMapper;

    private final NewsClientPort newsClientAdapter;

    @Override
    public CommentUseCaseResult createComment(CommentCreateCommand createCommand) {
        Boolean existsNews = newsClientAdapter.existsNews(createCommand.newsId());
        if (existsNews == null || !existsNews) {
            throw EntityNotFoundException.getInstance(
                    String.format(NEWS_NOT_FOUND, createCommand.newsId())
            );
        }

        Comment comment = commentMapper.commandToDomain(createCommand);
        Comment savedComment = commentAdapter.createComment(comment);
        return commentMapper.domainToUseCase(savedComment);
    }
}
