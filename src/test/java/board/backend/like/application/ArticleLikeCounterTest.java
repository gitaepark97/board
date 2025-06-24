package board.backend.like.application;

import board.backend.common.cache.fake.FakeCachedRepository;
import board.backend.like.application.fake.FakeArticleLikeCountRepository;
import board.backend.like.domain.ArticleLikeCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleLikeCounterTest {

    private FakeCachedRepository<ArticleLikeCount, Long> cachedRepository;
    private FakeArticleLikeCountRepository articleLikeCountRepository;
    private ArticleLikeCounter articleLikeCounter;

    @BeforeEach
    void setUp() {
        cachedRepository = new FakeCachedRepository<>();
        articleLikeCountRepository = new FakeArticleLikeCountRepository();
        articleLikeCounter = new ArticleLikeCounter(cachedRepository, articleLikeCountRepository);
    }

    @Test
    @DisplayName("좋아요 수가 캐시에 있을 경우 해당 값을 반환한다")
    void count_success_whenCached_returnsFromCache() {
        // given
        ArticleLikeCount articleLikeCount = ArticleLikeCount.builder()
            .articleId(1L)
            .count(5L)
            .build();
        cachedRepository.save(articleLikeCount.getArticleId(), articleLikeCount, Duration.ofMinutes(10));

        // when
        Long count = articleLikeCounter.count(articleLikeCount.getArticleId());

        // then
        assertThat(count).isEqualTo(articleLikeCount.getCount());
    }

    @Test
    @DisplayName("좋아요 수가 캐시에 없고 저장소에 있으면 저장소 값을 반환하고 캐시에 저장한다")
    void count_success_whenNotCached_returnsFromRepository() {
        // given
        ArticleLikeCount count = ArticleLikeCount.builder().articleId(2L).count(10L).build();
        articleLikeCountRepository.save(count);

        // when
        Long result = articleLikeCounter.count(count.getArticleId());

        // then
        assertThat(result).isEqualTo(count.getCount());
        assertThat(cachedRepository.findByKey(count.getArticleId())).contains(count);
    }

    @Test
    @DisplayName("좋아요 수가 캐시와 DB 모두 없으면 0을 반환한다")
    void count_success_whenNotExist_returnsZero() {
        // when
        Long count = articleLikeCounter.count(999L);

        // then
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @DisplayName("여러 게시글의 좋아요 수를 반환한다 (캐시, 저장소 조합)")
    void count_success_whenMultipleArticles_returnsMap() {
        // given
        ArticleLikeCount count1 = ArticleLikeCount.builder()
            .articleId(1L)
            .count(10L)
            .build();
        ArticleLikeCount count2 = ArticleLikeCount.builder()
            .articleId(2L)
            .count(5L)
            .build();
        Long notExistId = 3L;
        cachedRepository.save(count1.getArticleId(), count1, Duration.ofMinutes(10));
        articleLikeCountRepository.increase(count2.getArticleId());

        // when
        Map<Long, Long> result = articleLikeCounter.count(List.of(count1.getArticleId(), count2.getArticleId(), notExistId));

        // then
        assertThat(result).containsEntry(count1.getArticleId(), count1.getCount());
        assertThat(result).containsEntry(count2.getArticleId(), 1L);
        assertThat(result).containsEntry(notExistId, 0L);
    }

}