package ru.clevertec.newsservice.adapter.input.web.news;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsCreateDto;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsPageableDto;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsResponse;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsUpdateDto;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.domain.NewsPageable;
import ru.clevertec.newsservice.port.input.news.command.NewsCreateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsPageUseCaseResult;
import ru.clevertec.newsservice.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;

@Mapper(componentModel = "spring")
public interface NewsWebMapper {

    NewsCreateCommand dtoToCommand(NewsCreateDto newsDto);

    NewsUpdateCommand dtoToCommand(NewsUpdateDto newsDto);

    NewsEntity domainToEntity(News news);

    News entityToDomain(NewsEntity newsEntity);

    NewsResponse useCaseToDto(NewsUseCaseResult useCaseResult);

    NewsPageableDto useCaseToDto(NewsPageUseCaseResult caseResult);

    default NewsPageable pageToDomain(Page<NewsEntity> newsPage) {
        return NewsPageable.builder()
                .number(newsPage.getNumber() + 1)
                .size(newsPage.getSize())
                .totalPages(newsPage.getTotalPages())
                .totalElements(newsPage.getTotalElements())
                .content(newsPage.getContent().stream()
                        .map(this::entityToDomain)
                        .toList()
                )
                .build();
    }
}
