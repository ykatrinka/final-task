package ru.clevertec.commentservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.commentservice.port.output.WriteCommentPort;
import ru.clevertec.commentservice.util.Constant;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteCommentServiceTest {

    @Mock
    private WriteCommentPort commentAdapter;

    @InjectMocks
    private DeleteCommentService deleteCommentService;

    @Test
    void shouldDeleteComment() {
        //given
        UUID commentId = Constant.COMMENT_UUID;

        //when

        //then
        deleteCommentService.deleteComment(commentId);

        verify(commentAdapter, times(1)).deleteComment(commentId);
    }


}