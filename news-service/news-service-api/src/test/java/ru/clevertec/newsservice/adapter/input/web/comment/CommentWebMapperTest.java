package ru.clevertec.newsservice.adapter.input.web.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.newsservice.adapter.input.web.comment.dto.CommentResponse;
import ru.clevertec.newsservice.adapter.input.web.comment.dto.CommentsPageResponse;
import ru.clevertec.newsservice.port.input.comment.command.CommentUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentsUseCaseResult;
import ru.clevertec.newsservice.util.TestDataComment;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CommentWebMapperTest {

    @InjectMocks
    private CommentWebMapper commentWebMapper = new CommentWebMapperImpl();

    @Test
    void shouldConvertCommentUseCaseToCommentPageResponse() {
        //given
        CommentUseCase expectedComment = TestDataComment.getCommentUseCase();

        //when
        CommentResponse actualComment = commentWebMapper.useCaseToCommentDto(expectedComment);

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
        CommentsUseCaseResult expectedComment = TestDataComment.getCommentUseCaseResult();

        //when
        CommentsPageResponse actualComment = commentWebMapper.useCaseToDto(expectedComment);

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