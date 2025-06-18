package board.backend.view.application;

import board.backend.common.event.EventPublisher;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleViewedEventPayload;
import board.backend.common.support.TimeProvider;
import board.backend.view.application.port.ArticleViewCountBackupRepository;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.application.port.ArticleViewDistributedLockRepository;
import board.backend.view.domain.ArticleViewCount;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
class ArticleViewWriter {

    private static final int BACK_UP_BATCH_SIZE = 10;
    private static final Duration TTL = Duration.ofMinutes(1);

    private final TimeProvider timeProvider;
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewCountBackupRepository articleViewCountBackUpRepository;
    private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
    private final EventPublisher eventPublisher;

    void createCount(Long articleId) {
        articleViewCountRepository.save(ArticleViewCount.init(articleId));
    }

    void increaseCount(Long articleId, String ip) {
        if (!articleViewDistributedLockRepository.lock(articleId, ip, TTL)) {
            return;
        }

        Long count = articleViewCountRepository.increase(articleId);
        if (count % BACK_UP_BATCH_SIZE == 0) {
            // 게시글 조회 수 백업
            articleViewCountBackUpRepository.save(ArticleViewCount.create(articleId, count));

            // 게시글 조회 이벤트 발행
            eventPublisher.publishEvent(EventType.ARTICLE_VIEWED, new ArticleViewedEventPayload(articleId, count, timeProvider.now()));
        }
    }

    @Transactional
    void deleteArticle(Long articleId) {
        articleViewCountBackUpRepository.deleteById(articleId);
        articleViewCountRepository.deleteById(articleId);
    }

}
