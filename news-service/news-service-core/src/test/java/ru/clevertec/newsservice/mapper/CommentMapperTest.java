package ru.clevertec.newsservice.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.newsservice.domain.comment.Comment;
import ru.clevertec.newsservice.domain.comment.CommentsPage;
import ru.clevertec.newsservice.port.input.comment.command.CommentUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentsUseCaseResult;
import ru.clevertec.newsservice.util.TestDataComment;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    private CommentMapper commentMapper = new CommentMapperImpl();

    @Test
    void shouldConvertCommentUseCaseToCommentPageResponse() {
        //given
        Comment expectedComment = TestDataComment.getComment();

        //when
        CommentUseCase actualComment = commentMapper.commentToUseCase(expectedComment);

        //then
        assertAll(
                () -> assertEquals(expectedComment.id(), actualComment.id()),
                () -> assertEquals(expectedComment.author(), actualComment.author()),
                () -> assertEquals(expectedComment.newsId(), actualComment.newsId()),
                () -> assertEquals(expectedComment.text(), actualComment.text())
        );

    }

    @Test
    void shouldConvertCommentsUseCaseResultToCommentPageResponse() {
        //given
        CommentsPage expectedComment = TestDataComment.getCommentsPage();

        //when
        CommentsUseCaseResult actualComment = commentMapper.domainToUseCase(expectedComment);

        //then
        assertAll(
                () -> assertEquals(expectedComment.number(), actualComment.number()),
                () -> assertEquals(expectedComment.size(), actualComment.size()),
                () -> assertEquals(expectedComment.totalPages(), actualComment.totalPages()),
                () -> assertEquals(expectedComment.totalElements(), actualComment.totalElements()),
                () -> assertEquals(expectedComment.content().getFirst().id(), actualComment.content().getFirst().id()),
                () -> assertEquals(expectedComment.content().getFirst().author(), actualComment.content().getFirst().author()),
                () -> assertEquals(expectedComment.content().getFirst().newsId(), actualComment.content().getFirst().newsId()),
                () -> assertEquals(expectedComment.content().getFirst().text(), actualComment.content().getFirst().text())
        );

    }

}