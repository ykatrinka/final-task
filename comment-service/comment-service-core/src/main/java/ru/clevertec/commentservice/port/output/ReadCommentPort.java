package ru.clevertec.commentservice.port.output;

import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.domain.CommentPageable;

import java.util.List;
import java.util.UUID;

public interface ReadCommentPort {

    Comment readComment(UUID commentId);

    CommentPageable readCommentPage(int page, int size);

    CommentPageable readCommentsByNews(UUID newsId, int page, int size);

    List<Comment> searchComment(String text, List<String> fields, int limit);
}
