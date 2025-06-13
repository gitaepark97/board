package board.backend.view.application;

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

    private static final Duration TTL = Duration.ofMinutes(10);

    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;

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
    }

    @Transactional
    void deleteArticle(Long articleId) {
        articleViewCountRepository.deleteById(articleId);
    }

}
