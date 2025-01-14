package ru.clevertec.commentservice.adapter.input.web.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    public static final String URL_READ_COMMENT = "/{commentId}";
    public static final String URL_UPDATE_COMMENT = "/{commentId}";
    public static final String URL_DELETE_COMMENT = "/{commentId}";
    public static final String URL_NEWS_COMMENTS = "/news/{newsId}";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_SIZE = "size";
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SIZE = "10";

    public static final String URL_SEARCH = "/search";
    public static final String PARAM_SEARCH_TEXT = "text";
    public static final String PARAM_SEARCH_LIMIT = "limit";
    public static final String PARAM_SEARCH_FIELDS = "fields";
    public static final String SEARCH_LIMIT = "15";

    private final CreateCommentUseCase createCommentUseCase;
    private final ReadCommentUseCase readCommentUseCase;
    private final UpdateCommentUseCase updateCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;

    private final CommentWebMapper commentMapper;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentCreateDto commentCreateDto) {
        CommentCreateCommand createCommand = commentMapper.dtoToCommand(commentCreateDto);
        CommentUseCaseResult caseResultComment = createCommentUseCase.createComment(createCommand);
        CommentResponse commentResponse = commentMapper.useCaseToDto(caseResultComment);

        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);

    }

    @GetMapping(value = URL_READ_COMMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CommentResponse> readComment(@PathVariable @NotNull UUID commentId) {
        CommentUseCaseResult caseResultComment = readCommentUseCase.readComment(commentId);
        CommentResponse commentResponse = commentMapper.useCaseToDto(caseResultComment);

        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @PutMapping(URL_UPDATE_COMMENT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable @NotNull UUID commentId,
            @Valid @RequestBody CommentUpdateDto commentUpdateDto
    ) {
        CommentUpdateCommand updateCommand = commentMapper.dtoToCommand(commentUpdateDto);
        CommentUseCaseResult caseResultComment = updateCommentUseCase.updateComment(commentId, updateCommand);
        CommentResponse commentResponse = commentMapper.useCaseToDto(caseResultComment);

        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @DeleteMapping(URL_DELETE_COMMENT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteComment(@PathVariable @NotNull UUID commentId) {
        deleteCommentUseCase.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CommentPageableDto> readCommentPage(
            @RequestParam(name = PARAM_PAGE, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(name = PARAM_SIZE, defaultValue = DEFAULT_SIZE) int size
    ) {
        CommentPageUseCaseResult caseResult = readCommentUseCase.readCommentPage(page, size);
        CommentPageableDto pageableComment = commentMapper.useCaseToDto(caseResult);
        return new ResponseEntity<>(pageableComment, HttpStatus.OK);
    }


    @GetMapping(URL_NEWS_COMMENTS)
    public ResponseEntity<CommentPageableDto> readCommentsByNews(
            @PathVariable("newsId") UUID newsId,
            @RequestParam(name = PARAM_PAGE, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(name = PARAM_SIZE, defaultValue = DEFAULT_SIZE) int size
    ) {
        CommentPageUseCaseResult caseResult = readCommentUseCase.readCommentsByNews(newsId, page, size);
        CommentPageableDto pageableComment = commentMapper.useCaseToDto(caseResult);
        return new ResponseEntity<>(pageableComment, HttpStatus.OK);
    }

    @GetMapping(URL_SEARCH)
    public ResponseEntity<List<CommentResponse>> searchComments(
            @RequestParam(name = PARAM_SEARCH_TEXT) String text,
            @RequestParam(name = PARAM_SEARCH_LIMIT, defaultValue = SEARCH_LIMIT, required = false) int limit,
            @RequestParam(name = PARAM_SEARCH_FIELDS) List<String> fields
    ) {
        List<CommentUseCaseResult> caseResult = readCommentUseCase.searchComment(text, fields, limit);
        List<CommentResponse> news = caseResult.stream()
                .map(commentMapper::useCaseToDto)
                .toList();
        return new ResponseEntity<>(news, HttpStatus.OK);
    }
}
