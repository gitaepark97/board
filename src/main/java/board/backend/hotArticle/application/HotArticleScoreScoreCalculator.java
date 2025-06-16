package board.backend.hotArticle.application;

import board.backend.hotArticle.application.port.DailyArticleCountRepository;
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
    private final DailyArticleCountRepository dailyArticleViewCountRepository;
    private final DailyArticleCountRepository dailyArticleCommentCountRepository;
    private final HotArticleRepository hotArticleRepository;
    private final TimeCalculator timeCalculator;

    HotArticleScoreScoreCalculator(
        DailyArticleCountRepository dailyArticleLikeCountRepository,
        @Qualifier("dailyArticleViewCountRepository")
        DailyArticleCountRepository dailyArticleViewCountRepository,
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

    void afterArticleLiked(Long articleId, LocalDateTime likedAt) {
        dailyArticleLikeCountRepository.increaseOrSave(articleId, likedAt, timeCalculator.calculateDurationToNoon());
        calculateScore(articleId, likedAt);
    }

    void afterArticleUnliked(Long articleId, LocalDateTime unlikedAt) {
        dailyArticleLikeCountRepository.decrease(articleId, unlikedAt);
        calculateScore(articleId, unlikedAt);
    }

    void afterArticleViewed(Long articleId, LocalDateTime viewedAt) {
        dailyArticleViewCountRepository.increaseOrSave(articleId, viewedAt, timeCalculator.calculateDurationToNoon());
        calculateScore(articleId, viewedAt);
    }

    void afterCommentCreated(Long articleId, LocalDateTime createdAt) {
        dailyArticleCommentCountRepository.decrease(articleId, createdAt);
        calculateScore(articleId, createdAt);
    }

    void afterCommentDeleted(Long articleId, LocalDateTime deleteAt) {
        dailyArticleCommentCountRepository.decrease(articleId, deleteAt);
        calculateScore(articleId, deleteAt);
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
