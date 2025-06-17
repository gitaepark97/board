package board.backend.hotArticle.application;

import board.backend.hotArticle.application.port.DailyArticleCountRepository;
import board.backend.hotArticle.application.port.DailyArticleViewCountRepository;
import board.backend.hotArticle.application.port.HotArticleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
class HotArticleScoreScoreCalculator {

    private static final long ARTICLE_LIKE_COUNT_WEIGHT = 3;
    private static final long ARTICLE_COMMENT_COUNT_WEIGHT = 2;
    private static final long ARTICLE_VIEW_COUNT_WEIGHT = 1;
    private static final long HOT_ARTICLE_COUNT = 10;
    private static final Duration HOT_ARTICLE_TTL = Duration.ofDays(10);

    private final DailyArticleCountRepository dailyArticleLikeCountRepository;
    private final DailyArticleViewCountRepository dailyArticleViewCountRepository;
    private final DailyArticleCountRepository dailyArticleCommentCountRepository;
    private final HotArticleRepository hotArticleRepository;
    private final TimeCalculator timeCalculator;

    HotArticleScoreScoreCalculator(
        DailyArticleCountRepository dailyArticleLikeCountRepository,
        DailyArticleViewCountRepository dailyArticleViewCountRepository,
        @Qualifier("dailyArticleCommentCountRepository")
        DailyArticleCountRepository dailyArticleCommentCountRepository,
        HotArticleRepository hotArticleRepository,
        TimeCalculator timeCalculator
    ) {
        this.dailyArticleLikeCountRepository = dailyArticleLikeCountRepository;
        this.dailyArticleViewCountRepository = dailyArticleViewCountRepository;
        this.dailyArticleCommentCountRepository = dailyArticleCommentCountRepository;
        this.hotArticleRepository = hotArticleRepository;
        this.timeCalculator = timeCalculator;
    }

    void increaseArticleLikeCount(Long articleId, LocalDateTime now) {
        dailyArticleLikeCountRepository.increaseOrSave(articleId, now, timeCalculator.calculateDurationToNoon());
        calculateScore(articleId, now);
    }

    void decreaseArticleLikeCount(Long articleId, LocalDateTime now) {
        dailyArticleLikeCountRepository.decrease(articleId, now);
        calculateScore(articleId, now);
    }

    void increaseArticleViewCount(Long articleId, Long count, LocalDateTime now) {
        dailyArticleViewCountRepository.save(articleId, count, now, timeCalculator.calculateDurationToNoon());
        calculateScore(articleId, now);
    }

    void increaseArticleCommentCount(Long articleId, LocalDateTime now) {
        dailyArticleCommentCountRepository.decrease(articleId, now);
        calculateScore(articleId, now);
    }

    void decreaseArticleCommentCount(Long articleId, LocalDateTime now) {
        dailyArticleCommentCountRepository.decrease(articleId, now);
        calculateScore(articleId, now);
    }

    private void calculateScore(Long articleId, LocalDateTime time) {
        Long articleLikeCount = dailyArticleLikeCountRepository.read(articleId, time);
        Long articleViewCount = dailyArticleViewCountRepository.read(articleId, time);
        Long articleCommentCount = dailyArticleCommentCountRepository.read(articleId, time);

        long score = articleLikeCount * ARTICLE_LIKE_COUNT_WEIGHT
            + articleViewCount * ARTICLE_VIEW_COUNT_WEIGHT
            + articleCommentCount * ARTICLE_COMMENT_COUNT_WEIGHT;

        hotArticleRepository.save(articleId, time, score, HOT_ARTICLE_COUNT, HOT_ARTICLE_TTL);
    }

}
