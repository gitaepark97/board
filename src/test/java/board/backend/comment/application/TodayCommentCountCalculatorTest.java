package board.backend.comment.application;

import board.backend.comment.application.fake.FakeArticleCommentCountRepository;
import board.backend.comment.application.fake.FakeArticleCommentCountSnapshotRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.ArticleCommentCountSnapshot;
import board.backend.common.support.fake.FakeTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TodayCommentCountCalculatorTest {

    private FakeTimeProvider timeProvider;
    private FakeArticleCommentCountRepository articleCommentCountRepository;
    private FakeArticleCommentCountSnapshotRepository articleCommentCountSnapshotRepository;
    private TodayCommentCountCalculator todayCommentCountCalculator;

    @BeforeEach
    void setUp() {
        timeProvider = new FakeTimeProvider(LocalDateTime.of(2024, 1, 1, 12, 0));
        articleCommentCountRepository = new FakeArticleCommentCountRepository();
        articleCommentCountSnapshotRepository = new FakeArticleCommentCountSnapshotRepository();
        todayCommentCountCalculator = new TodayCommentCountCalculator(
            timeProvider,
            articleCommentCountRepository,
            articleCommentCountSnapshotRepository
        );
    }

    @Test
    @DisplayName("현재 좋아요 수에서 어제 좋아요 수를 뺀 값을 반환한다")
    void calculateTodayLikeCount_returnsCorrectDifference() {
        // given
        Long articleId = 1L;
        articleCommentCountRepository.save(new ArticleCommentCount(articleId, 20L));
        articleCommentCountSnapshotRepository.saveAll(List.of(
            new ArticleCommentCountSnapshot(timeProvider.yesterday(), articleId, 15L)
        ));

        // when
        long result = todayCommentCountCalculator.calculate(articleId);

        // then
        assertThat(result).isEqualTo(5L);
    }

    @Test
    @DisplayName("어제의 좋아요 수가 없으면 현재 수치를 그대로 반환한다")
    void calculateTodayLikeCount_whenNoSnapshot_returnsFullCount() {
        // given
        Long articleId = 1L;
        articleCommentCountRepository.save(new ArticleCommentCount(articleId, 8L));

        // when
        long result = todayCommentCountCalculator.calculate(articleId);

        // then
        assertThat(result).isEqualTo(8L);
    }

    @Test
    @DisplayName("현재 좋아요 수가 없으면 0을 반환한다")
    void calculateTodayLikeCount_whenNoCurrentCount_returnsZero() {
        // given
        Long articleId = 1L;
        articleCommentCountSnapshotRepository.saveAll(List.of(
            new ArticleCommentCountSnapshot(timeProvider.yesterday(), articleId, 10L)
        ));

        // when
        long result = todayCommentCountCalculator.calculate(articleId);

        // then
        assertThat(result).isEqualTo(0L);
    }

    @Test
    @DisplayName("어제 수치가 더 높아도 음수가 아닌 0을 반환한다")
    void calculateTodayLikeCount_whenYesterdayHigher_returnsZero() {
        // given
        Long articleId = 1L;
        articleCommentCountRepository.increaseOrSave(new ArticleCommentCount(articleId, 3L));
        articleCommentCountSnapshotRepository.saveAll(List.of(
            new ArticleCommentCountSnapshot(timeProvider.yesterday(), articleId, 10L)
        ));

        // when
        long result = todayCommentCountCalculator.calculate(articleId);

        // then
        assertThat(result).isEqualTo(0L);
    }

}