package ru.clevertec.commentservice.adapter.output.persistence.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.clevertec.commentservice.adapter.input.web.comment.CommentWebMapper;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.entity.CommentEntity;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.repository.CommentRepository;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.domain.CommentPageable;
import ru.clevertec.commentservice.port.output.ReadCommentPort;
import ru.clevertec.commentservice.utils.ReflectionUtil;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReadCommentAdapter implements ReadCommentPort {

    public static final String COMMENT_NOT_FOUND = "Comment with id %s not found";

    private final CommentRepository commentRepository;
    private final CommentWebMapper commentMapper;

    @PersistenceContext
    private final EntityManager entityManager;


    @Cacheable(value = "comment", key = "#commentId.toString()")
    @Override
    public Comment readComment(UUID commentId) {
        return commentRepository.findById(commentId)
                .map(commentMapper::entityToDomain)
                .orElseThrow(() -> EntityNotFoundException.getInstance(
                        String.format(COMMENT_NOT_FOUND, commentId))
                );

    }

    @Override
    public CommentPageable readCommentPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CommentEntity> commentPage = commentRepository.findAll(pageable);
        return commentMapper.pageToDomain(commentPage);

    }

    @Override
    public CommentPageable readCommentsByNews(UUID newsId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CommentEntity> commentPage = commentRepository.findByNewsId(newsId, pageable);
        return commentMapper.pageToDomain(commentPage);
    }

    @Override
    public List<Comment> searchComment(String text, List<String> fields, int limit) {
        List<String> searchableFields = ReflectionUtil.getFieldsByAnnotation(CommentEntity.class, FullTextField.class);
        List<String> fieldsToSearchBy = fields.isEmpty() ? searchableFields : fields;

        boolean containsInvalidField = fieldsToSearchBy.stream().anyMatch(f -> !searchableFields.contains(f));

        if (containsInvalidField) {
            throw new IllegalArgumentException();
        }

        SearchSession searchSession = Search.session(entityManager);

        List<CommentEntity> news = searchSession
                .search(CommentEntity.class)
                .where(f -> f.match()
                        .fields(fields.toArray(new String[0]))
                        .matching(text)
                        .fuzzy(2))
                .fetchHits(limit);

        return news.stream()
                .map(commentMapper::entityToDomain)
                .toList();
    }
}
