package ru.clevertec.commentservice.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.mapper.CommentMapper;
import ru.clevertec.commentservice.port.input.command.CommentCreateCommand;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;
import ru.clevertec.commentservice.port.output.NewsClientPort;
import ru.clevertec.commentservice.port.output.WriteCommentPort;
import ru.clevertec.commentservice.util.TestDataComment;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCommentServiceTest {

    @Mock
    private WriteCommentPort commentAdapter;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private NewsClientPort newsClientAdapter;

    @InjectMocks
    private CreateCommentService createCommentService;

    @Test
    void shouldCreateComment() {
        // given
        CommentCreateCommand commentCommand = TestDataComment.getCommentCreateCommand();
        Comment comment = TestDataComment.getCommentForCreate();
        Comment savedComment = TestDataComment.getCommentSaved();
        CommentUseCaseResult expectedComment = TestDataComment.getCommentUseCaseResult();

        when(newsClientAdapter.existsNews(commentCommand.newsId())).thenReturn(true);
        when(commentMapper.commandToDomain(commentCommand)).thenReturn(comment);
        when(commentAdapter.createComment(comment)).thenReturn(savedComment);
        when(commentMapper.domainToUseCase(savedComment)).thenReturn(expectedComment);

        // when
        CommentUseCaseResult actualComment = createCommentService.createComment(commentCommand);

        // then
        assertAll(
                () -> assertNotNull(actualComment),
                () -> assertNotNull(actualComment.id()),
                () -> assertEquals(expectedComment.author(), actualComment.author()),
                () -> assertEquals(expectedComment.newsId(), actualComment.newsId()),
                () -> assertEquals(expectedComment.text(), actualComment.text())
        );

        verify(commentMapper, times(1)).commandToDomain(commentCommand);
        verify(commentAdapter, times(1)).createComment(comment);
        verify(commentMapper, times(1)).domainToUseCase(savedComment);
    }

    @Test
    void shouldNotCreateComment_whenNewsIsNotExists() {
        // given
        CommentCreateCommand commentCommand = TestDataComment.getCommentCreateCommand();
        Comment comment = TestDataComment.getCommentForCreate();
        Comment savedComment = TestDataComment.getCommentSaved();

        when(newsClientAdapter.existsNews(commentCommand.newsId())).thenReturn(false);

        // when

        // then
        assertThrows(EntityNotFoundException.class,
                () -> createCommentService.createComment(commentCommand));

        verify(commentMapper, times(0)).commandToDomain(commentCommand);
        verify(commentAdapter, times(0)).createComment(comment);
        verify(commentMapper, times(0)).domainToUseCase(savedComment);
    }

    @Test
    void shouldNotCreateComment_whenNewsIsExistsReturnNull() {
        // given
        CommentCreateCommand commentCommand = TestDataComment.getCommentCreateCommand();
        Comment comment = TestDataComment.getCommentForCreate();
        Comment savedComment = TestDataComment.getCommentSaved();

        when(newsClientAdapter.existsNews(commentCommand.newsId())).thenReturn(null);

        // when

        // then
        assertThrows(EntityNotFoundException.class,
                () -> createCommentService.createComment(commentCommand));

        verify(commentMapper, times(0)).commandToDomain(commentCommand);
        verify(commentAdapter, times(0)).createComment(comment);
        verify(commentMapper, times(0)).domainToUseCase(savedComment);
    }

}