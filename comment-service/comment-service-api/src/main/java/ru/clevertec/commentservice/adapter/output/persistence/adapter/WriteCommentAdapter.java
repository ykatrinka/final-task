package ru.clevertec.commentservice.adapter.output.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import ru.clevertec.commentservice.adapter.input.web.comment.CommentWebMapper;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.entity.CommentEntity;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.repository.CommentRepository;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.port.output.WriteCommentPort;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WriteCommentAdapter implements WriteCommentPort {

    public static final String COMMENT_NOT_FOUND = "Comment with id %s not found";

    private final CommentRepository commentRepository;
    private final CommentWebMapper commentMapper;

    @CachePut(value = "comment", key = "#result.id.toString()")
    @Override
    public Comment createComment(Comment comment) {
        CommentEntity commentEntity = commentMapper.domainToEntity(comment);
        CommentEntity savedComment = commentRepository.save(commentEntity);
        return commentMapper.entityToDomain(savedComment);
    }

    @CachePut(value = "comment", key = "#result.id.toString()")
    @Override
    public Comment updateComment(Comment comment) {
        if (!commentRepository.existsById(comment.getId())) {
            throw EntityNotFoundException.getInstance(
                    String.format(COMMENT_NOT_FOUND, comment.getId())
            );
        }
        CommentEntity commentEntity = commentMapper.domainToEntity(comment);
        CommentEntity updatedComment = commentRepository.save(commentEntity);
        return commentMapper.entityToDomain(updatedComment);
    }

    @CacheEvict(value = "comment", key = "#commentId.toString()")
    @Override
    public void deleteComment(UUID commentId) {
        Optional.ofNullable(commentId)
                .map(commentRepository::findById)
                .ifPresent(news -> commentRepository.deleteById(commentId));

    }
}
