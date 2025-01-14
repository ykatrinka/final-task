package ru.clevertec.newsservice.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.newsservice.domain.comment.Comment;
import ru.clevertec.newsservice.domain.comment.CommentsPage;
import ru.clevertec.newsservice.port.input.comment.command.CommentUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentsUseCaseResult;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentUseCase commentToUseCase(Comment comment);

    CommentsUseCaseResult domainToUseCase(CommentsPage comments);
}
