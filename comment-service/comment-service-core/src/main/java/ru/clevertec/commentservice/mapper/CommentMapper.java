package ru.clevertec.commentservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.domain.CommentPageable;
import ru.clevertec.commentservice.port.input.command.CommentCreateCommand;
import ru.clevertec.commentservice.port.input.command.CommentPageUseCaseResult;
import ru.clevertec.commentservice.port.input.command.CommentUpdateCommand;
import ru.clevertec.commentservice.port.input.command.CommentUseCaseResult;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment commandToDomain(CommentCreateCommand createCommand);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment commandToDomain(CommentUpdateCommand updateCommand);

    CommentUseCaseResult domainToUseCase(Comment comment);

    CommentPageUseCaseResult pageToUseCase(CommentPageable commentPageable);
}
