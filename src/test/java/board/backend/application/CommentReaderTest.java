package board.backend.application;

import board.backend.domain.Comment;
import board.backend.infra.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommentReaderTest {

    private CommentRepository commentRepository;
    private CommentReader commentReader;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        commentReader = new CommentReader(commentRepository);
    }

    @Test
    @DisplayName("lastParentCommentId와 lastCommentId가 null이면 첫 페이지 댓글을 조회한다")
    void readAll_withoutLastParentCommentIdAndLastCommentIdReturnsFirstPage() {
        // given
        Long articleId = 1L;
        Long pageSize = 3L;

        List<Comment> comments = List.of(
            Comment.create(1L, articleId, 100L, 1L, "댓글1", LocalDateTime.now()),
            Comment.create(2L, articleId, 101L, 2L, "댓글2", LocalDateTime.now())
        );

        when(commentRepository.findAllByArticleId(articleId, pageSize)).thenReturn(comments);

        // when
        List<Comment> result = commentReader.readAll(articleId, pageSize, null, null);

        // then
        assertThat(result).isEqualTo(comments);
    }

    @Test
    @DisplayName("lastParentCommentId와 lastCommentId가 존재하면 다음 페이지 댓글을 조회한다")
    void readAll_withLastParentCommentIdAndLastCommentIdReturnsNextPage() {
        // given
        Long articleId = 1L;
        Long pageSize = 3L;
        Long lastParentCommentId = 10L;
        Long lastCommentId = 15L;

        List<Comment> comments = List.of(
            Comment.create(16L, articleId, 100L, 10L, "댓글3", LocalDateTime.now()),
            Comment.create(17L, articleId, 101L, 10L, "댓글4", LocalDateTime.now())
        );

        when(commentRepository.findAllByArticleId(articleId, pageSize, lastParentCommentId, lastCommentId))
            .thenReturn(comments);

        // when
        List<Comment> result = commentReader.readAll(articleId, pageSize, lastParentCommentId, lastCommentId);

        // then
        assertThat(result).isEqualTo(comments);
    }

}