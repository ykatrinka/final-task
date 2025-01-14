package ru.clevertec.commentservice.adapter.input.web.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentCreateDto;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentPageableDto;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentResponse;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentUpdateDto;
import ru.clevertec.commentservice.port.input.CreateCommentUseCase;
import ru.clevertec.commentservice.port.input.DeleteCommentUseCase;
import ru.clevertec.commentservice.port.input.ReadCommentUseCase;
import ru.clevertec.commentservice.port.input.UpdateCommentUseCase;
import ru.clevertec.commentservice.port.input.command.CommentCreateCommand;
import ru.clevertec.commentservice.port.input.command.CommentPageUseCaseResult;
import ru.clevertec.commentservice.port.input.command.CommentUpdateCommand;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;
import ru.clevertec.commentservice.util.Constant;
import ru.clevertec.commentservice.util.TestDataComment;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;
import ru.clevertec.exceptionstarter.handler.GlobalExceptionHandling;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @MockitoBean
    private CreateCommentUseCase createCommentUseCase;
    @MockitoBean
    private ReadCommentUseCase readCommentUseCase;
    @MockitoBean
    private UpdateCommentUseCase updateCommentUseCase;
    @MockitoBean
    private DeleteCommentUseCase deleteCommentUseCase;

    @MockitoBean
    private CommentWebMapper commentWebMapper;

    @MockitoBean
    private GlobalExceptionHandling globalExceptionHandling;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateCommentAndReturnStatus201() throws Exception {
        //given
        CommentCreateDto commentCreateDto = TestDataComment.getCommentCreateDto();
        CommentCreateCommand commentCommand = TestDataComment.getCommentCreateCommand();
        CommentUseCaseResult commentUseCaseResult = TestDataComment.getCommentUseCaseResultForCreate();
        CommentResponse expectedComment = TestDataComment.getCommentResponseForCreate();

        String body = objectMapper.writeValueAsString(commentCreateDto);

        when(commentWebMapper.dtoToCommand(commentCreateDto)).thenReturn(commentCommand);
        when(createCommentUseCase.createComment(commentCommand)).thenReturn(commentUseCaseResult);
        when(commentWebMapper.useCaseToDto(commentUseCaseResult)).thenReturn(expectedComment);

        mockMvc.perform(
                        post("/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.author").value(expectedComment.author()))
                .andExpect(jsonPath("$.newsId").value(expectedComment.newsId().toString()))
                .andExpect(jsonPath("$.text").value(expectedComment.text()));

        //when


        //then
        verify(commentWebMapper, times(1)).dtoToCommand(commentCreateDto);
        verify(createCommentUseCase, times(1)).createComment(commentCommand);
        verify(commentWebMapper, times(1)).useCaseToDto(commentUseCaseResult);
    }

    @Nested
    class ReadComment {

        @Test
        void shouldReadCommentAndReturnStatus200() throws Exception {
            //given
            UUID commentId = Constant.COMMENT_UUID;
            CommentUseCaseResult commentUseCaseResult = TestDataComment.getCommentUseCaseResultForRead();
            CommentResponse expectedComment = TestDataComment.getCommentResponseForRead();

            when(readCommentUseCase.readComment(commentId)).thenReturn(commentUseCaseResult);
            when(commentWebMapper.useCaseToDto(commentUseCaseResult)).thenReturn(expectedComment);

            //when
            mockMvc.perform(get("/comments/{commentId}", commentId)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.id").value(expectedComment.id().toString()))
                    .andExpect(jsonPath("$.author").value(expectedComment.author()))
                    .andExpect(jsonPath("$.newsId").value(expectedComment.newsId().toString()))
                    .andExpect(jsonPath("$.text").value(expectedComment.text()));

            //then
            verify(readCommentUseCase, times(1)).readComment(commentId);
            verify(commentWebMapper, times(1)).useCaseToDto(commentUseCaseResult);
        }

        @Test
        void shouldNotReadComment_whenCommentNotFound() throws Exception {
            //given
            UUID commentId = Constant.COMMENT_UUID;
            when(readCommentUseCase.readComment(commentId)).thenThrow(EntityNotFoundException.class);

            //when
            mockMvc.perform(get("/comments/{commentId}", commentId)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(result ->
                            assertInstanceOf(EntityNotFoundException.class,
                                    result.getResolvedException())
                    );

            //then
            verify(readCommentUseCase, times(1)).readComment(commentId);
        }

    }

    @Nested
    class UpdateComment {

        @Test
        void shouldUpdateCommentAndReturnStatus200() throws Exception {
            //given
            UUID commentId = Constant.COMMENT_UUID;
            CommentUpdateDto commentUpdateDto = TestDataComment.getCommentUpdateDto();
            CommentUpdateCommand commentCommand = TestDataComment.getCommentUpdateCommand();
            CommentUseCaseResult commentUseCaseResult = TestDataComment.getCommentUseCaseResultForUpdate();
            CommentResponse expectedComment = TestDataComment.getCommentResponseForUpdate();

            String body = objectMapper.writeValueAsString(commentUpdateDto);

            when(commentWebMapper.dtoToCommand(commentUpdateDto)).thenReturn(commentCommand);
            when(updateCommentUseCase.updateComment(commentId, commentCommand)).thenReturn(commentUseCaseResult);
            when(commentWebMapper.useCaseToDto(commentUseCaseResult)).thenReturn(expectedComment);

            //when
            mockMvc.perform(put("/comments/{commentId}", commentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.id").value(expectedComment.id().toString()))
                    .andExpect(jsonPath("$.author").value(expectedComment.author()))
                    .andExpect(jsonPath("$.newsId").value(expectedComment.newsId().toString()))
                    .andExpect(jsonPath("$.text").value(expectedComment.text()));

            //then
            verify(commentWebMapper, times(1)).dtoToCommand(commentUpdateDto);
            verify(updateCommentUseCase, times(1)).updateComment(commentId, commentCommand);
            verify(commentWebMapper, times(1)).useCaseToDto(commentUseCaseResult);
        }


        @Test
        void shouldNotUpdateComment_whenCommentNotFound() throws Exception {
            //given
            UUID commentId = Constant.COMMENT_UUID;
            CommentUpdateDto commentUpdateDto = TestDataComment.getCommentUpdateDto();
            CommentUpdateCommand commentCommand = TestDataComment.getCommentUpdateCommand();

            String body = objectMapper.writeValueAsString(commentUpdateDto);

            when(commentWebMapper.dtoToCommand(commentUpdateDto)).thenReturn(commentCommand);
            when(updateCommentUseCase.updateComment(commentId, commentCommand)).thenThrow(EntityNotFoundException.class);

            //when
            mockMvc.perform(put("/comments/{commentId}", commentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(result ->
                            assertInstanceOf(EntityNotFoundException.class,
                                    result.getResolvedException())
                    );

            //then
            verify(commentWebMapper, times(1)).dtoToCommand(commentUpdateDto);
            verify(updateCommentUseCase, times(1)).updateComment(commentId, commentCommand);
        }
    }

    @Test
    void shouldDeleteComment() {
        //given
        UUID commentId = Constant.COMMENT_UUID;

        //when
        deleteCommentUseCase.deleteComment(commentId);

        //then
        verify(deleteCommentUseCase, times(1)).deleteComment(commentId);
    }

    @Test
    void shouldReadCommentPageAndReturnStatus200() throws Exception {
        //given
        CommentPageUseCaseResult commentUseCaseResult = TestDataComment.getCommentPageUseCaseResult();
        CommentPageableDto expectedCommentPage = TestDataComment.getCommentPageableDto();

        when(readCommentUseCase.readCommentPage(Constant.PAGE_NUMBER, Constant.PAGE_SIZE))
                .thenReturn(commentUseCaseResult);
        when(commentWebMapper.useCaseToDto(commentUseCaseResult)).thenReturn(expectedCommentPage);


        //when
        mockMvc.perform(get("/comments")
                        .param("page", String.valueOf(Constant.PAGE_NUMBER))
                        .param("size", String.valueOf(Constant.PAGE_SIZE))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(expectedCommentPage.content().size()))
                .andExpect(jsonPath("$.content[0].id").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").value(expectedCommentPage.content().getFirst().id().toString()))
                .andExpect(jsonPath("$.content[0].author").value(expectedCommentPage.content().getFirst().author()))
                .andExpect(jsonPath("$.content[0].newsId").value(expectedCommentPage.content().getFirst().newsId().toString()))
                .andExpect(jsonPath("$.content[0].text").value(expectedCommentPage.content().getFirst().text()));

        //then
        verify(readCommentUseCase, times(1))
                .readCommentPage(Constant.PAGE_NUMBER, Constant.PAGE_SIZE);
        verify(commentWebMapper, times(1)).useCaseToDto(commentUseCaseResult);
    }

    @Test
    void shouldReadCommentsByNewsAndReturnStatus200() throws Exception {
        //given
        UUID newsId = Constant.NEWS_UUID;
        CommentPageUseCaseResult commentUseCaseResult = TestDataComment.getCommentPageUseCaseResult();
        CommentPageableDto expectedCommentPage = TestDataComment.getCommentPageableDto();

        when(readCommentUseCase.readCommentsByNews(newsId, Constant.PAGE_NUMBER, Constant.PAGE_SIZE))
                .thenReturn(commentUseCaseResult);
        when(commentWebMapper.useCaseToDto(commentUseCaseResult)).thenReturn(expectedCommentPage);


        //when
        mockMvc.perform(get("/comments/news/{newsId}", newsId)
                        .param("page", String.valueOf(Constant.PAGE_NUMBER))
                        .param("size", String.valueOf(Constant.PAGE_SIZE))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(expectedCommentPage.content().size()))
                .andExpect(jsonPath("$.content[0].id").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").value(expectedCommentPage.content().getFirst().id().toString()))
                .andExpect(jsonPath("$.content[0].author").value(expectedCommentPage.content().getFirst().author()))
                .andExpect(jsonPath("$.content[0].newsId").value(expectedCommentPage.content().getFirst().newsId().toString()))
                .andExpect(jsonPath("$.content[0].text").value(expectedCommentPage.content().getFirst().text()));

        //then
        verify(readCommentUseCase, times(1))
                .readCommentsByNews(newsId, Constant.PAGE_NUMBER, Constant.PAGE_SIZE);
        verify(commentWebMapper, times(1)).useCaseToDto(commentUseCaseResult);
    }

}