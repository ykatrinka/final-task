package ru.clevertec.commentservice.adapter.output.persistence.adapter;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.commentservice.adapter.input.web.comment.CommentWebMapper;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.entity.CommentEntity;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.repository.CommentRepository;
import ru.clevertec.commentservice.domain.Comment;
import ru.clevertec.commentservice.domain.CommentPageable;
import ru.clevertec.commentservice.util.Constant;
import ru.clevertec.commentservice.util.TestDataComment;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadCommentAdapterTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentWebMapper commentWebMapper;

    @InjectMocks
    private ReadCommentAdapter readCommentAdapter;

    @Nested
    class ReadComment {

        @Test
        void shouldReadComment() {
            //given
            UUID commentId = Constant.COMMENT_UUID;
            CommentEntity commentEntity = TestDataComment.getCommentEntityForRead();
            Comment expectedComment = TestDataComment.getCommentForRead();

            when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(commentEntity));
            when(commentWebMapper.entityToDomain(commentEntity)).thenReturn(expectedComment);

            //when
            Comment actualComment = readCommentAdapter.readComment(commentId);

            //then
            assertAll(
                    () -> assertNotNull(actualComment),
                    () -> assertEquals(expectedComment.getId(), actualComment.getId()),
                    () -> assertEquals(expectedComment.getAuthor(), actualComment.getAuthor()),
                    () -> assertEquals(expectedComment.getNewsId(), actualComment.getNewsId()),
                    () -> assertEquals(expectedComment.getText(), actualComment.getText())
            );

            verify(commentRepository, times(1)).findById(commentId);
            verify(commentWebMapper, times(1)).entityToDomain(commentEntity);
        }

        @Test
        void shouldNotReadComment_whenCommentNotFound() {
            //given
            UUID commentId = Constant.COMMENT_UUID;

            when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

            //when, then
            assertThrows(EntityNotFoundException.class, () -> readCommentAdapter.readComment(commentId));

            verify(commentRepository, times(1)).findById(commentId);
        }

        @Test
        void shouldReadCommentPage() {
            //given
            PageRequest pageable = TestDataComment.getPageRequest(
                    Constant.PAGE_NUMBER - 1,
                    Constant.PAGE_SIZE
            );
            Page<CommentEntity> pageCommentEntity = TestDataComment.getPageCommentEntity();
            CommentPageable expectedCommentPage = TestDataComment.getCommentPageable();

            when(commentRepository.findAll(pageable)).thenReturn(pageCommentEntity);
            when(commentWebMapper.pageToDomain(pageCommentEntity)).thenReturn(expectedCommentPage);

            //when
            CommentPageable actualCommentPage = readCommentAdapter.readCommentPage(Constant.PAGE_NUMBER, Constant.PAGE_SIZE);

            //then
            assertAll(
                    () -> assertNotNull(actualCommentPage),
                    () -> assertEquals(expectedCommentPage.getContent().size(), actualCommentPage.getContent().size()),
                    () -> assertNotNull(actualCommentPage.getContent().getFirst()),
                    () -> assertEquals(expectedCommentPage.getContent().getFirst().getId(), actualCommentPage.getContent().getFirst().getId()),
                    () -> assertEquals(expectedCommentPage.getContent().getFirst().getAuthor(), actualCommentPage.getContent().getFirst().getAuthor()),
                    () -> assertEquals(expectedCommentPage.getContent().getFirst().getNewsId(), actualCommentPage.getContent().getFirst().getNewsId()),
                    () -> assertEquals(expectedCommentPage.getContent().getFirst().getText(), actualCommentPage.getContent().getFirst().getText())
            );

            verify(commentRepository, times(1)).findAll(pageable);
            verify(commentWebMapper, times(1)).pageToDomain(pageCommentEntity);
        }

        @Test
        void shouldReadCommentsByNews() {
            //given
            UUID newsId = Constant.NEWS_UUID;
            PageRequest pageable = TestDataComment.getPageRequest(
                    Constant.PAGE_NUMBER - 1,
                    Constant.PAGE_SIZE
            );
            Page<CommentEntity> pageCommentEntity = TestDataComment.getPageCommentEntity();
            CommentPageable expectedCommentPage = TestDataComment.getCommentPageable();

            when(commentRepository.findByNewsId(newsId, pageable)).thenReturn(pageCommentEntity);
            when(commentWebMapper.pageToDomain(pageCommentEntity)).thenReturn(expectedCommentPage);

            //when
            CommentPageable actualCommentPage = readCommentAdapter.readCommentsByNews(newsId, Constant.PAGE_NUMBER, Constant.PAGE_SIZE);

            //then
            assertAll(
                    () -> assertNotNull(actualCommentPage),
                    () -> assertEquals(expectedCommentPage.getContent().size(), actualCommentPage.getContent().size()),
                    () -> assertNotNull(actualCommentPage.getContent().getFirst()),
                    () -> assertEquals(expectedCommentPage.getContent().getFirst().getId(), actualCommentPage.getContent().getFirst().getId()),
                    () -> assertEquals(expectedCommentPage.getContent().getFirst().getAuthor(), actualCommentPage.getContent().getFirst().getAuthor()),
                    () -> assertEquals(expectedCommentPage.getContent().getFirst().getNewsId(), actualCommentPage.getContent().getFirst().getNewsId()),
                    () -> assertEquals(expectedCommentPage.getContent().getFirst().getText(), actualCommentPage.getContent().getFirst().getText())
            );

            verify(commentRepository, times(1)).findByNewsId(newsId, pageable);
            verify(commentWebMapper, times(1)).pageToDomain(pageCommentEntity);
        }

    }


}