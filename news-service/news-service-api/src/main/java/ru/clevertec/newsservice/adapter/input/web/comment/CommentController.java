package ru.clevertec.newsservice.adapter.input.web.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsservice.adapter.input.web.comment.dto.CommentResponse;
import ru.clevertec.newsservice.adapter.input.web.comment.dto.CommentsPageResponse;
import ru.clevertec.newsservice.port.input.comment.ReadCommentsUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentsUseCaseResult;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CommentController {

    public static final String URL_NEWS_COMMENTS = "/news/{newsId}/comments";
    public static final String URL_COMMENT = "/news/{newsId}/comments/{commentId}";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_SIZE = "size";
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SIZE = "10";

    private final ReadCommentsUseCase readCommentsUseCase;
    private final CommentWebMapper commentWebMapper;

    @GetMapping(URL_NEWS_COMMENTS)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CommentsPageResponse> readComments(
            @PathVariable UUID newsId,
            @RequestParam(name = PARAM_PAGE, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(name = PARAM_SIZE, defaultValue = DEFAULT_SIZE) int size
    ) {
        CommentsUseCaseResult useCaseResult = readCommentsUseCase.readComments(newsId, page, size);
        CommentsPageResponse commentsPageable = commentWebMapper.useCaseToDto(useCaseResult);
        return new ResponseEntity<>(commentsPageable, HttpStatus.OK);
    }

    @GetMapping(URL_COMMENT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CommentResponse> readComment(@PathVariable UUID newsId, @PathVariable UUID commentId) {
        CommentUseCase useCaseResult = readCommentsUseCase.readComment(newsId, commentId);
        CommentResponse comment = commentWebMapper.useCaseToCommentDto(useCaseResult);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

}
