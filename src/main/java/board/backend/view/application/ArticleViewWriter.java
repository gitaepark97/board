package board.backend.view.application;

import board.backend.common.event.ArticleViewCountIncreasedEvent;
import board.backend.common.support.TimeProvider;
import board.backend.view.application.port.ArticleViewBackupTimeRepository;
import board.backend.view.application.port.ArticleViewCountBackupRepository;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.application.port.ArticleViewDistributedLockRepository;
import board.backend.view.domain.ArticleViewCount;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
class ArticleViewWriter {

    private static final Duration BACK_UP_INTERVAL = Duration.ofSeconds(60);
    private static final Duration TTL = Duration.ofMinutes(10);

    private final TimeProvider timeProvider;
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewCountBackupRepository articleViewCountBackUpRepository;
    private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
    private final ArticleViewBackupTimeRepository articleViewBackupTimeRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    void createCount(Long articleId) {
        articleViewCountRepository.save(ArticleViewCount.init(articleId));
    }

    void increaseCount(Long articleId, String ip) {
        if (!articleViewDistributedLockRepository.lock(articleId, ip, TTL)) {
            return;
        }

        Long count = articleViewCountRepository.increase(articleId);
        if (shouldBackUp(articleId)) {
            // 게시글 조회 수 백업
            articleViewCountBackUpRepository.save(ArticleViewCount.create(articleId, count));
            articleViewBackupTimeRepository.update(articleId, timeProvider.now());

            // 게시글 조회 이벤트 발행
            applicationEventPublisher.publishEvent(new ArticleViewCountIncreasedEvent(articleId, count, timeProvider.now()));
        }
    }

    @Transactional
    void deleteArticle(Long articleId) {
        articleViewCountBackUpRepository.deleteById(articleId);
        articleViewCountRepository.deleteById(articleId);
    }

    private boolean shouldBackUp(Long articleId) {
        LocalDateTime last = articleViewBackupTimeRepository.findById(articleId);
        return last == null || last.plus(BACK_UP_INTERVAL).isBefore(timeProvider.now());
    }

}
