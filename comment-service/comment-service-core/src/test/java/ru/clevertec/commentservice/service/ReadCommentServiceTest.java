package ru.clevertec.commentservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.domain.CommentPageable;
import ru.clevertec.commentservice.mapper.CommentMapper;
import ru.clevertec.commentservice.port.input.command.CommentPageUseCaseResult;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;
import ru.clevertec.commentservice.port.output.NewsClientPort;
import ru.clevertec.commentservice.port.output.ReadCommentPort;
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
class ReadCommentServiceTest {

    @Mock
    private ReadCommentPort commentAdapter;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private NewsClientPort newsClientAdapter;

    @InjectMocks
    private ReadCommentService readCommentService;

    @Test
    void shouldReadComment() {
        //given
        UUID commentId = Constant.COMMENT_UUID;
        Comment comment = TestDataComment.getCommentForRead();
        CommentUseCaseResult expectedComment = TestDataComment.getCommentUseCaseResult();

        when(commentAdapter.readComment(commentId)).thenReturn(comment);
        when(commentMapper.domainToUseCase(comment)).thenReturn(expectedComment);

        //when
        CommentUseCaseResult actualComment = readCommentService.readComment(commentId);

        //then
        assertAll(
                () -> assertNotNull(actualComment),
                () -> assertEquals(expectedComment.id(), actualComment.id()),
                () -> assertEquals(expectedComment.author(), actualComment.author()),
                () -> assertEquals(expectedComment.newsId(), actualComment.newsId()),
                () -> assertEquals(expectedComment.text(), actualComment.text())
        );

        verify(commentAdapter, times(1)).readComment(commentId);
        verify(commentMapper, times(1)).domainToUseCase(comment);
    }


    @Test
    void shouldReadCommentPage() {
        //given
        CommentPageable commentPage = TestDataComment.getCommentPageable();
        CommentPageUseCaseResult expectedCommentPage = TestDataComment.getCommentPageUseCaseResult();

        when(commentAdapter.readCommentPage(Constant.PAGE_NUMBER, Constant.PAGE_SIZE)).thenReturn(commentPage);
        when(commentMapper.pageToUseCase(commentPage)).thenReturn(expectedCommentPage);

        //when
        CommentPageUseCaseResult actualCommentPage = readCommentService.readCommentPage(Constant.PAGE_NUMBER, Constant.PAGE_SIZE);

        //then
        assertAll(
                () -> assertNotNull(actualCommentPage),
                () -> assertEquals(expectedCommentPage.content().size(), actualCommentPage.content().size()),
                () -> assertNotNull(actualCommentPage.content().getFirst()),
                () -> assertEquals(expectedCommentPage.content().getFirst().id(), actualCommentPage.content().getFirst().id()),
                () -> assertEquals(expectedCommentPage.content().getFirst().author(), actualCommentPage.content().getFirst().author()),
                () -> assertEquals(expectedCommentPage.content().getFirst().newsId(), actualCommentPage.content().getFirst().newsId()),
                () -> assertEquals(expectedCommentPage.content().getFirst().text(), actualCommentPage.content().getFirst().text())
        );

        verify(commentAdapter, times(1)).readCommentPage(Constant.PAGE_NUMBER, Constant.PAGE_SIZE);
        verify(commentMapper, times(1)).pageToUseCase(commentPage);
    }

    @Test
    void shouldReadCommentsByNews() {
        //given
        UUID newsId = Constant.NEWS_UUID;
        CommentPageable commentPage = TestDataComment.getCommentPageable();
        CommentPageUseCaseResult expectedCommentPage = TestDataComment.getCommentPageUseCaseResult();

        when(newsClientAdapter.existsNews(newsId)).thenReturn(true);
        when(commentAdapter.readCommentsByNews(newsId, Constant.PAGE_NUMBER, Constant.PAGE_SIZE)).thenReturn(commentPage);
        when(commentMapper.pageToUseCase(commentPage)).thenReturn(expectedCommentPage);

        //when
        CommentPageUseCaseResult actualCommentPage = readCommentService.readCommentsByNews(newsId, Constant.PAGE_NUMBER, Constant.PAGE_SIZE);

        //then
        assertAll(
                () -> assertNotNull(actualCommentPage),
                () -> assertEquals(expectedCommentPage.content().size(), actualCommentPage.content().size()),
                () -> assertNotNull(actualCommentPage.content().getFirst()),
                () -> assertEquals(expectedCommentPage.content().getFirst().id(), actualCommentPage.content().getFirst().id()),
                () -> assertEquals(expectedCommentPage.content().getFirst().author(), actualCommentPage.content().getFirst().author()),
                () -> assertEquals(expectedCommentPage.content().getFirst().newsId(), actualCommentPage.content().getFirst().newsId()),
                () -> assertEquals(expectedCommentPage.content().getFirst().text(), actualCommentPage.content().getFirst().text())
        );

        verify(commentAdapter, times(1)).readCommentsByNews(newsId, Constant.PAGE_NUMBER, Constant.PAGE_SIZE);
        verify(commentMapper, times(1)).pageToUseCase(commentPage);
    }

    @Test
    void shouldNotReadCommentsByNews_whenNewsNotExists() {
        //given
        UUID newsId = Constant.NEWS_UUID;
        CommentPageable commentPage = TestDataComment.getCommentPageable();

        when(newsClientAdapter.existsNews(newsId)).thenReturn(false);

        //when

        //then
        assertThrows(EntityNotFoundException.class,
                () -> readCommentService.readCommentsByNews(newsId, Constant.PAGE_NUMBER, Constant.PAGE_SIZE));

        verify(commentAdapter, times(0)).readCommentsByNews(newsId, Constant.PAGE_NUMBER, Constant.PAGE_SIZE);
        verify(commentMapper, times(0)).pageToUseCase(commentPage);
    }

    @Test
    void shouldNotReadCommentsByNews_whenNewsExistsReturnNull() {
        //given
        UUID newsId = Constant.NEWS_UUID;
        CommentPageable commentPage = TestDataComment.getCommentPageable();

        when(newsClientAdapter.existsNews(newsId)).thenReturn(null);

        //when

        //then
        assertThrows(EntityNotFoundException.class,
                () -> readCommentService.readCommentsByNews(newsId, Constant.PAGE_NUMBER, Constant.PAGE_SIZE));

        verify(commentAdapter, times(0)).readCommentsByNews(newsId, Constant.PAGE_NUMBER, Constant.PAGE_SIZE);
        verify(commentMapper, times(0)).pageToUseCase(commentPage);
    }


}