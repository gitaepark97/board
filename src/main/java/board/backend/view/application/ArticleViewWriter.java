package board.backend.view.application;

import board.backend.view.domain.ArticleViewCount;
import board.backend.view.infra.ArticleViewCountRepository;
import board.backend.view.infra.ArticleViewDistributedLockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class ArticleViewWriter {

    private static final Duration TTL = Duration.ofMinutes(10);

    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;

    @Transactional
    public void increaseCount(Long articleId, String ip) {
        if (!articleViewDistributedLockRepository.lock(articleId, ip, TTL)) {
            return;
        }

        ArticleViewCount articleViewCount = ArticleViewCount.init(articleId);
        articleViewCountRepository.upsert(articleViewCount.getArticleId(), articleViewCount.getViewCount());
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        articleViewCountRepository.deleteById(articleId);
    }

}
