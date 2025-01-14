package ru.clevertec.newsservice.port.output;


import ru.clevertec.newsservice.domain.comment.Comment;
import ru.clevertec.newsservice.domain.comment.CommentsPage;

import java.util.UUID;

public interface ReadCommentPort {
    CommentsPage readComments(UUID newsId, int page, int size);

    Comment readComment(UUID newsId, UUID commentId);
}
