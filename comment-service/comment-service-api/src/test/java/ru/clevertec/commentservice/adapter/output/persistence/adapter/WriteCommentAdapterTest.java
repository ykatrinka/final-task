package ru.clevertec.commentservice.adapter.output.persistence.adapter;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.commentservice.adapter.input.web.comment.CommentWebMapper;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.entity.CommentEntity;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.repository.CommentRepository;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.util.Constant;
import ru.clevertec.commentservice.util.TestDataComment;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriteCommentAdapterTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentWebMapper commentMapper;

    @InjectMocks
    private WriteCommentAdapter writeNewsAdapter;

    @Test
    void shouldCreateComment() {
        // given
        Comment comment = TestDataComment.getCommentForCreate();
        CommentEntity commentEntity = TestDataComment.getCommentEntityForCreate();
        CommentEntity savedCommentEntity = TestDataComment.getCommentEntitySaved();
        Comment expectedComment = TestDataComment.getCommentSaved();

        when(commentMapper.domainToEntity(comment)).thenReturn(commentEntity);
        when(commentRepository.save(commentEntity)).thenReturn(savedCommentEntity);
        when(commentMapper.entityToDomain(savedCommentEntity)).thenReturn(expectedComment);

        // when
        Comment actualComment = writeNewsAdapter.createComment(comment);

        // then
        assertAll(
                () -> assertNotNull(actualComment),
                () -> assertNotNull(actualComment.getId()),
                () -> assertEquals(expectedComment.getAuthor(), actualComment.getAuthor()),
                () -> assertEquals(expectedComment.getNewsId(), actualComment.getNewsId()),
                () -> assertEquals(expectedComment.getText(), actualComment.getText())
        );

        verify(commentMapper, times(1)).domainToEntity(comment);
        verify(commentRepository, times(1)).save(commentEntity);
        verify(commentMapper, times(1)).entityToDomain(savedCommentEntity);
    }

    @Nested
    class UpdateComment {

        @Test
        void shouldUpdateComment() {
            // given
            Comment comment = TestDataComment.getCommentForUpdate();
            CommentEntity commentEntity = TestDataComment.getCommentEntityForUpdate();
            CommentEntity updatedCommentEntity = TestDataComment.getCommentEntityUpdated();
            Comment expectedComment = TestDataComment.getCommentUpdated();

            when(commentRepository.existsById(comment.getId())).thenReturn(true);
            when(commentMapper.domainToEntity(comment)).thenReturn(commentEntity);
            when(commentRepository.save(commentEntity)).thenReturn(updatedCommentEntity);
            when(commentMapper.entityToDomain(updatedCommentEntity)).thenReturn(expectedComment);

            // when
            Comment actualComment = writeNewsAdapter.updateComment(comment);

            // then
            assertAll(
                    () -> assertNotNull(actualComment),
                    () -> assertNotNull(actualComment.getId()),
                    () -> assertEquals(expectedComment.getAuthor(), actualComment.getAuthor()),
                    () -> assertEquals(expectedComment.getNewsId(), actualComment.getNewsId()),
                    () -> assertEquals(expectedComment.getText(), actualComment.getText())
            );

            verify(commentRepository, times(1)).existsById(comment.getId());
            verify(commentMapper, times(1)).domainToEntity(comment);
            verify(commentRepository, times(1)).save(commentEntity);
            verify(commentMapper, times(1)).entityToDomain(updatedCommentEntity);
        }

        @Test
        void shouldNotUpdateComment_whenCommentNotFound() {
            //given
            UUID commentId = Constant.COMMENT_UUID_FAIL;
            Comment comment = TestDataComment.getCommentForUpdateFail();

            when(commentRepository.existsById(commentId)).thenReturn(false);

            //when, then
            assertThrows(EntityNotFoundException.class, () -> writeNewsAdapter.updateComment(comment));

            verify(commentRepository, times(1)).existsById(commentId);
        }

    }

    @Test
    void shouldDeleteComment() {
        //given
        UUID commentId = Constant.COMMENT_UUID;

        //when

        //then
        writeNewsAdapter.deleteComment(commentId);
        verify(commentRepository, times(1)).deleteById(commentId);
    }


}