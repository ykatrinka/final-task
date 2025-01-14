package ru.clevertec.commentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.mapper.CommentMapper;
import ru.clevertec.commentservice.port.input.UpdateCommentUseCase;
import ru.clevertec.commentservice.port.input.command.CommentUpdateCommand;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;
import ru.clevertec.commentservice.port.output.NewsClientPort;
import ru.clevertec.commentservice.port.output.WriteCommentPort;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateCommentService implements UpdateCommentUseCase {

    public static final String NEWS_NOT_FOUND = "News with id '%s' not found";

    private final WriteCommentPort commentAdapter;
    private final CommentMapper commentMapper;

    private final NewsClientPort newsClientAdapter;

    @Override
    public CommentUseCaseResult updateComment(UUID commentId, CommentUpdateCommand updateCommand) {
        Boolean existsNews = newsClientAdapter.existsNews(updateCommand.newsId());
        if (existsNews == null || !existsNews) {
            throw EntityNotFoundException.getInstance(
                    String.format(NEWS_NOT_FOUND, updateCommand.newsId())
            );
        }

        Comment comment = commentMapper.commandToDomain(updateCommand);
        comment.setId(commentId);
        Comment updatedComment = commentAdapter.updateComment(comment);
        return commentMapper.domainToUseCase(updatedComment);
    }
}
