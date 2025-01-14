package ru.clevertec.newsservice.adapter.output.persistence.adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;
import ru.clevertec.newsservice.adapter.output.persistence.feignclient.CommentClient;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.repository.NewsRepository;
import ru.clevertec.newsservice.domain.comment.Comment;
import ru.clevertec.newsservice.domain.comment.CommentsPage;
import ru.clevertec.newsservice.util.Constant;
import ru.clevertec.newsservice.util.TestDataComment;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadCommentAdapterTest {

    @Mock
    private NewsRepository newsRepository;
    @Mock
    private CommentClient commentClient;

    @InjectMocks
    private ReadCommentAdapter readCommentAdapter;


    @Test
    void shouldReadComment() {
        //given
        UUID newsId = Constant.NEWS_UUID;
        UUID commentId = Constant.COMMENT_UUID;
        Comment comment = TestDataComment.getComment();

        when(commentClient.readComment(commentId)).thenReturn(new ResponseEntity<>(comment, HttpStatus.OK));

        //when
        Comment actualComment = readCommentAdapter.readComment(newsId, commentId);

        //then
        assertAll(
                () -> assertNotNull(actualComment),
                () -> assertEquals(comment.id(), actualComment.id()),
                () -> assertEquals(comment.author(), actualComment.author()),
                () -> assertEquals(comment.newsId(), actualComment.newsId()),
                () -> assertEquals(comment.text(), actualComment.text())
        );

        verify(commentClient, times(1)).readComment(commentId);
    }

    @Test
    void shouldNotReadComment_whenCommentNotFound() {
        //given
        UUID newsId = Constant.NEWS_UUID;
        UUID commentId = Constant.COMMENT_UUID;

        when(commentClient.readComment(commentId)).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        //when, then
        assertThrows(EntityNotFoundException.class, () -> readCommentAdapter.readComment(newsId, commentId));

        verify(commentClient, times(1)).readComment(commentId);
    }

    @Test
    void shouldReadCommentsPage() {
        //given
        UUID newsId = Constant.NEWS_UUID;
        int size = Constant.PAGE_SIZE;
        int page = Constant.PAGE_NUMBER;
        CommentsPage expectedComments = TestDataComment.getCommentsPage();

        when(newsRepository.existsById(newsId)).thenReturn(true);
        when(commentClient.readComments(newsId, page, size)).thenReturn(new ResponseEntity<>(expectedComments, HttpStatus.OK));

        //when
        CommentsPage actualNewsPage = readCommentAdapter.readComments(newsId, page, size);

        //then
        assertAll(
                () -> assertNotNull(actualNewsPage),
                () -> assertEquals(expectedComments.content().size(), actualNewsPage.content().size()),
                () -> assertNotNull(actualNewsPage.content().getFirst()),
                () -> assertEquals(expectedComments.content().getFirst().id(), actualNewsPage.content().getFirst().id()),
                () -> assertEquals(expectedComments.content().getFirst().author(), actualNewsPage.content().getFirst().author()),
                () -> assertEquals(expectedComments.content().getFirst().newsId(), actualNewsPage.content().getFirst().newsId()),
                () -> assertEquals(expectedComments.content().getFirst().text(), actualNewsPage.content().getFirst().text())
        );

        verify(newsRepository, times(1)).existsById(newsId);
        verify(commentClient, times(1)).readComments(newsId, page, size);
    }

    @Test
    void shouldNotReadCommentsPage_whenNewsNotFound() {
        //given
        UUID newsId = Constant.NEWS_UUID;
        int size = Constant.PAGE_SIZE;
        int page = Constant.PAGE_NUMBER;

        when(newsRepository.existsById(newsId)).thenReturn(false);

        //when

        //then
        assertThrows(EntityNotFoundException.class, () -> readCommentAdapter.readComments(newsId, page, size));

        verify(newsRepository, times(1)).existsById(newsId);
    }

}