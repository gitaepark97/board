package board.backend.hotArticle.application;

import board.backend.hotArticle.application.port.DailyArticleCountRepository;
import board.backend.hotArticle.application.port.DailyArticleViewCountRepository;
import board.backend.hotArticle.application.port.HotArticleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
class HotArticleDeleter {

    private final DailyArticleCountRepository dailyArticleLikeCountRepository;
    private final DailyArticleViewCountRepository dailyArticleViewCountRepository;
    private final DailyArticleCountRepository dailyArticleCommentCountRepository;
    private final HotArticleRepository hotArticleRepository;

    HotArticleDeleter(
        DailyArticleCountRepository dailyArticleLikeCountRepository,
        DailyArticleViewCountRepository dailyArticleViewCountRepository,
        @Qualifier("dailyArticleCommentCountRepository")
        DailyArticleCountRepository dailyArticleCommentCountRepository,
        HotArticleRepository hotArticleRepository
    ) {
        this.dailyArticleLikeCountRepository = dailyArticleLikeCountRepository;
        this.dailyArticleViewCountRepository = dailyArticleViewCountRepository;
        this.dailyArticleCommentCountRepository = dailyArticleCommentCountRepository;
        this.hotArticleRepository = hotArticleRepository;
    }

    void delete(Long articleId) {
        hotArticleRepository.delete(articleId);
        dailyArticleLikeCountRepository.deleteById(articleId);
        dailyArticleViewCountRepository.deleteById(articleId);
        dailyArticleCommentCountRepository.deleteById(articleId);
    }

}
