package ru.clevertec.newsservice.adapter.output.persistence.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.newsservice.domain.comment.Comment;
import ru.clevertec.newsservice.domain.comment.CommentsPage;

import java.util.UUID;

@FeignClient(
        name = "${feign.client.comment-service.name}",
        url = "${feign.client.comment-service.url}"
)
public interface CommentClient {

    String PARAM_PAGE = "page";
    String PARAM_SIZE = "size";
    String DEFAULT_PAGE = "1";
    String DEFAULT_SIZE = "10";
    String URL_COMMENTS = "comments/news/{newsId}";
    String URL_COMMENT = "comments/{commentId}";


    @GetMapping(path = URL_COMMENTS)
    ResponseEntity<CommentsPage> readComments(
            @PathVariable UUID newsId,
            @RequestParam(name = PARAM_PAGE, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(name = PARAM_SIZE, defaultValue = DEFAULT_SIZE) int size
    );

    @GetMapping(path = URL_COMMENT)
    ResponseEntity<Comment> readComment(@PathVariable UUID commentId);

}
