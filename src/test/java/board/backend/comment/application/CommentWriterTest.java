package board.backend.comment.application;

import board.backend.article.application.ArticleReader;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.Comment;
import board.backend.comment.domain.CommentNotFound;
import board.backend.comment.infra.ArticleCommentCountRepository;
import board.backend.comment.infra.CommentRepository;
import board.backend.common.infra.CacheRepository;
import board.backend.common.support.IdProvider;
import board.backend.common.support.TimeProvider;
import board.backend.user.application.UserReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommentWriterTest {

    private IdProvider idProvider;
    private TimeProvider timeProvider;
    private CommentRepository commentRepository;
    private CommentWriter commentWriter;

    @BeforeEach
    void setUp() {
        idProvider = mock(IdProvider.class);
        timeProvider = mock(TimeProvider.class);
        commentRepository = mock(CommentRepository.class);
        CacheRepository<ArticleCommentCount, Long> articleCommentCountCacheRepository = mock(CacheRepository.class);
        ArticleCommentCountRepository articleCommentCountRepository = mock(ArticleCommentCountRepository.class);
        ArticleReader articleReader = mock(ArticleReader.class);
        UserReader userReader = mock(UserReader.class);
        commentWriter = new CommentWriter(idProvider, timeProvider, commentRepository, articleCommentCountCacheRepository, articleCommentCountRepository, articleReader, userReader);
    }

    @Test
    @DisplayName("부모 댓글 없이 댓글 생성 성공")
    void create_success_withoutParent() {
        // given
        Long articleId = 1L;
        Long writerId = 2L;
        String content = "댓글입니다.";
        Long newCommentId = 10L;
        LocalDateTime now = LocalDateTime.now();

        when(idProvider.nextId()).thenReturn(newCommentId);
        when(timeProvider.now()).thenReturn(now);

        // when
        Comment comment = commentWriter.create(articleId, writerId, null, content);

        // then
        assertThat(comment.getId()).isEqualTo(newCommentId);
    }

    @Test
    @DisplayName("존재하지 않는 부모 댓글이면 예외 발생")
    void create_fail_parentCommentNotFound() {
        // given
        Long parentId = 999L;
        when(commentRepository.existsById(parentId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> commentWriter.create(1L, 2L, parentId, "대댓글"))
            .isInstanceOf(CommentNotFound.class);
    }

    @Test
    @DisplayName("삭제된 댓글이면 삭제하지 않음")
    void delete_doNothing_ifAlreadyDeleted() {
        // given
        Long commentId = 1L;
        Comment comment = mock(Comment.class);
        when(comment.getIsDeleted()).thenReturn(true);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // when
        commentWriter.delete(commentId, 100L);

        // then
    }

    @Test
    @DisplayName("자식 댓글이 있으면 논리 삭제")
    void delete_markAsDeleted_ifHasChildren() {
        // given
        Long commentId = 1L;
        Comment comment = mock(Comment.class);
        when(comment.getIsDeleted()).thenReturn(false);
        when(comment.getArticleId()).thenReturn(10L);
        when(comment.getId()).thenReturn(commentId);
        when(comment.delete()).thenReturn(comment);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.countBy(10L, commentId, 2)).thenReturn(2);

        // when
        commentWriter.delete(commentId, 100L);
    }

    @Test
    @DisplayName("자식 댓글이 없으면 물리 삭제")
    void delete_completely_ifNoChildren() {
        // given
        Long commentId = 1L;
        Comment comment = mock(Comment.class);
        when(comment.getIsDeleted()).thenReturn(false);
        when(comment.getArticleId()).thenReturn(10L);
        when(comment.getId()).thenReturn(commentId);
        when(comment.isRoot()).thenReturn(true);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.countBy(10L, commentId, 2)).thenReturn(1);

        // when
        commentWriter.delete(commentId, 100L);
    }

    @Test
    @DisplayName("게시글 댓글 전체 삭제")
    void delete_allCommentsByArticle() {
        // given
        Long articleId = 1L;

        // when
        commentWriter.deleteArticle(articleId);
    }

}