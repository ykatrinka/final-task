package ru.clevertec.commentservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.mapper.CommentMapper;
import ru.clevertec.commentservice.port.input.command.CommentUpdateCommand;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;
import ru.clevertec.commentservice.port.output.NewsClientPort;
import ru.clevertec.commentservice.port.output.WriteCommentPort;
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
class UpdateCommentServiceTest {

    @Mock
    private WriteCommentPort commentAdapter;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private NewsClientPort newsClientAdapter;

    @InjectMocks
    private UpdateCommentService updateCommentService;

    @Test
    void shouldUpdateComment() {
        // given
        UUID commentId = Constant.COMMENT_UUID;
        CommentUpdateCommand commentCommand = TestDataComment.getCommentUpdateCommand();
        Comment comment = TestDataComment.getCommentForUpdate();
        Comment updatedComment = TestDataComment.getCommentUpdated();
        CommentUseCaseResult expectedComment = TestDataComment.getCommentUseCaseResultForUpdate();

        when(newsClientAdapter.existsNews(commentCommand.newsId())).thenReturn(true);
        when(commentMapper.commandToDomain(commentCommand)).thenReturn(comment);
        when(commentAdapter.updateComment(comment)).thenReturn(updatedComment);
        when(commentMapper.domainToUseCase(updatedComment)).thenReturn(expectedComment);

        // when
        CommentUseCaseResult actualComment = updateCommentService.updateComment(commentId, commentCommand);

        // then
        assertAll(
                () -> assertNotNull(actualComment),
                () -> assertNotNull(actualComment.id()),
                () -> assertEquals(expectedComment.author(), actualComment.author()),
                () -> assertEquals(expectedComment.newsId(), actualComment.newsId()),
                () -> assertEquals(expectedComment.text(), actualComment.text())
        );

        verify(commentMapper, times(1)).commandToDomain(commentCommand);
        verify(commentAdapter, times(1)).updateComment(comment);
        verify(commentMapper, times(1)).domainToUseCase(updatedComment);
    }

    @Test
    void shouldNotCreateComment_whenNewsIsNotExists() {
        // given
        UUID commentId = Constant.COMMENT_UUID;
        CommentUpdateCommand commentCommand = TestDataComment.getCommentUpdateCommand();
        Comment comment = TestDataComment.getCommentForUpdate();
        Comment updatedComment = TestDataComment.getCommentUpdated();

        when(newsClientAdapter.existsNews(commentCommand.newsId())).thenReturn(false);

        // when

        // then
        assertThrows(EntityNotFoundException.class,
                () -> updateCommentService.updateComment(commentId, commentCommand));

        verify(commentMapper, times(0)).commandToDomain(commentCommand);
        verify(commentAdapter, times(0)).updateComment(comment);
        verify(commentMapper, times(0)).domainToUseCase(updatedComment);
    }

    @Test
    void shouldNotCreateComment_whenNewsIsExistsreturnNull() {
        // given
        UUID commentId = Constant.COMMENT_UUID;
        CommentUpdateCommand commentCommand = TestDataComment.getCommentUpdateCommand();
        Comment comment = TestDataComment.getCommentForUpdate();
        Comment updatedComment = TestDataComment.getCommentUpdated();

        when(newsClientAdapter.existsNews(commentCommand.newsId())).thenReturn(null);

        // when

        // then
        assertThrows(EntityNotFoundException.class,
                () -> updateCommentService.updateComment(commentId, commentCommand));

        verify(commentMapper, times(0)).commandToDomain(commentCommand);
        verify(commentAdapter, times(0)).updateComment(comment);
        verify(commentMapper, times(0)).domainToUseCase(updatedComment);
    }
}