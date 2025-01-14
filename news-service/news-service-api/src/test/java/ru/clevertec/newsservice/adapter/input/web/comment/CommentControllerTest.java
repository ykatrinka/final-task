package ru.clevertec.newsservice.adapter.input.web.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.exceptionstarter.handler.GlobalExceptionHandling;
import ru.clevertec.newsservice.adapter.input.web.comment.dto.CommentResponse;
import ru.clevertec.newsservice.adapter.input.web.comment.dto.CommentsPageResponse;
import ru.clevertec.newsservice.port.input.comment.ReadCommentsUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentsUseCaseResult;
import ru.clevertec.newsservice.util.Constant;
import ru.clevertec.newsservice.util.TestDataComment;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @MockitoBean
    private ReadCommentsUseCase readCommentsUseCase;

    @MockitoBean
    private CommentWebMapper commentWebMapper;

    @MockitoBean
    private GlobalExceptionHandling globalExceptionHandling;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReadCommentsByNewsAndReturnStatus200() throws Exception {
        //given
        UUID newsId = Constant.NEWS_UUID;
        CommentsUseCaseResult useCaseResult = TestDataComment.getCommentUseCaseResult();
        CommentsPageResponse expectedComments = TestDataComment.getCommentsPageResponse();


        when(readCommentsUseCase.readComments(newsId, Constant.PAGE_NUMBER, Constant.PAGE_SIZE))
                .thenReturn(useCaseResult);
        when(commentWebMapper.useCaseToDto(useCaseResult)).thenReturn(expectedComments);


        //when
        mockMvc.perform(get("/news/{newsId}/comments", newsId)
                        .param("page", String.valueOf(Constant.PAGE_NUMBER))
                        .param("size", String.valueOf(Constant.PAGE_SIZE))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(expectedComments.content().size()))
                .andExpect(jsonPath("$.content[0].id").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").value(expectedComments.content().getFirst().id().toString()))
                .andExpect(jsonPath("$.content[0].author").value(expectedComments.content().getFirst().author()))
                .andExpect(jsonPath("$.content[0].newsId").value(expectedComments.content().getFirst().newsId().toString()))
                .andExpect(jsonPath("$.content[0].text").value(expectedComments.content().getFirst().text()));

        //then
        verify(readCommentsUseCase, times(1))
                .readComments(newsId, Constant.PAGE_NUMBER, Constant.PAGE_SIZE);
        verify(commentWebMapper, times(1)).useCaseToDto(useCaseResult);
    }


    @Test
    void shouldReadCommentAndReturnStatus200() throws Exception {
        //given
        UUID newsId = Constant.NEWS_UUID;
        UUID commentId = Constant.COMMENT_UUID;
        CommentUseCase useCaseResult = TestDataComment.getCommentUseCase();
        CommentResponse expectedComments = TestDataComment.getCommentResponse();


        when(readCommentsUseCase.readComment(newsId, commentId)).thenReturn(useCaseResult);
        when(commentWebMapper.useCaseToCommentDto(useCaseResult)).thenReturn(expectedComments);


        //when
        mockMvc.perform(get("/news/{newsId}/comments/{commentsId}", newsId, commentId)
                        .param("page", String.valueOf(Constant.PAGE_NUMBER))
                        .param("size", String.valueOf(Constant.PAGE_SIZE))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").value(expectedComments.id().toString()))
                .andExpect(jsonPath("$.author").value(expectedComments.author()))
                .andExpect(jsonPath("$.newsId").value(expectedComments.newsId().toString()))
                .andExpect(jsonPath("$.text").value(expectedComments.text()));

        //then
        verify(readCommentsUseCase, times(1)).readComment(newsId, commentId);
        verify(commentWebMapper, times(1)).useCaseToCommentDto(useCaseResult);
    }

}