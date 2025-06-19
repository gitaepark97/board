package board.backend.comment.application;

import board.backend.comment.application.fake.FakeArticleCommentCountRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.common.infra.fake.FakeCachedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CommentCounterTest {

    private FakeCachedRepository<ArticleCommentCount, Long> cachedRepository;
    private FakeArticleCommentCountRepository commentCountRepository;
    private CommentCounter commentCounter;

    @BeforeEach
    void setUp() {
        cachedRepository = new FakeCachedRepository<>();
        commentCountRepository = new FakeArticleCommentCountRepository();
        commentCounter = new CommentCounter(cachedRepository, commentCountRepository);
    }

    @Test
    @DisplayName("댓글 수가 캐시에 있으면 캐시 값을 반환한다")
    void count_success_whenCached_returnsCachedValue() {
        // given
        ArticleCommentCount count = new ArticleCommentCount(1L, 5L);
        cachedRepository.save(count.articleId(), count, Duration.ofMinutes(10));

        // when
        Long result = commentCounter.count(count.articleId());

        // then
        assertThat(result).isEqualTo(5L);
    }

    @Test
    @DisplayName("댓글 수가 캐시에 없고 저장소에 있으면 저장소 값을 반환하고 캐시에 저장한다")
    void count_success_whenOnlyInRepository_returnsAndCachesValue() {
        // given
        ArticleCommentCount count = new ArticleCommentCount(2L, 10L);
        commentCountRepository.save(count);

        // when
        Long result = commentCounter.count(count.articleId());

        // then
        assertThat(result).isEqualTo(10L);
        assertThat(cachedRepository.findByKey(count.articleId())).contains(count);
    }

    @Test
    @DisplayName("댓글 수가 캐시에도 저장소에도 없으면 0을 반환한다")
    void count_success_whenNotExist_returnsZero() {
        // given
        Long articleId = 3L;

        // when
        Long result = commentCounter.count(articleId);

        // then
        assertThat(result).isEqualTo(0L);
    }

    @Test
    @DisplayName("여러 게시글의 댓글 수를 조회한다")
    void count_success_whenMultipleArticles_returnsAllCounts() {
        // given
        ArticleCommentCount count1 = new ArticleCommentCount(1L, 5L);
        ArticleCommentCount count2 = new ArticleCommentCount(2L, 10L);
        Long notExistId = 3L;
        cachedRepository.save(1L, count1, Duration.ofMinutes(10));
        commentCountRepository.save(count2);

        // when
        Map<Long, Long> result = commentCounter.count(List.of(count1.articleId(), count2.articleId(), notExistId));

        // then
        assertThat(result).containsEntry(count1.articleId(), count1.commentCount());
        assertThat(result).containsEntry(count2.articleId(), count2.commentCount());
        assertThat(result).containsEntry(notExistId, 0L);
    }

}