package ru.clevertec.commentservice.adapter.input.web.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentCreateDto;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentPageableDto;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentResponse;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentUpdateDto;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.entity.CommentEntity;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.domain.CommentPageable;
import ru.clevertec.commentservice.port.input.command.CommentCreateCommand;
import ru.clevertec.commentservice.port.input.command.CommentPageUseCaseResult;
import ru.clevertec.commentservice.port.input.command.CommentUpdateCommand;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;
import ru.clevertec.commentservice.util.TestDataComment;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CommentWebMapperTest {

    @InjectMocks
    private CommentWebMapper commentWebMapper = new CommentWebMapperImpl();

    @Test
    void shouldConvertCommentCreateDtoToCommentCreateCommand() {
        //given
        CommentCreateDto expectedComment = TestDataComment.getCommentCreateDto();

        //when
        CommentCreateCommand actualComment = commentWebMapper.dtoToCommand(expectedComment);

        //then
        assertAll(
                () -> assertEquals(expectedComment.author(), actualComment.author()),
                () -> assertEquals(expectedComment.newsId(), actualComment.newsId()),
                () -> assertEquals(expectedComment.text(), actualComment.text())
        );
    }

    @Test
    void shouldConvertCommentUpdateDtoToCommentUpdateCommand() {
        //given
        CommentUpdateDto expectedComment = TestDataComment.getCommentUpdateDto();

        //when
        CommentUpdateCommand actualComment = commentWebMapper.dtoToCommand(expectedComment);

        //then
        assertAll(
                () -> assertEquals(expectedComment.author(), actualComment.author()),
                () -> assertEquals(expectedComment.newsId(), actualComment.newsId()),
                () -> assertEquals(expectedComment.text(), actualComment.text())
        );
    }


    @Test
    void shouldConvertCommentToNewsEntity() {
        //given
        Comment expectedComment = TestDataComment.getCommentForRead();

        //when
        CommentEntity actualComment = commentWebMapper.domainToEntity(expectedComment);

        //then
        assertAll(
                () -> assertEquals(expectedComment.getId(), actualComment.getId()),
                () -> assertEquals(expectedComment.getCreatedAt(), actualComment.getCreatedAt()),
                () -> assertEquals(expectedComment.getAuthor(), actualComment.getAuthor()),
                () -> assertEquals(expectedComment.getNewsId(), actualComment.getNewsId()),
                () -> assertEquals(expectedComment.getText(), actualComment.getText())

        );
    }

    @Test
    void shouldConvertCommentEntityToComment() {
        //given
        CommentEntity expectedComment = TestDataComment.getCommentEntityForRead();

        //when
        Comment actualComment = commentWebMapper.entityToDomain(expectedComment);

        //then
        assertAll(
                () -> assertEquals(expectedComment.getId(), actualComment.getId()),
                () -> assertEquals(expectedComment.getCreatedAt(), actualComment.getCreatedAt()),
                () -> assertEquals(expectedComment.getAuthor(), actualComment.getAuthor()),
                () -> assertEquals(expectedComment.getNewsId(), actualComment.getNewsId()),
                () -> assertEquals(expectedComment.getText(), actualComment.getText())
        );
    }

    @Test
    void shouldConvertCommentUseCaseResultToCommentResponse() {
        //given
        CommentUseCaseResult expectedComment = TestDataComment.getCommentUseCaseResultForRead();

        //when
        CommentResponse actualComment = commentWebMapper.useCaseToDto(expectedComment);

        //then
        assertAll(
                () -> assertEquals(expectedComment.id(), actualComment.id()),
                () -> assertEquals(expectedComment.author(), actualComment.author()),
                () -> assertEquals(expectedComment.newsId(), actualComment.newsId()),
                () -> assertEquals(expectedComment.text(), actualComment.text())
        );
    }

    @Test
    void shouldConvertCommentPageUseCaseResultToCommentPageableDto() {
        //given
        CommentPageUseCaseResult expectedCommentPage = TestDataComment.getCommentPageUseCaseResult();

        //when
        CommentPageableDto actualCommentPage = commentWebMapper.useCaseToDto(expectedCommentPage);

        //then
        assertAll(
                () -> assertEquals(expectedCommentPage.number(), actualCommentPage.number()),
                () -> assertEquals(expectedCommentPage.size(), actualCommentPage.size()),
                () -> assertEquals(expectedCommentPage.totalPages(), actualCommentPage.totalPages()),
                () -> assertEquals(expectedCommentPage.totalElements(), actualCommentPage.totalElements()),
                () -> assertEquals(expectedCommentPage.content().size(), actualCommentPage.content().size()),
                () -> assertEquals(expectedCommentPage.content().getFirst().id(), actualCommentPage.content().getFirst().id()),
                () -> assertEquals(expectedCommentPage.content().getFirst().author(), actualCommentPage.content().getFirst().author()),
                () -> assertEquals(expectedCommentPage.content().getFirst().newsId(), actualCommentPage.content().getFirst().newsId()),
                () -> assertEquals(expectedCommentPage.content().getFirst().text(), actualCommentPage.content().getFirst().text())
        );
    }

    @Test
    void shouldConvertPageCommentEntityToCommentPageable() {
        //given
        Page<CommentEntity> expectedCommentPage = TestDataComment.getPageCommentEntity();

        //when
        CommentPageable actualCommentPage = commentWebMapper.pageToDomain(expectedCommentPage);

        //then
        assertAll(
                () -> assertEquals(expectedCommentPage.getNumber(), actualCommentPage.getNumber() - 1),
                () -> assertEquals(expectedCommentPage.getSize(), actualCommentPage.getSize()),
                () -> assertEquals(expectedCommentPage.getTotalPages(), actualCommentPage.getTotalPages()),
                () -> assertEquals(expectedCommentPage.getTotalElements(), actualCommentPage.getTotalElements()),
                () -> assertEquals(expectedCommentPage.getContent().size(), actualCommentPage.getContent().size()),
                () -> assertEquals(expectedCommentPage.getContent().getFirst().getId(), actualCommentPage.getContent().getFirst().getId()),
                () -> assertEquals(expectedCommentPage.getContent().getFirst().getAuthor(), actualCommentPage.getContent().getFirst().getAuthor()),
                () -> assertEquals(expectedCommentPage.getContent().getFirst().getNewsId(), actualCommentPage.getContent().getFirst().getNewsId()),
                () -> assertEquals(expectedCommentPage.getContent().getFirst().getText(), actualCommentPage.getContent().getFirst().getText())
        );
    }


}