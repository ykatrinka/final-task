package ru.clevertec.newsservice.adapter.output.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;
import ru.clevertec.newsservice.adapter.output.persistence.feignclient.CommentClient;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.repository.NewsRepository;
import ru.clevertec.newsservice.domain.comment.Comment;
import ru.clevertec.newsservice.domain.comment.CommentsPage;
import ru.clevertec.newsservice.port.output.ReadCommentPort;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReadCommentAdapter implements ReadCommentPort {

    public static final String NEWS_NOT_FOUND = "News with id %s not found";
    public static final String COMMENT_NOT_FOUND = "Comment with id %s not found or use another news";

    private final NewsRepository newsRepository;
    private final CommentClient commentClient;

    @Override
    public CommentsPage readComments(UUID newsId, int page, int size) {
        if (!newsRepository.existsById(newsId)) {
            throw EntityNotFoundException.getInstance(
                    String.format(NEWS_NOT_FOUND, newsId)
            );
        }
        return commentClient.readComments(newsId, page, size).getBody();
    }

    @Override
    public Comment readComment(UUID newsId, UUID commentId) {
        Comment comment = commentClient.readComment(commentId).getBody();
        if (comment == null || !newsId.equals(comment.newsId())) {
            throw EntityNotFoundException.getInstance(
                    String.format(COMMENT_NOT_FOUND, newsId)
            );
        }
        return comment;
    }
}
