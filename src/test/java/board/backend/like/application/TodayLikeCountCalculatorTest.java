package board.backend.like.application;

import board.backend.common.support.fake.FakeTimeProvider;
import board.backend.like.application.fake.FakeArticleCommentRepository;
import board.backend.like.application.fake.FakeArticleLikeCountSnapshotRepository;
import board.backend.like.domain.ArticleLikeCount;
import board.backend.like.domain.ArticleLikeCountSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TodayLikeCountCalculatorTest {

    private FakeTimeProvider timeProvider;
    private FakeArticleCommentRepository articleLikeCountRepository;
    private FakeArticleLikeCountSnapshotRepository articleLikeCountSnapshotRepository;
    private TodayLikeCountCalculator todayLikeCountCalculator;

    @BeforeEach
    void setUp() {
        timeProvider = new FakeTimeProvider(LocalDateTime.of(2024, 1, 1, 12, 0));
        articleLikeCountRepository = new FakeArticleCommentRepository();
        articleLikeCountSnapshotRepository = new FakeArticleLikeCountSnapshotRepository();
        todayLikeCountCalculator = new TodayLikeCountCalculator(
            timeProvider,
            articleLikeCountRepository,
            articleLikeCountSnapshotRepository
        );
    }

    @Test
    @DisplayName("현재 좋아요 수에서 어제 좋아요 수를 뺀 값을 반환한다")
    void calculate_returnsCorrectDifference() {
        // given
        Long articleId = 1L;
        articleLikeCountRepository.increaseOrSave(new ArticleLikeCount(articleId, 20L));
        articleLikeCountSnapshotRepository.saveAll(List.of(
            new ArticleLikeCountSnapshot(timeProvider.yesterday(), articleId, 15L)
        ));

        // when
        long result = todayLikeCountCalculator.calculate(articleId);

        // then
        assertThat(result).isEqualTo(5L);
    }

    @Test
    @DisplayName("어제의 좋아요 수가 없으면 현재 수치를 그대로 반환한다")
    void calculate() {
        // given
        Long articleId = 1L;
        articleLikeCountRepository.increaseOrSave(new ArticleLikeCount(articleId, 8L));

        // when
        long result = todayLikeCountCalculator.calculate(articleId);

        // then
        assertThat(result).isEqualTo(8L);
    }

    @Test
    @DisplayName("현재 좋아요 수가 없으면 0을 반환한다")
    void calculate_returnsZero() {
        // given
        Long articleId = 1L;
        articleLikeCountSnapshotRepository.saveAll(List.of(
            new ArticleLikeCountSnapshot(timeProvider.yesterday(), articleId, 10L)
        ));

        // when
        long result = todayLikeCountCalculator.calculate(articleId);

        // then
        assertThat(result).isEqualTo(0L);
    }

    @Test
    @DisplayName("어제 수치가 더 높아도 음수가 아닌 0을 반환한다")
    void calculate_whenYesterdayHigher_returnsZero() {
        // given
        Long articleId = 1L;
        articleLikeCountRepository.increaseOrSave(new ArticleLikeCount(articleId, 3L));
        articleLikeCountSnapshotRepository.saveAll(List.of(
            new ArticleLikeCountSnapshot(timeProvider.yesterday(), articleId, 10L)
        ));

        // when
        long result = todayLikeCountCalculator.calculate(articleId);

        // then
        assertThat(result).isEqualTo(0L);
    }

}