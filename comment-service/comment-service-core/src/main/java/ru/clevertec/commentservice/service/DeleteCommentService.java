package ru.clevertec.commentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.commentservice.port.input.DeleteCommentUseCase;
import ru.clevertec.commentservice.port.output.WriteCommentPort;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteCommentService implements DeleteCommentUseCase {

    private final WriteCommentPort commentAdapter;

    @Override
    public void deleteComment(UUID commentId) {
        commentAdapter.deleteComment(commentId);
    }
}
