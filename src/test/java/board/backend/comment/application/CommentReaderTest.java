package board.backend.comment.application;

import board.backend.comment.application.dto.CommentWithWriter;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.Comment;
import board.backend.comment.infra.ArticleCommentCountRepository;
import board.backend.comment.infra.CommentRepository;
import board.backend.common.infra.CacheRepository;
import board.backend.user.application.UserReader;
import board.backend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommentReaderTest {

    private CommentRepository commentRepository;
    private CacheRepository<ArticleCommentCount, Long> articleCommentCountCacheRepository;
    private ArticleCommentCountRepository articleCommentCountRepository;
    private UserReader userReader;
    private CommentReader commentReader;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        articleCommentCountCacheRepository = mock(CacheRepository.class);
        articleCommentCountRepository = mock(ArticleCommentCountRepository.class);
        userReader = mock(UserReader.class);
        commentReader = new CommentReader(commentRepository, articleCommentCountCacheRepository, articleCommentCountRepository, userReader);
    }

    @Test
    @DisplayName("lastCommentId가 없을 때 댓글 목록을 조회한다")
    void readAll_withoutLastCommentId_success() {
        // given
        Long articleId = 1L;
        Long pageSize = 3L;
        List<Comment> comments = List.of(
            Comment.create(1L, articleId, 10L, null, "댓글1", LocalDateTime.now()),
            Comment.create(2L, articleId, 11L, null, "댓글2", LocalDateTime.now())
        );
        Map<Long, User> writerMap = Map.of(
            10L, User.create(10L, "user10@email.com", "user10", LocalDateTime.now()),
            11L, User.create(11L, "user11@email.com", "user11", LocalDateTime.now())
        );
        when(commentRepository.findAllById(articleId, pageSize)).thenReturn(comments);
        when(userReader.readAll(anyList())).thenReturn(writerMap);

        // when
        List<CommentWithWriter> result = commentReader.readAll(articleId, pageSize, null, null);

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("lastCommentId가 있을 때 다음 댓글 목록을 조회한다")
    void readAll_withLastCommentId_success() {
        // given
        Long articleId = 1L;
        Long pageSize = 3L;
        Long lastParentCommentId = 5L;
        Long lastCommentId = 7L;
        List<Comment> comments = List.of(
            Comment.create(8L, articleId, 12L, 5L, "댓글3", LocalDateTime.now())
        );
        Map<Long, User> writerMap = Map.of(
            12L, User.create(12L, "user12@email.com", "user12", LocalDateTime.now())
        );
        when(commentRepository.findAllById(articleId, pageSize, lastParentCommentId, lastCommentId)).thenReturn(comments);
        when(userReader.readAll(anyList())).thenReturn(writerMap);

        // when
        List<CommentWithWriter> result = commentReader.readAll(articleId, pageSize, lastParentCommentId, lastCommentId);

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("캐시에 존재하는 댓글 수는 DB 조회 없이 반환한다")
    void count_allCacheHit_success() {
        // given
        List<Long> articleIds = List.of(1L, 2L);
        List<ArticleCommentCount> cached = List.of(
            ArticleCommentCount.init(1L),
            ArticleCommentCount.init(2L)
        );
        when(articleCommentCountCacheRepository.getAll(articleIds)).thenReturn(cached);

        // when
        Map<Long, Long> result = commentReader.count(articleIds);

        // then
        assertThat(result).containsEntry(1L, 1L).containsEntry(2L, 1L);
    }

    @Test
    @DisplayName("일부 캐시 미스 시 DB에서 조회하고, 캐시에 저장한 뒤 합쳐서 반환한다")
    void count_partialCacheMiss_success() {
        // given
        List<Long> articleIds = List.of(1L, 2L, 3L);
        List<ArticleCommentCount> cached = List.of(
            ArticleCommentCount.init(1L),
            ArticleCommentCount.init(3L)
        );
        List<ArticleCommentCount> uncached = List.of(
            ArticleCommentCount.init(2L)
        );
        when(articleCommentCountCacheRepository.getAll(articleIds)).thenReturn(cached);
        when(articleCommentCountRepository.findAllById(List.of(2L))).thenReturn(uncached);

        // when
        Map<Long, Long> result = commentReader.count(articleIds);

        // then
        assertThat(result).containsEntry(1L, 1L)
            .containsEntry(2L, 1L)
            .containsEntry(3L, 1L);
    }

    @Test
    @DisplayName("모두 캐시 미스일 경우 DB에서 모두 조회하고 캐시에 저장 후 반환한다")
    void count_allCacheMiss_success() {
        // given
        List<Long> articleIds = List.of(1L, 2L);
        List<ArticleCommentCount> fromDb = List.of(
            ArticleCommentCount.init(1L),
            ArticleCommentCount.init(2L)
        );
        when(articleCommentCountCacheRepository.getAll(articleIds)).thenReturn(List.of());
        when(articleCommentCountRepository.findAllById(articleIds)).thenReturn(fromDb);

        // when
        Map<Long, Long> result = commentReader.count(articleIds);

        // then
        assertThat(result).containsEntry(1L, 1L).containsEntry(2L, 1L);
    }

}