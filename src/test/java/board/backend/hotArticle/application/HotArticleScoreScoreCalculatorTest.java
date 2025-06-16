package board.backend.hotArticle.application;

import board.backend.hotArticle.application.port.DailyArticleCountRepository;
import board.backend.hotArticle.application.port.HotArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HotArticleScoreScoreCalculatorTest {

    private DailyArticleCountRepository likeRepo;
    private DailyArticleCountRepository viewRepo;
    private DailyArticleCountRepository commentRepo;
    private TimeCalculator timeCalculator;
    private HotArticleScoreScoreCalculator calculator;

    @BeforeEach
    void setUp() {
        likeRepo = mock(DailyArticleCountRepository.class);
        viewRepo = mock(DailyArticleCountRepository.class);
        commentRepo = mock(DailyArticleCountRepository.class);
        HotArticleRepository hotArticleRepository = mock(HotArticleRepository.class);
        timeCalculator = mock(TimeCalculator.class);

        calculator = new HotArticleScoreScoreCalculator(
            likeRepo,
            viewRepo,
            commentRepo,
            hotArticleRepository,
            timeCalculator
        );
    }

    @Test
    @DisplayName("좋아요 증가 후 점수 계산")
    void increaseArticleLikeCount_shouldCalculateScoreAndSave() {
        // given
        Long articleId = 1L;
        LocalDateTime now = LocalDateTime.now();
        when(timeCalculator.calculateDurationToNoon()).thenReturn(Duration.ofHours(12));
        when(likeRepo.read(articleId, now)).thenReturn(5L);
        when(viewRepo.read(articleId, now)).thenReturn(10L);
        when(commentRepo.read(articleId, now)).thenReturn(2L);

        // when
        calculator.increaseArticleLikeCount(articleId, now);
    }

    @Test
    @DisplayName("좋아요 감소 후 점수 계산")
    void decreaseArticleLikeCount_shouldCalculateScoreAndSave() {
        // given
        Long articleId = 1L;
        LocalDateTime now = LocalDateTime.now();
        when(likeRepo.read(articleId, now)).thenReturn(2L);
        when(viewRepo.read(articleId, now)).thenReturn(4L);
        when(commentRepo.read(articleId, now)).thenReturn(1L);

        // when
        calculator.decreaseArticleLikeCount(articleId, now);
    }

    @Test
    @DisplayName("조회수 증가 후 점수 계산")
    void increaseArticleViewCount_shouldCalculateScoreAndSave() {
        // given
        Long articleId = 1L;
        LocalDateTime now = LocalDateTime.now();
        when(timeCalculator.calculateDurationToNoon()).thenReturn(Duration.ofHours(10));
        when(likeRepo.read(articleId, now)).thenReturn(3L);
        when(viewRepo.read(articleId, now)).thenReturn(6L);
        when(commentRepo.read(articleId, now)).thenReturn(1L);

        // when
        calculator.increaseArticleViewCount(articleId, now);
    }

    @Test
    @DisplayName("댓글 생성 시 점수 계산")
    void increaseArticleCommentCount_shouldCalculateScoreAndSave() {
        // given
        Long articleId = 1L;
        LocalDateTime now = LocalDateTime.now();
        when(likeRepo.read(articleId, now)).thenReturn(1L);
        when(viewRepo.read(articleId, now)).thenReturn(2L);
        when(commentRepo.read(articleId, now)).thenReturn(5L);

        // when
        calculator.increaseArticleCommentCount(articleId, now);
    }

    @Test
    @DisplayName("댓글 삭제 시 점수 계산")
    void decreaseArticleCommentCount_shouldCalculateScoreAndSave() {
        // given
        Long articleId = 2L;
        LocalDateTime now = LocalDateTime.now();
        when(likeRepo.read(articleId, now)).thenReturn(0L);
        when(viewRepo.read(articleId, now)).thenReturn(1L);
        when(commentRepo.read(articleId, now)).thenReturn(3L);

        // when
        calculator.decreaseArticleCommentCount(articleId, now);
    }

}