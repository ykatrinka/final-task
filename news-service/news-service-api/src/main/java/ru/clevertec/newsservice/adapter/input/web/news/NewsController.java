package ru.clevertec.newsservice.adapter.input.web.news;

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
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsCreateDto;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsPageableDto;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsResponse;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsUpdateDto;
import ru.clevertec.newsservice.port.input.news.CreateNewsUseCase;
import ru.clevertec.newsservice.port.input.news.DeleteNewsUseCase;
import ru.clevertec.newsservice.port.input.news.ReadNewsUseCase;
import ru.clevertec.newsservice.port.input.news.UpdateNewsUseCase;
import ru.clevertec.newsservice.port.input.news.command.NewsCreateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsPageUseCaseResult;
import ru.clevertec.newsservice.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    public static final String URL_SEARCH = "/search";
    public static final String PARAM_SEARCH_TEXT = "text";
    public static final String PARAM_SEARCH_LIMIT = "limit";
    public static final String PARAM_SEARCH_FIELDS = "fields";
    public static final String SEARCH_LIMIT = "15";

    public static final String URL_READ_NEWS = "/{newsId}";
    public static final String URL_UPDATE_NEWS = "/{newsId}";
    public static final String URL_DELETE_NEWS = "/{newsId}";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_SIZE = "size";
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SIZE = "10";
    public static final String URL_VALIDATE_NEWS = "/{newsId}/exists";

    private final CreateNewsUseCase createNewsUseCase;
    private final ReadNewsUseCase readNewsUseCase;
    private final UpdateNewsUseCase updateNewsUseCase;
    private final DeleteNewsUseCase deleteNewsUseCase;

    private final NewsWebMapper newsMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NewsResponse> createNews(@Valid @RequestBody NewsCreateDto newsCreateDto) {
        NewsCreateCommand createCommand = newsMapper.dtoToCommand(newsCreateDto);
        NewsUseCaseResult caseResultNews = createNewsUseCase.createNews(createCommand);
        NewsResponse newsResponse = newsMapper.useCaseToDto(caseResultNews);

        return new ResponseEntity<>(newsResponse, HttpStatus.CREATED);

    }

    @GetMapping(value = URL_READ_NEWS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NewsResponse> readNews(@PathVariable @NotNull UUID newsId) {
        NewsUseCaseResult caseResultNews = readNewsUseCase.readNews(newsId);
        NewsResponse newsResponse = newsMapper.useCaseToDto(caseResultNews);

        return new ResponseEntity<>(newsResponse, HttpStatus.OK);
    }

    @PutMapping(URL_UPDATE_NEWS)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NewsResponse> updateNews(
            @PathVariable @NotNull UUID newsId,
            @Valid @RequestBody NewsUpdateDto newsUpdateDto
    ) {
        NewsUpdateCommand updateCommand = newsMapper.dtoToCommand(newsUpdateDto);
        NewsUseCaseResult caseResultNews = updateNewsUseCase.updateNews(newsId, updateCommand);
        NewsResponse newsResponse = newsMapper.useCaseToDto(caseResultNews);

        return new ResponseEntity<>(newsResponse, HttpStatus.OK);
    }

    @DeleteMapping(URL_DELETE_NEWS)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteNews(@PathVariable @NotNull UUID newsId) {
        deleteNewsUseCase.deleteNews(newsId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NewsPageableDto> readNewsPage(
            @RequestParam(name = PARAM_PAGE, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(name = PARAM_SIZE, defaultValue = DEFAULT_SIZE) int size
    ) {
        NewsPageUseCaseResult caseResult = readNewsUseCase.readNewsPage(page, size);
        NewsPageableDto pageableNews = newsMapper.useCaseToDto(caseResult);
        return new ResponseEntity<>(pageableNews, HttpStatus.OK);
    }

    @GetMapping(path = URL_VALIDATE_NEWS)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Boolean> existsNews(@PathVariable UUID newsId) {
        boolean useCaseResult = readNewsUseCase.isNewsExists(newsId);
        return new ResponseEntity<>(useCaseResult, HttpStatus.OK);
    }

    @GetMapping(URL_SEARCH)
    public ResponseEntity<List<NewsResponse>> searchNews(
            @RequestParam(name = PARAM_SEARCH_TEXT) String text,
            @RequestParam(name = PARAM_SEARCH_LIMIT, defaultValue = SEARCH_LIMIT, required = false) int limit,
            @RequestParam(name = PARAM_SEARCH_FIELDS) List<String> fields
    ) {
        List<NewsUseCaseResult> caseResult = readNewsUseCase.searchNews(text, fields, limit);
        List<NewsResponse> news = caseResult.stream()
                .map(newsMapper::useCaseToDto)
                .toList();
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

}
