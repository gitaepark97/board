package board.backend.view.application;

import board.backend.view.application.fake.FakeArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleViewCounterTest {

    private FakeArticleViewCountRepository articleViewCountRepository;
    private ArticleViewCounter articleViewCounter;

    @BeforeEach
    void setUp() {
        articleViewCountRepository = new FakeArticleViewCountRepository();
        articleViewCounter = new ArticleViewCounter(articleViewCountRepository);
    }

    @Test
    @DisplayName("게시글 ID로 조회 수를 조회할 수 있다")
    void count_success_whenArticleExists_returnsViewCount() {
        // given
        ArticleViewCount articleViewCount = new ArticleViewCount(1L, 123L);
        articleViewCountRepository.save(articleViewCount);

        // when
        Long count = articleViewCounter.count(articleViewCount.articleId());

        // then
        assertThat(count).isEqualTo(articleViewCount.viewCount());
    }

    @Test
    @DisplayName("여러 게시글 ID로 조회 수를 조회할 수 있다")
    void count_success_whenMultipleArticles_returnsViewCountMap() {
        // given
        ArticleViewCount articleViewCount1 = new ArticleViewCount(1L, 10L);
        ArticleViewCount articleViewCount2 = new ArticleViewCount(2L, 20L);
        ArticleViewCount articleViewCount3 = new ArticleViewCount(3L, 30L);
        articleViewCountRepository.save(articleViewCount1);
        articleViewCountRepository.save(articleViewCount2);
        articleViewCountRepository.save(articleViewCount3);

        // when
        Map<Long, Long> result = articleViewCounter.count(List.of(articleViewCount1.articleId(), articleViewCount2.articleId(), articleViewCount3.articleId()));

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
            articleViewCount1.articleId(), articleViewCount1.viewCount(),
            articleViewCount2.articleId(), articleViewCount2.viewCount(),
            articleViewCount3.articleId(), articleViewCount3.viewCount()
        ));
    }

    @Test
    @DisplayName("조회 수가 없는 게시글은 0을 반환한다")
    void count_success_whenMissing_returnsZero() {
        // when
        Long count = articleViewCounter.count(999L);

        // then
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @DisplayName("조회 수가 없는 게시글은 0으로 채운다")
    void count_success_whenSomeMissing_returnsZeroForMissing() {
        // given
        ArticleViewCount count1 = new ArticleViewCount(1L, 100L);
        Long notExistId = 2L;
        articleViewCountRepository.save(count1);

        // when
        Map<Long, Long> result = articleViewCounter.count(List.of(count1.articleId(), notExistId));

        // then
        assertThat(result.get(count1.articleId())).isEqualTo(count1.viewCount());
        assertThat(result.get(notExistId)).isEqualTo(0L);
    }

}