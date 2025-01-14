package ru.clevertec.commentservice.port.input;

import java.util.UUID;

public interface DeleteCommentUseCase {
    void deleteComment(UUID commentId);
}

