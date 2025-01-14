package ru.clevertec.commentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.domain.CommentPageable;
import ru.clevertec.commentservice.mapper.CommentMapper;
import ru.clevertec.commentservice.port.input.ReadCommentUseCase;
import ru.clevertec.commentservice.port.input.command.CommentPageUseCaseResult;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;
import ru.clevertec.commentservice.port.output.NewsClientPort;
import ru.clevertec.commentservice.port.output.ReadCommentPort;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReadCommentService implements ReadCommentUseCase {

    public static final String NEWS_NOT_FOUND = "News with id '%s' not found";

    private final ReadCommentPort commentAdapter;
    private final CommentMapper commentMapper;

    private final NewsClientPort newsClientAdapter;

    @Override
    public CommentUseCaseResult readComment(UUID commentId) {
        Comment comment = commentAdapter.readComment(commentId);
        return commentMapper.domainToUseCase(comment);
    }

    @Override
    public CommentPageUseCaseResult readCommentPage(int page, int size) {
        CommentPageable commentPageable = commentAdapter.readCommentPage(page, size);
        return commentMapper.pageToUseCase(commentPageable);
    }

    @Override
    public CommentPageUseCaseResult readCommentsByNews(UUID newsId, int page, int size) {
        Boolean existsNews = newsClientAdapter.existsNews(newsId);
        if (existsNews == null || !existsNews) {
            throw EntityNotFoundException.getInstance(
                    String.format(NEWS_NOT_FOUND, newsId)
            );
        }

        CommentPageable commentPageable = commentAdapter.readCommentsByNews(newsId, page, size);
        return commentMapper.pageToUseCase(commentPageable);
    }

    @Override
    public List<CommentUseCaseResult> searchComment(String text, List<String> fields, int limit) {
        List<Comment> comments = commentAdapter.searchComment(text, fields, limit);
        return comments.stream()
                .map(commentMapper::domainToUseCase)
                .toList();
    }
}
