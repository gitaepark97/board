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
        ArticleViewCount articleViewCount = ArticleViewCount.builder().articleId(1L).count(123L).build();
        articleViewCountRepository.save(articleViewCount);

        // when
        Long count = articleViewCounter.count(articleViewCount.getArticleId());

        // then
        assertThat(count).isEqualTo(articleViewCount.getCount());
    }

    @Test
    @DisplayName("여러 게시글 ID로 조회 수를 조회할 수 있다")
    void count_success_whenMultipleArticles_returnsViewCountMap() {
        // given
        ArticleViewCount articleViewCount1 = ArticleViewCount.builder().articleId(1L).count(10L).build();
        ArticleViewCount articleViewCount2 = ArticleViewCount.builder().articleId(2L).count(20L).build();
        ArticleViewCount articleViewCount3 = ArticleViewCount.builder().articleId(3L).count(30L).build();
        articleViewCountRepository.save(articleViewCount1);
        articleViewCountRepository.save(articleViewCount2);
        articleViewCountRepository.save(articleViewCount3);

        // when
        Map<Long, Long> result = articleViewCounter.count(List.of(articleViewCount1.getArticleId(), articleViewCount2.getArticleId(), articleViewCount3.getArticleId()));

        // then
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
            articleViewCount1.getArticleId(), articleViewCount1.getCount(),
            articleViewCount2.getArticleId(), articleViewCount2.getCount(),
            articleViewCount3.getArticleId(), articleViewCount3.getCount()
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
        ArticleViewCount count1 = ArticleViewCount.builder().articleId(1L).count(100L).build();
        Long notExistId = 2L;
        articleViewCountRepository.save(count1);

        // when
        Map<Long, Long> result = articleViewCounter.count(List.of(count1.getArticleId(), notExistId));

        // then
        assertThat(result.get(count1.getArticleId())).isEqualTo(count1.getCount());
        assertThat(result.get(notExistId)).isEqualTo(0L);
    }

}