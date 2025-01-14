package ru.clevertec.commentservice.adapter.input.web.comment;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentCreateDto;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentPageableDto;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentResponse;
import ru.clevertec.commentservice.adapter.input.web.comment.dto.CommentUpdateDto;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.entity.CommentEntity;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.domain.CommentPageable;
import ru.clevertec.commentservice.port.input.command.CommentCreateCommand;
import ru.clevertec.commentservice.port.input.command.CommentPageUseCaseResult;
import ru.clevertec.commentservice.port.input.command.CommentUpdateCommand;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;

@Mapper(componentModel = "spring")
public interface CommentWebMapper {

    CommentCreateCommand dtoToCommand(CommentCreateDto commentDto);

    CommentUpdateCommand dtoToCommand(CommentUpdateDto commentDto);

    CommentEntity domainToEntity(Comment comment);

    Comment entityToDomain(CommentEntity commentEntity);

    CommentResponse useCaseToDto(CommentUseCaseResult caseResult);

    CommentPageableDto useCaseToDto(CommentPageUseCaseResult caseResult);

    default CommentPageable pageToDomain(Page<CommentEntity> newsPage) {
        return CommentPageable.builder()
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
