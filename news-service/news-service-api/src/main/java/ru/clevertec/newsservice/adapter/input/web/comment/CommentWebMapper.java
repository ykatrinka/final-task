package ru.clevertec.newsservice.adapter.input.web.comment;

import org.mapstruct.Mapper;
import ru.clevertec.newsservice.adapter.input.web.comment.dto.CommentResponse;
import ru.clevertec.newsservice.adapter.input.web.comment.dto.CommentsPageResponse;
import ru.clevertec.newsservice.port.input.comment.command.CommentUseCase;
import ru.clevertec.newsservice.port.input.comment.command.CommentsUseCaseResult;

@Mapper(componentModel = "spring")
public interface CommentWebMapper {
    CommentsPageResponse useCaseToDto(CommentsUseCaseResult useCaseResult);

    CommentResponse useCaseToCommentDto(CommentUseCase useCaseResult);
}
