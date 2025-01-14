package ru.clevertec.commentservice.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.domain.CommentPageable;
import ru.clevertec.commentservice.port.input.command.CommentCreateCommand;
import ru.clevertec.commentservice.port.input.command.CommentPageUseCaseResult;
import ru.clevertec.commentservice.port.input.command.CommentUpdateCommand;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;
import ru.clevertec.commentservice.util.TestDataComment;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    private CommentMapper commentMapper = new CommentMapperImpl();

    @Test
    void shouldConvertCommentCreateCommandToComment() {
        //given
        CommentCreateCommand expectedComment = TestDataComment.getCommentCreateCommand();

        //when
        Comment actualComment = commentMapper.commandToDomain(expectedComment);

        //then
        assertAll(
                () -> assertNull(actualComment.getId()),
                () -> assertNull(actualComment.getCreatedAt()),
                () -> assertEquals(expectedComment.author(), actualComment.getAuthor()),
                () -> assertEquals(expectedComment.newsId(), actualComment.getNewsId()),
                () -> assertEquals(expectedComment.text(), actualComment.getText())
        );
    }

    @Test
    void shouldConvertCommentUpdateCommandToComment() {
        //given
        CommentUpdateCommand expectedComment = TestDataComment.getCommentUpdateCommand();

        //when
        Comment actualComment = commentMapper.commandToDomain(expectedComment);

        //then
        assertAll(
                () -> assertNull(actualComment.getId()),
                () -> assertNull(actualComment.getCreatedAt()),
                () -> assertEquals(expectedComment.author(), actualComment.getAuthor()),
                () -> assertEquals(expectedComment.newsId(), actualComment.getNewsId()),
                () -> assertEquals(expectedComment.text(), actualComment.getText())
        );
    }


    @Test
    void shouldConvertCommentToCommentUseCaseResult() {
        //given
        Comment expectedComment = TestDataComment.getCommentForRead();

        //when
        CommentUseCaseResult actualComment = commentMapper.domainToUseCase(expectedComment);

        //then
        assertAll(
                () -> assertEquals(expectedComment.getId(), actualComment.id()),
                () -> assertEquals(expectedComment.getAuthor(), actualComment.author()),
                () -> assertEquals(expectedComment.getNewsId(), actualComment.newsId()),
                () -> assertEquals(expectedComment.getText(), actualComment.text())
        );
    }

    @Test
    void shouldConvertCommentPageableToCommentPageUseCaseResult() {
        //given
        CommentPageable expectedCommentPage = TestDataComment.getCommentPageable();

        //when
        CommentPageUseCaseResult actualCommentPage = commentMapper.pageToUseCase(expectedCommentPage);

        //then
        assertAll(
                () -> assertNotNull(actualCommentPage),
                () -> assertEquals(expectedCommentPage.getContent().size(), actualCommentPage.content().size()),
                () -> assertNotNull(actualCommentPage.content().getFirst()),
                () -> assertEquals(expectedCommentPage.getContent().getFirst().getId(), actualCommentPage.content().getFirst().id()),
                () -> assertEquals(expectedCommentPage.getContent().getFirst().getAuthor(), actualCommentPage.content().getFirst().author()),
                () -> assertEquals(expectedCommentPage.getContent().getFirst().getNewsId(), actualCommentPage.content().getFirst().newsId()),
                () -> assertEquals(expectedCommentPage.getContent().getFirst().getText(), actualCommentPage.content().getFirst().text())
        );
    }


}