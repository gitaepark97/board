package board.backend.view.application;

import board.backend.common.event.EventPublisher;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleViewCountChangedEventPayload;
import board.backend.common.support.TimeProvider;
import board.backend.view.application.port.ArticleViewCountBackupRepository;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.application.port.ArticleViewDistributedLockRepository;
import board.backend.view.domain.ArticleViewCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
class ArticleViewManager {

    private static final long BACK_UP_BATCH_SIZE = 10;
    private static final Duration TTL = Duration.ofMinutes(1);

    private final TimeProvider timeProvider;
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewCountBackupRepository articleViewCountBackUpRepository;
    private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
    private final EventPublisher eventPublisher;
    private final TodayViewCountCalculatorImpl todayViewCountCalculator;

    void increaseCount(Long articleId, String ip) {
        if (!articleViewDistributedLockRepository.lock(articleId, ip, TTL)) {
            return;
        }

        long currentCount = articleViewCountRepository.increase(articleId);
        if (currentCount % BACK_UP_BATCH_SIZE != 0) {
            return;
        }

        ArticleViewCount articleViewCount = ArticleViewCount.create(articleId, currentCount);
        backupViewCount(articleViewCount);
        publishViewEvent(articleViewCount);
    }

    private void backupViewCount(ArticleViewCount articleViewCount) {
        articleViewCountBackUpRepository.save(articleViewCount);
    }

    private void publishViewEvent(ArticleViewCount articleViewCount) {
        long todayCount = todayViewCountCalculator.calculate(articleViewCount);

        eventPublisher.publishEvent(
            EventType.ARTICLE_VIEW_COUNT_CHANGED,
            new ArticleViewCountChangedEventPayload(articleViewCount.getArticleId(), todayCount, timeProvider.now())
        );
    }

}
