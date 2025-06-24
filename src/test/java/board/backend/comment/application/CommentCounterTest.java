package board.backend.comment.application;

import board.backend.comment.application.fake.FakeArticleCommentCountRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.common.cache.fake.FakeCachedRepository;
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
        ArticleCommentCount count = ArticleCommentCount.builder().articleId(1L).count(5L).build();
        cachedRepository.save(count.getArticleId(), count, Duration.ofMinutes(10));

        // when
        Long result = commentCounter.count(count.getArticleId());

        // then
        assertThat(result).isEqualTo(count.getCount());
    }

    @Test
    @DisplayName("댓글 수가 캐시에 없고 저장소에 있으면 저장소 값을 반환하고 캐시에 저장한다")
    void count_success_whenOnlyInRepository_returnsAndCachesValue() {
        // given
        ArticleCommentCount count = ArticleCommentCount.builder().articleId(2L).count(10L).build();
        commentCountRepository.save(count);

        // when
        Long result = commentCounter.count(count.getArticleId());

        // then
        assertThat(result).isEqualTo(count.getCount());
        assertThat(cachedRepository.findByKey(count.getArticleId())).contains(count);
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
        ArticleCommentCount count1 = ArticleCommentCount.builder().articleId(1L).count(5L).build();
        ArticleCommentCount count2 = ArticleCommentCount.builder().articleId(2L).count(10L).build();
        Long notExistId = 3L;
        cachedRepository.save(1L, count1, Duration.ofMinutes(10));
        commentCountRepository.save(count2);

        // when
        Map<Long, Long> result = commentCounter.count(List.of(count1.getArticleId(), count2.getArticleId(), notExistId));

        // then
        assertThat(result).containsEntry(count1.getArticleId(), count1.getCount());
        assertThat(result).containsEntry(count2.getArticleId(), count2.getCount());
        assertThat(result).containsEntry(notExistId, 0L);
    }

}