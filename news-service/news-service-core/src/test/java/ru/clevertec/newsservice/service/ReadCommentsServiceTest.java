package ru.clevertec.newsservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.newsservice.domain.comment.Comment;
import ru.clevertec.newsservice.domain.comment.CommentsPage;
import ru.clevertec.newsservice.mapper.CommentMapper;
import ru.clevertec.newsservice.port.input.comment.command.CommentUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentsUseCaseResult;
import ru.clevertec.newsservice.port.output.ReadCommentPort;
import ru.clevertec.newsservice.util.Constant;
import ru.clevertec.newsservice.util.TestDataComment;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadCommentsServiceTest {

    @Mock
    private ReadCommentPort commentAdapter;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private ReadCommentsService readCommentsService;

    @Test
    void shouldReadComment() {
        //given
        UUID newsId = Constant.NEWS_UUID;
        UUID commentId = Constant.COMMENT_UUID;
        Comment comment = TestDataComment.getComment();
        CommentUseCase expectedComment = TestDataComment.getCommentUseCase();

        when(commentAdapter.readComment(newsId, commentId)).thenReturn(comment);
        when(commentMapper.commentToUseCase(comment)).thenReturn(expectedComment);

        //when
        CommentUseCase actualComment = readCommentsService.readComment(newsId, commentId);

        //then
        assertAll(
                () -> assertNotNull(actualComment),
                () -> assertEquals(expectedComment.id(), actualComment.id()),
                () -> assertEquals(expectedComment.author(), actualComment.author()),
                () -> assertEquals(expectedComment.newsId(), actualComment.newsId()),
                () -> assertEquals(expectedComment.text(), actualComment.text())
        );

        verify(commentAdapter, times(1)).readComment(newsId, commentId);
        verify(commentMapper, times(1)).commentToUseCase(comment);
    }


    @Test
    void shouldReadNewsPage() {
        //given
        int page = Constant.PAGE_NUMBER;
        int size = Constant.PAGE_SIZE;
        UUID newsId = Constant.NEWS_UUID;
        CommentsPage comments = TestDataComment.getCommentsPage();
        CommentsUseCaseResult expectedComments = TestDataComment.getCommentsUseCaseResult();


        when(commentAdapter.readComments(newsId, page, size)).thenReturn(comments);
        when(commentMapper.domainToUseCase(comments)).thenReturn(expectedComments);

        //when
        CommentsUseCaseResult actualComments = readCommentsService.readComments(newsId, page, size);

        //then
        assertAll(
                () -> assertNotNull(actualComments),
                () -> assertEquals(expectedComments.content().size(), actualComments.content().size()),
                () -> assertNotNull(expectedComments.content().getFirst()),
                () -> assertEquals(expectedComments.content().getFirst().id(), actualComments.content().getFirst().id()),
                () -> assertEquals(expectedComments.content().getFirst().author(), actualComments.content().getFirst().author()),
                () -> assertEquals(expectedComments.content().getFirst().newsId(), actualComments.content().getFirst().newsId()),
                () -> assertEquals(expectedComments.content().getFirst().text(), actualComments.content().getFirst().text())
        );

        verify(commentAdapter, times(1)).readComments(newsId, page, size);
        verify(commentMapper, times(1)).domainToUseCase(comments);
    }


}