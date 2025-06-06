package board.backend.application;

import board.backend.domain.ArticleCommentCount;
import board.backend.domain.Comment;
import board.backend.infra.ArticleCommentCountRepository;
import board.backend.infra.CommentRepository;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommentReaderTest {

    private CommentRepository commentRepository;
    private ArticleCommentCountRepository articleCommentCountRepository;
    private CommentReader commentReader;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        articleCommentCountRepository = mock(ArticleCommentCountRepository.class);
        commentReader = new CommentReader(commentRepository, articleCommentCountRepository);
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

        when(commentRepository.findAllById(articleId, pageSize)).thenReturn(comments);

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

        when(commentRepository.findAllById(articleId, pageSize, lastParentCommentId, lastCommentId))
            .thenReturn(comments);

        // when
        List<Comment> result = commentReader.readAll(articleId, pageSize, lastParentCommentId, lastCommentId);

        // then
        assertThat(result).isEqualTo(comments);
    }

    @Test
    @DisplayName("게시글 ID 목록에 대한 댓글 수를 Map 형태로 반환한다")
    void count_returnsCommentCountMap() {
        // given
        List<Long> articleIds = List.of(1L, 2L, 3L);
        List<ArticleCommentCount> commentCounts = List.of(
            ArticleCommentCount.init(1L),
            ArticleCommentCount.init(2L)
        );

        when(articleCommentCountRepository.findAllById(articleIds)).thenReturn(commentCounts);

        // when
        Map<Long, Long> result = commentReader.count(articleIds);

        // then
        AssertionsForInterfaceTypes.assertThat(result).containsEntry(1L, 1L)
            .containsEntry(2L, 1L)
            .doesNotContainKey(3L);
    }

    @Test
    @DisplayName("댓글 수 결과가 없으면 빈 Map을 반환한다")
    void count_emptyResult_returnsEmptyMap() {
        // given
        List<Long> articleIds = List.of(10L, 20L);
        when(articleCommentCountRepository.findAllById(articleIds)).thenReturn(List.of());

        // when
        Map<Long, Long> result = commentReader.count(articleIds);

        // then
        AssertionsForInterfaceTypes.assertThat(result).isEmpty();
    }

}