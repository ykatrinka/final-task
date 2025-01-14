package ru.clevertec.commentservice.port.output;

import ru.clevertec.commentservice.domain.Comment;

import java.util.UUID;

public interface WriteCommentPort {

    Comment createComment(Comment comment);

    Comment updateComment(Comment comment);

    void deleteComment(UUID commentId);

}
