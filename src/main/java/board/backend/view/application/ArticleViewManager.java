package board.backend.view.application;

import board.backend.common.event.EventPublisher;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleViewedEventPayload;
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
    private final TodayViewCountCalculator todayViewCountCalculator;

    void increaseCount(Long articleId, String ip) {
        if (!articleViewDistributedLockRepository.lock(articleId, ip, TTL)) {
            return;
        }

        long currentCount = articleViewCountRepository.increase(articleId);
        if (currentCount % BACK_UP_BATCH_SIZE != 0) {
            return;
        }

        backupViewCount(articleId, currentCount);
        publishViewEvent(articleId, currentCount);
    }

    private void backupViewCount(Long articleId, Long currentCount) {
        articleViewCountBackUpRepository.save(ArticleViewCount.create(articleId, currentCount));
    }

    private void publishViewEvent(Long articleId, Long currentCount) {
        long todayCount = todayViewCountCalculator.calculate(articleId, currentCount);

        eventPublisher.publishEvent(
            EventType.ARTICLE_VIEWED,
            new ArticleViewedEventPayload(articleId, todayCount, timeProvider.now())
        );
    }

}
