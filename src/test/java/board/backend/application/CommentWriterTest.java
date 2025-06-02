package board.backend.application;

import board.backend.domain.Comment;
import board.backend.domain.CommentNotFound;
import board.backend.infra.ArticleCommentCountRepository;
import board.backend.infra.CommentRepository;
import board.backend.support.IdProvider;
import board.backend.support.TimeProvider;
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
    private ArticleCommentCountRepository articleCommentCountRepository;
    private CommentWriter commentWriter;

    @BeforeEach
    void setUp() {
        idProvider = mock(IdProvider.class);
        timeProvider = mock(TimeProvider.class);
        commentRepository = mock(CommentRepository.class);
        articleCommentCountRepository = mock(ArticleCommentCountRepository.class);
        commentWriter = new CommentWriter(idProvider, timeProvider, commentRepository, articleCommentCountRepository);
    }

    @Test
    @DisplayName("부모 댓글 없이 댓글 생성에 성공한다")
    void create_successWithoutParentCommentId() {
        // given
        Long newCommentId = 10L;
        Long articleId = 1L;
        Long writerId = 100L;
        String content = "댓글입니다";
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        when(idProvider.nextId()).thenReturn(newCommentId);
        when(timeProvider.now()).thenReturn(now);
        when(articleCommentCountRepository.increase(articleId)).thenReturn(1L); // count 증가 성공

        // when
        Comment result = commentWriter.create(articleId, writerId, null, content);

        // then
        assertThat(result.getId()).isEqualTo(newCommentId);
    }

    @Test
    @DisplayName("부모 댓글이 존재할 경우 댓글 생성에 성공한다")
    void create_successWithParentCommentId() {
        // given
        Long parentId = 10L;
        Long newCommentId = 11L;
        Long articleId = 1L;
        Long writerId = 100L;
        String content = "대댓글입니다";
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        when(idProvider.nextId()).thenReturn(newCommentId);
        when(timeProvider.now()).thenReturn(now);
        when(commentRepository.existsById(parentId)).thenReturn(true);
        when(articleCommentCountRepository.increase(articleId)).thenReturn(1L);

        // when
        Comment result = commentWriter.create(articleId, writerId, parentId, content);

        // then
        assertThat(result.getParentId()).isEqualTo(parentId);
    }

    @Test
    @DisplayName("첫 댓글 생성에 성공한다")
    void create_commentCountIncreaseFails_thenSave() {
        // given
        Long articleId = 1L;
        Long newCommentId = 11L;
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        when(idProvider.nextId()).thenReturn(newCommentId);
        when(timeProvider.now()).thenReturn(now);
        when(articleCommentCountRepository.increase(articleId)).thenReturn(0L);

        // when
        commentWriter.create(articleId, 100L, null, "댓글");
    }

    @Test
    @DisplayName("부모 댓글이 존재하지 않으면 예외가 발생한다")
    void create_failWhenParentCommentNotFound() {
        // given
        Long parentId = 10L;
        Long articleId = 1L;
        Long writerId = 100L;
        String content = "잘못된 대댓글";

        when(commentRepository.existsById(parentId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> commentWriter.create(articleId, writerId, parentId, content))
            .isInstanceOf(CommentNotFound.class);
    }

    @Test
    @DisplayName("삭제된 댓글이면 삭제하지 않는다")
    void delete_alreadyDeleted_doesNothing() {
        // given
        Long commentId = 1L;
        Comment deletedComment = mock(Comment.class);
        when(deletedComment.getIsDeleted()).thenReturn(true);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(deletedComment));

        // when
        commentWriter.delete(commentId);
    }

    @Test
    @DisplayName("자식이 있는 댓글은 논리 삭제된다")
    void delete_withChildren_marksAsDeleted() {
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
        commentWriter.delete(commentId);
    }

    @Test
    @DisplayName("자식이 없는 댓글은 물리 삭제된다")
    void delete_withoutChildrenDeletesComment() {
        // given
        Long commentId = 1L;
        Comment comment = mock(Comment.class);
        when(comment.getIsDeleted()).thenReturn(false);
        when(comment.getArticleId()).thenReturn(10L);
        when(comment.getId()).thenReturn(commentId);
        when(comment.isRoot()).thenReturn(true); // 루트 댓글이므로 부모 재귀 삭제 없음
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.countBy(10L, commentId, 2)).thenReturn(1);

        // when
        commentWriter.delete(commentId);
    }

    @Test
    @DisplayName("부모가 삭제 상태이고 자식이 없으면 재귀적으로 삭제한다")
    void delete_recursivelyDeletesParentIfDeletedAndNoChildren() {
        // given
        Long commentId = 2L;
        Long parentId = 1L;

        Comment child = mock(Comment.class);
        when(child.getIsDeleted()).thenReturn(false);
        when(child.getArticleId()).thenReturn(10L);
        when(child.getId()).thenReturn(commentId);
        when(child.getParentId()).thenReturn(parentId);
        when(child.isRoot()).thenReturn(false);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(child));
        when(commentRepository.countBy(10L, commentId, 2)).thenReturn(1);

        Comment parent = mock(Comment.class);
        when(parent.getIsDeleted()).thenReturn(true);
        when(parent.getArticleId()).thenReturn(10L);
        when(parent.getId()).thenReturn(parentId);
        when(parent.isRoot()).thenReturn(true);
        when(commentRepository.findById(parentId)).thenReturn(Optional.of(parent));
        when(commentRepository.countBy(10L, parentId, 2)).thenReturn(1);

        // when
        commentWriter.delete(commentId);
    }

}