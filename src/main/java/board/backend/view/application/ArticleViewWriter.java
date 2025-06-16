package board.backend.view.application;

import board.backend.common.event.ArticleViewCountIncreasedEvent;
import board.backend.common.support.TimeProvider;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.application.port.ArticleViewDistributedLockRepository;
import board.backend.view.domain.ArticleViewCount;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
class ArticleViewWriter {

    private static final Duration TTL = Duration.ofMinutes(10);

    private final TimeProvider timeProvider;
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    void saveCount(Long articleId) {
        articleViewCountRepository.save(ArticleViewCount.init(articleId));
    }

    @Transactional
    void increaseCount(Long articleId, String ip) {
        if (!articleViewDistributedLockRepository.lock(articleId, ip, TTL)) {
            return;
        }

        articleViewCountRepository.increase(articleId);

        // 게시글 조회 이벤트 발행
        applicationEventPublisher.publishEvent(new ArticleViewCountIncreasedEvent(articleId, timeProvider.now()));

    }

    @Transactional
    void deleteArticle(Long articleId) {
        articleViewCountRepository.deleteById(articleId);
    }

}
