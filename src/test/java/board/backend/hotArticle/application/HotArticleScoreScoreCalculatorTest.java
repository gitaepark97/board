package board.backend.hotArticle.application;

import board.backend.hotArticle.application.fake.FakeDailyArticleCountRepository;
import board.backend.hotArticle.application.fake.FakeHotArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class HotArticleScoreScoreCalculatorTest {

    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);

    private FakeDailyArticleCountRepository likeCountRepository;
    private FakeDailyArticleCountRepository viewCountRepository;
    private FakeDailyArticleCountRepository commentCountRepository;
    private FakeHotArticleRepository hotArticleRepository;
    private HotArticleScoreScoreCalculator calculator;

    @BeforeEach
    void setUp() {
        likeCountRepository = new FakeDailyArticleCountRepository();
        viewCountRepository = new FakeDailyArticleCountRepository();
        commentCountRepository = new FakeDailyArticleCountRepository();
        hotArticleRepository = new FakeHotArticleRepository();
        calculator = new HotArticleScoreScoreCalculator(
            likeCountRepository,
            viewCountRepository,
            commentCountRepository,
            hotArticleRepository,
            new TimeCalculator()
        );
    }

    @Test
    @DisplayName("좋아요 수 증가 시 점수를 계산하고 핫 게시글로 저장한다")
    void increaseArticleLikeCount_success_savesHotArticle() {
        // given
        Long articleId = 1L;

        // when
        calculator.increaseArticleLikeCount(articleId, now);

        // then
        assertThat(hotArticleRepository.findById(articleId))
            .contains(new FakeHotArticleRepository.Entry(articleId, now, 3L));
    }

    @Test
    @DisplayName("댓글 수와 조회 수가 있을 때 정확한 점수를 계산한다")
    void calculateScore_success_combinesAllMetricsWithWeight() {
        // given
        Long articleId = 1L;
        likeCountRepository.increaseOrSave(articleId, now, Duration.ofDays(1)); // +3
        likeCountRepository.increaseOrSave(articleId, now, Duration.ofDays(1)); // +3
        commentCountRepository.increaseOrSave(articleId, now, Duration.ofDays(1)); // +2
        viewCountRepository.increaseOrSave(articleId, 5L, now, Duration.ofDays(1)); // +5

        // when
        calculator.increaseArticleCommentCount(articleId, now);

        // then
        // 좋아요 2회 → 6, 댓글 2회 → 4, 조회수 5 → 5 → 총합 15
        assertThat(hotArticleRepository.findById(articleId))
            .contains(new FakeHotArticleRepository.Entry(articleId, now, 15L));
    }

}