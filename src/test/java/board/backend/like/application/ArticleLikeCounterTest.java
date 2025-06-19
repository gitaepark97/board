package board.backend.like.application;

import board.backend.common.infra.fake.FakeCachedRepository;
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
        ArticleLikeCount ArticleLikeCount = new ArticleLikeCount(1L, 5L);
        cachedRepository.save(ArticleLikeCount.articleId(), ArticleLikeCount, Duration.ofMinutes(10));

        // when
        Long count = articleLikeCounter.count(ArticleLikeCount.articleId());

        // then
        assertThat(count).isEqualTo(ArticleLikeCount.likeCount());
    }

    @Test
    @DisplayName("좋아요 수가 캐시에 없고 DB에 있을 경우 해당 값을 반환한다")
    void count_success_whenNotCached_returnsFromRepository() {
        // given
        ArticleLikeCount articleLikeCount = new ArticleLikeCount(1L, 3L);
        articleLikeCountRepository.increaseOrSave(articleLikeCount);

        // when
        Long count = articleLikeCounter.count(articleLikeCount.articleId());

        // then
        assertThat(count).isEqualTo(articleLikeCount.likeCount());
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
        ArticleLikeCount count1 = new ArticleLikeCount(1L, 10L);
        ArticleLikeCount count2 = new ArticleLikeCount(2L, 5L);
        Long notExistId = 3L;
        cachedRepository.save(count1.articleId(), count1, Duration.ofMinutes(10));
        articleLikeCountRepository.increaseOrSave(count2);

        // when
        Map<Long, Long> result = articleLikeCounter.count(List.of(count1.articleId(), count2.articleId(), notExistId));

        // then
        assertThat(result).containsEntry(count1.articleId(), count1.likeCount());
        assertThat(result).containsEntry(count2.articleId(), count2.likeCount());
        assertThat(result).containsEntry(notExistId, 0L);
    }

}