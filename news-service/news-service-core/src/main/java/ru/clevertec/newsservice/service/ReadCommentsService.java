package ru.clevertec.newsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.newsservice.domain.comment.Comment;
import ru.clevertec.newsservice.domain.comment.CommentsPage;
import ru.clevertec.newsservice.mapper.CommentMapper;
import ru.clevertec.newsservice.port.input.comment.ReadCommentsUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentsUseCaseResult;
import ru.clevertec.newsservice.port.output.ReadCommentPort;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReadCommentsService implements ReadCommentsUseCase {

    private final ReadCommentPort commentAdapter;
    private final CommentMapper commentMapper;

    @Override
    public CommentsUseCaseResult readComments(UUID newsId, int page, int size) {
        CommentsPage comments = commentAdapter.readComments(newsId, page, size);
        return commentMapper.domainToUseCase(comments);
    }

    @Override
    public CommentUseCase readComment(UUID newsId, UUID commentId) {
        Comment comment = commentAdapter.readComment(newsId, commentId);
        return commentMapper.commentToUseCase(comment);
    }
}
